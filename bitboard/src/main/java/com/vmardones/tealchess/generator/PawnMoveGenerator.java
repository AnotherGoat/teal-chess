/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.piece.PromotionChoice;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.square.AlgebraicConverter;

final class PawnMoveGenerator implements MoveGenerator {

    private static final long RANK_1 = 0xffL;
    private static final long RANK_4 = 0xff_00_00_00L;
    private static final long RANK_5 = 0xff_00_00_00_00L;
    private static final long RANK_8 = 0xff_00_00_00_00_00_00_00L;
    private static final long FILE_A = 0x01_01_01_01_01_01_01_01L;
    private static final long FILE_H = 0x80_80_80_80_80_80_80_80L;

    private static final int PUSH_OFFSET = 8;
    private static final int DOUBLE_PUSH_OFFSET = 16;
    private static final int LEFT_CAPTURE_OFFSET = 7;
    private static final int RIGHT_CAPTURE_OFFSET = 9;

    @Override
    public List<Move> generate(Position position) {
        var sideToMove = position.sideToMove();
        return sideToMove.isWhite() ? generateWhiteMoves(position) : generateBlackMoves(position);
    }

    PawnMoveGenerator() {}

    private List<Move> generateWhiteMoves(Position position) {
        var board = position.board();
        var pawns = board.pawns(Color.WHITE);

        if (pawns == 0) {
            return emptyList();
        }

        var emptySquares = board.emptySquares();
        var capturablePieces = board.capturablePieces(Color.WHITE);

        var moves = new ArrayList<Move>();

        var pushedPawns = pawns << PUSH_OFFSET;
        addMoves(moves, MoveType.PAWN_PUSH, pushedPawns & emptySquares & ~RANK_8, 0, 1);

        var doublePushedPawns = pawns << DOUBLE_PUSH_OFFSET;
        var skippedEmptySquares = emptySquares << PUSH_OFFSET;
        addMoves(moves, MoveType.DOUBLE_PUSH, doublePushedPawns & emptySquares & skippedEmptySquares & RANK_4, 0, 2);

        var leftCapturePawns = pawns << LEFT_CAPTURE_OFFSET;
        addMoves(moves, MoveType.PAWN_CAPTURE, leftCapturePawns & capturablePieces & ~RANK_8 & ~FILE_H, -1, 1);

        var rightCapturePawns = pawns << RIGHT_CAPTURE_OFFSET;
        addMoves(moves, MoveType.PAWN_CAPTURE, rightCapturePawns & capturablePieces & ~RANK_8 & ~FILE_A, 1, 1);

        var enPassantTarget = position.enPassantTarget();
        if (enPassantTarget != null) {
            var enPassantBitboard = singleBit(enPassantTarget);

            addEnPassantMove(moves, leftCapturePawns & enPassantBitboard & ~FILE_H, -1, 1);
            addEnPassantMove(moves, rightCapturePawns & enPassantBitboard & ~FILE_A, 1, 1);
        }

        addPromotionMoves(moves, MoveType.PAWN_PUSH, pushedPawns & emptySquares & RANK_8, 0, 1);
        addPromotionMoves(moves, MoveType.PAWN_CAPTURE, leftCapturePawns & capturablePieces & RANK_8 & ~FILE_H, -1, 1);
        addPromotionMoves(moves, MoveType.PAWN_CAPTURE, rightCapturePawns & capturablePieces & RANK_8 & ~FILE_A, 1, 1);

        return moves;
    }

    private List<Move> generateBlackMoves(Position position) {
        var board = position.board();
        var pawns = board.pawns(Color.BLACK);

        if (pawns == 0) {
            return emptyList();
        }

        var emptySquares = board.emptySquares();
        var capturablePieces = board.capturablePieces(Color.BLACK);

        var moves = new ArrayList<Move>();

        var pushedPawns = pawns >> PUSH_OFFSET;
        addMoves(moves, MoveType.PAWN_PUSH, pushedPawns & emptySquares & ~RANK_1, 0, -1);

        var doublePushedPawns = pawns >> DOUBLE_PUSH_OFFSET;
        var skippedEmptySquares = emptySquares >> PUSH_OFFSET;
        addMoves(moves, MoveType.DOUBLE_PUSH, doublePushedPawns & emptySquares & skippedEmptySquares & RANK_5, 0, -2);

        var leftCapturePawns = pawns >> RIGHT_CAPTURE_OFFSET;
        addMoves(moves, MoveType.PAWN_CAPTURE, leftCapturePawns & capturablePieces & ~RANK_1 & ~FILE_H, -1, -1);

        var rightCapturePawns = pawns >> LEFT_CAPTURE_OFFSET;
        addMoves(moves, MoveType.PAWN_CAPTURE, rightCapturePawns & capturablePieces & ~RANK_1 & ~FILE_A, 1, -1);

        var enPassantTarget = position.enPassantTarget();
        if (enPassantTarget != null) {
            var enPassantBitboard = singleBit(enPassantTarget);

            addEnPassantMove(moves, leftCapturePawns & enPassantBitboard & ~FILE_H, -1, -1);
            addEnPassantMove(moves, rightCapturePawns & enPassantBitboard & ~FILE_A, 1, -1);
        }

        addPromotionMoves(moves, MoveType.PAWN_PUSH, pushedPawns & emptySquares & RANK_1, 0, -1);
        addPromotionMoves(moves, MoveType.PAWN_CAPTURE, leftCapturePawns & capturablePieces & RANK_1 & ~FILE_H, -1, -1);
        addPromotionMoves(moves, MoveType.PAWN_CAPTURE, rightCapturePawns & capturablePieces & RANK_1 & ~FILE_A, 1, -1);

        return moves;
    }

    private void addMoves(List<Move> moves, MoveType type, long possibleMoves, int fileDelta, int rankDelta) {
        if (possibleMoves == 0) {
            return;
        }

        var destination = firstBit(possibleMoves);

        do {
            var fileIndex = AlgebraicConverter.fileIndex(destination);
            var rankIndex = AlgebraicConverter.rankIndex(destination);
            var source = AlgebraicConverter.toSquare(fileIndex - fileDelta, rankIndex - rankDelta);

            moves.add(new Move(type, source, destination));

            possibleMoves = clear(possibleMoves, destination);
            destination = firstBit(possibleMoves);
        } while (isSet(possibleMoves, destination));
    }

    private void addEnPassantMove(List<Move> moves, long possibleMoves, int fileDelta, int rankDelta) {
        if (possibleMoves == 0) {
            return;
        }

        var destination = firstBit(possibleMoves);

        var fileIndex = AlgebraicConverter.fileIndex(destination);
        var rankIndex = AlgebraicConverter.rankIndex(destination);
        var source = AlgebraicConverter.toSquare(fileIndex - fileDelta, rankIndex - rankDelta);

        moves.add(new Move(MoveType.EN_PASSANT, source, destination));
    }

    private void addPromotionMoves(List<Move> moves, MoveType type, long possibleMoves, int fileDelta, int rankDelta) {
        if (possibleMoves == 0) {
            return;
        }

        var destination = firstBit(possibleMoves);

        do {
            var fileIndex = AlgebraicConverter.fileIndex(destination);
            var rankIndex = AlgebraicConverter.rankIndex(destination);
            var source = AlgebraicConverter.toSquare(fileIndex - fileDelta, rankIndex - rankDelta);

            for (var choice : PromotionChoice.values()) {
                moves.add(new Move(type, source, destination, choice));
            }

            possibleMoves = clear(possibleMoves, destination);
            destination = firstBit(possibleMoves);
        } while (isSet(possibleMoves, destination));
    }
}
