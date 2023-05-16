/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;

import java.util.List;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.piece.PromotionChoice;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.square.AlgebraicConverter;

final class PawnMoveGenerator extends MoveGenerator {

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

    private final Color sideToMove;
    private final long emptySquares;
    private final long pawns;
    private final long capturablePieces;
    private final long enPassantBitboard;
    private final long promotionRank;
    private final long doublePushRank;

    PawnMoveGenerator(Position position) {
        super(position);
        var board = position.board();
        sideToMove = position.sideToMove();
        emptySquares = board.emptySquares();
        pawns = board.pawns(sideToMove);
        capturablePieces = board.capturablePieces(sideToMove);

        var enPassantTarget = position.enPassantTarget();
        enPassantBitboard = enPassantTarget == null ? 0 : singleBit(enPassantTarget);

        promotionRank = sideToMove.isWhite() ? RANK_8 : RANK_1;
        doublePushRank = sideToMove.isWhite() ? RANK_4 : RANK_5;
    }

    @Override
    public List<Move> generate() {
        if (pawns == 0) {
            return moves;
        }

        addPushes();
        addDoublePushes();
        addLeftCaptures();
        addRightCaptures();
        addLeftEnPassantCaptures();
        addRightEnPassantCaptures();
        addPushPromotions();
        addLeftCapturePromotions();
        addRightCapturePromotions();

        return moves;
    }

    private long movePawns(int offset) {
        return sideToMove.isWhite() ? pawns << offset : pawns >> offset;
    }

    private long movePawns(int whiteOffset, int blackOffset) {
        return sideToMove.isWhite() ? pawns << whiteOffset : pawns >> blackOffset;
    }

    private void addPushes() {
        var movedPawns = movePawns(PUSH_OFFSET);
        var possibleMoves = movedPawns & emptySquares & ~promotionRank;
        var rankDelta = sideToMove.isWhite() ? -1 : 1;

        addMoves(MoveType.PAWN_PUSH, possibleMoves, 0, rankDelta);
    }

    private void addDoublePushes() {
        var movedPawns = movePawns(DOUBLE_PUSH_OFFSET);
        var forwardEmptySquares = sideToMove.isWhite() ? emptySquares << PUSH_OFFSET : emptySquares >> PUSH_OFFSET;
        var possibleMoves = movedPawns & emptySquares & forwardEmptySquares & doublePushRank;
        var rankDelta = sideToMove.isWhite() ? -2 : 2;

        addMoves(MoveType.DOUBLE_PUSH, possibleMoves, 0, rankDelta);
    }

    private void addLeftCaptures() {
        var movedPawns = movePawns(LEFT_CAPTURE_OFFSET, RIGHT_CAPTURE_OFFSET);
        var possibleMoves = movedPawns & capturablePieces & ~promotionRank & ~FILE_H;
        var rankDelta = sideToMove.isWhite() ? -1 : 1;

        addMoves(MoveType.PAWN_CAPTURE, possibleMoves, 1, rankDelta);
    }

    private void addRightCaptures() {
        var movedPawns = movePawns(RIGHT_CAPTURE_OFFSET, LEFT_CAPTURE_OFFSET);
        var possibleMoves = movedPawns & capturablePieces & ~promotionRank & ~FILE_A;
        var rankDelta = sideToMove.isWhite() ? -1 : 1;

        addMoves(MoveType.PAWN_CAPTURE, possibleMoves, -1, rankDelta);
    }

    private void addLeftEnPassantCaptures() {
        if (enPassantBitboard == 0L) {
            return;
        }

        var movedPawns = movePawns(LEFT_CAPTURE_OFFSET, RIGHT_CAPTURE_OFFSET);
        var possibleMoves = movedPawns & enPassantBitboard & ~FILE_H;
        var rankDelta = sideToMove.isWhite() ? -1 : 1;

        addMoves(MoveType.EN_PASSANT, possibleMoves, 1, rankDelta);
    }

    private void addRightEnPassantCaptures() {
        if (enPassantBitboard == 0L) {
            return;
        }

        var movedPawns = movePawns(RIGHT_CAPTURE_OFFSET, LEFT_CAPTURE_OFFSET);
        var possibleMoves = movedPawns & enPassantBitboard & ~FILE_A;
        var rankDelta = sideToMove.isWhite() ? -1 : 1;

        addMoves(MoveType.EN_PASSANT, possibleMoves, -1, rankDelta);
    }

    private void addPushPromotions() {
        var movedPawns = movePawns(PUSH_OFFSET);
        var possibleMoves = movedPawns & emptySquares & promotionRank;
        var rankDelta = sideToMove.isWhite() ? -1 : 1;

        addPromotionMoves(MoveType.PAWN_PUSH, possibleMoves, 0, rankDelta);
    }

    private void addLeftCapturePromotions() {
        var movedPawns = movePawns(LEFT_CAPTURE_OFFSET, RIGHT_CAPTURE_OFFSET);
        var possibleMoves = movedPawns & capturablePieces & promotionRank & ~FILE_H;
        var rankDelta = sideToMove.isWhite() ? -1 : 1;

        addPromotionMoves(MoveType.PAWN_CAPTURE, possibleMoves, 1, rankDelta);
    }

    private void addRightCapturePromotions() {
        var movedPawns = movePawns(RIGHT_CAPTURE_OFFSET, LEFT_CAPTURE_OFFSET);
        var possibleMoves = movedPawns & capturablePieces & promotionRank & ~FILE_A;
        var rankDelta = sideToMove.isWhite() ? -1 : 1;

        addPromotionMoves(MoveType.PAWN_CAPTURE, possibleMoves, -1, rankDelta);
    }

    private void addPromotionMoves(MoveType moveType, long possibleMoves, int fileDelta, int rankDelta) {
        if (possibleMoves == 0) {
            return;
        }

        var destination = firstBit(possibleMoves);

        do {
            var fileIndex = AlgebraicConverter.fileIndex(destination);
            var rankIndex = AlgebraicConverter.rankIndex(destination);
            var source = AlgebraicConverter.toSquare(fileIndex + fileDelta, rankIndex + rankDelta);

            for (var choice : PromotionChoice.values()) {
                moves.add(new Move(moveType, source, destination, choice));
            }

            possibleMoves = clear(possibleMoves, destination);
            destination = firstBit(possibleMoves);
        } while (isSet(possibleMoves, destination));
    }
}
