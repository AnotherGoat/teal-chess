/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;

import java.util.List;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.coordinate.AlgebraicConverter;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.position.Position;
import org.eclipse.jdt.annotation.Nullable;

final class PawnMoveGenerator extends MoveGenerator {

    private static final long RANK_1 = 0xffL;
    private static final long RANK_8 = 0xff00000000000000L;
    private static final int PUSH_OFFSET = 8;

    private final Color sideToMove;
    private final long emptyCoordinates;
    private final long pawns;
    private final long capturablePieces;
    private final @Nullable Integer enPassantTarget;

    PawnMoveGenerator(Position position) {
        super(position);
        var board = position.board();
        sideToMove = position.sideToMove();
        emptyCoordinates = board.emptyCoordinates();
        pawns = board.pawns(sideToMove);
        capturablePieces = board.capturablePieces(sideToMove);
        enPassantTarget = position.enPassantTarget();
    }

    @Override
    public List<Move> generate() {
        if (pawns == 0) {
            return moves;
        }

        addPawnPushes();

        return moves;
    }

    private void addPawnPushes() {
        var moveBitboard = sideToMove.isWhite() ? pawns << PUSH_OFFSET : pawns >> PUSH_OFFSET;
        var promotionRank = sideToMove.isWhite() ? RANK_8 : RANK_1;
        var possibleMoves = moveBitboard & emptyCoordinates & ~promotionRank;

        if (possibleMoves == 0) {
            return;
        }

        var rankDelta = sideToMove.isWhite() ? -1 : 1;
        addMoves(MoveType.PAWN_PUSH, possibleMoves, 0, rankDelta);
    }

    private void addMoves(MoveType moveType, long possibleMoves, int fileDelta, int rankDelta) {
        var start = firstBit(possibleMoves);
        var end = lastBit(possibleMoves);

        for (var i = start; i <= end; i++) {
            if (isSet(possibleMoves, i)) {
                var fileIndex = AlgebraicConverter.fileIndex(i);
                var rankIndex = AlgebraicConverter.rankIndex(i) + rankDelta;

                var source = AlgebraicConverter.toCoordinate(fileIndex, rankIndex);
                moves.add(new Move(moveType, source, i));
            }
        }
    }
}
