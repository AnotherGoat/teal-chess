/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.square.AlgebraicConverter;

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

    protected void addMoves(MoveType type, long possibleMoves, int source) {
        if (possibleMoves == 0) {
            return;
        }

        var destination = firstBit(possibleMoves);

        do {
            moves.add(new Move(type, source, destination));

            possibleMoves = clear(possibleMoves, destination);
            destination = firstBit(possibleMoves);
        } while (isSet(possibleMoves, destination));
    }

    protected void addMoves(MoveType type, long possibleMoves, int fileDelta, int rankDelta) {
        if (possibleMoves == 0) {
            return;
        }

        var destination = firstBit(possibleMoves);

        do {
            var fileIndex = AlgebraicConverter.fileIndex(destination);
            var rankIndex = AlgebraicConverter.rankIndex(destination);
            var source = AlgebraicConverter.toSquare(fileIndex + fileDelta, rankIndex + rankDelta);

            moves.add(new Move(type, source, destination));

            possibleMoves = clear(possibleMoves, destination);
            destination = firstBit(possibleMoves);
        } while (isSet(possibleMoves, destination));
    }
}
