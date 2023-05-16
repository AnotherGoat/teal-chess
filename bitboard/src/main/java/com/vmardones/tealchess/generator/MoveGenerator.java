/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.coordinate.AlgebraicConverter;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.position.Position;

public abstract sealed class MoveGenerator
        permits BishopMoveGenerator,
                KingMoveGenerator,
                KnightMoveGenerator,
                PawnMoveGenerator,
                PseudoLegalGenerator,
                QueenMoveGenerator,
                RookMoveGenerator {

    protected final Position position;
    protected final List<Move> moves = new ArrayList<>();

    protected MoveGenerator(Position position) {
        this.position = position;
    }

    abstract List<Move> generate();

    protected void addMoves(MoveType moveType, long possibleMoves, int fileDelta, int rankDelta) {
        if (possibleMoves == 0) {
            return;
        }

        var start = firstBit(possibleMoves);
        var end = lastBit(possibleMoves);

        for (var destination = start; destination <= end; destination++) {
            if (isSet(possibleMoves, destination)) {
                var fileIndex = AlgebraicConverter.fileIndex(destination) + fileDelta;
                var rankIndex = AlgebraicConverter.rankIndex(destination) + rankDelta;

                var source = AlgebraicConverter.toCoordinate(fileIndex, rankIndex);
                moves.add(new Move(moveType, source, destination));
            }
        }
    }
}
