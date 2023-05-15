/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import java.util.List;

import com.vmardones.tealchess.board.BitboardManipulator;
import com.vmardones.tealchess.board.Board;
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
        switch (sideToMove) {
            case WHITE -> {
                addWhitePushes();
            }
            case BLACK -> {
                addBlackPushes();
            }
        }

        return moves;
    }

    private void addWhitePushes() {
        var moveBitboard = pawns << PUSH_OFFSET;
        var possibleMoves = moveBitboard & emptyCoordinates & ~RANK_8;

        var trailingZeros = Long.numberOfTrailingZeros(possibleMoves);

        for (var i = 0; i + trailingZeros < Board.NUMBER_OF_SQUARES; i++) {
            var destination = i + trailingZeros;

            if (BitboardManipulator.isSet(possibleMoves, destination)) {
                var fileIndex = AlgebraicConverter.fileIndex(destination);
                var rankIndex = AlgebraicConverter.rankIndex(destination) - 1;

                var source = AlgebraicConverter.toCoordinate(fileIndex, rankIndex);
                moves.add(new Move(MoveType.PAWN_PUSH, source, destination));
            }
        }
    }

    private void addBlackPushes() {
        var moveBitboard = pawns >> PUSH_OFFSET;
        var possibleMoves = moveBitboard & emptyCoordinates & ~RANK_1;

        var trailingZeros = Long.numberOfTrailingZeros(possibleMoves);

        for (var i = 0; i + trailingZeros > 0; i--) {
            var destination = i + trailingZeros;

            if (BitboardManipulator.isSet(possibleMoves, destination)) {
                var fileIndex = AlgebraicConverter.fileIndex(destination);
                var rankIndex = AlgebraicConverter.rankIndex(destination) + 1;

                var source = AlgebraicConverter.toCoordinate(fileIndex, rankIndex);
                moves.add(new Move(MoveType.PAWN_PUSH, source, destination));
            }
        }
    }
}
