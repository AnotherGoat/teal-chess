/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import static com.vmardones.tealchess.board.BitboardManipulator.*;
import static com.vmardones.tealchess.move.Move.*;
import static com.vmardones.tealchess.move.MoveType.*;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.PieceType;
import com.vmardones.tealchess.piece.PromotionChoice;
import com.vmardones.tealchess.position.CastlingRights;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.square.Square;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The class responsible for making moves and building a new post-move position.
 * @see <a href="https://www.chessprogramming.org/Make_Move">Make Move</a>
 */
public final class MoveMaker {

    private static final int WHITE_KING_SIDE_ROOK = Square.h1;
    private static final int WHITE_QUEEN_SIDE_ROOK = Square.a1;
    private static final int BLACK_KING_SIDE_ROOK = Square.h8;
    private static final int BLACK_QUEEN_SIDE_ROOK = Square.a8;
    private static final int PAWN_PUSH_OFFSET = 8;

    public MoveMaker() {}

    /**
     * When a move is made, a new chess position is created, due to the position being immutable.
     *
     * @param position The position before the move.
     * @param move The move to make.
     * @return The new position, after the move is made.
     */
    public Position make(Position position, Move move) {

        var board = position.board();
        var piece = board.pieceAt(move.source());

        if (piece == null) {
            throw new AssertionError();
        }

        var sideToMove = position.sideToMove();

        var updatedBitboards = new long[PieceType.values().length][Color.values().length];

        for (var pieceType : PieceType.values()) {
            for (var pieceColor : Color.values()) {
                updatedBitboards[pieceType.ordinal()][pieceColor.ordinal()] =
                        updateBitboard(board, pieceType, pieceColor, sideToMove, move);
            }
        }

        var updatedBoard = Board.fromBitboards(updatedBitboards);

        var castlingRights = updateCastlingRights(position.castlingRights(), sideToMove, piece);
        var enPassantTarget = updateEnPassantTarget(move, sideToMove);

        var halfmoveClock = position.halfmoveClock();

        if (move.type() == MoveType.CAPTURE || piece.isPawn()) {
            halfmoveClock = 0;
        } else {
            halfmoveClock++;
        }

        var fullmoveCounter = position.fullmoveCounter();

        if (position.sideToMove().isBlack()) {
            fullmoveCounter++;
        }

        return new Position(
                updatedBoard, sideToMove.opposite(), castlingRights, enPassantTarget, halfmoveClock, fullmoveCounter);
    }

    private long updateBitboard(Board board, PieceType pieceType, Color pieceColor, Color sideToMove, Move move) {

        var bitboard = board.bitboard(pieceType, pieceColor);
        var source = move.source();
        var destination = move.destination();
        var promotionChoice = move.promotionChoice();

        if (promotionChoice == null
                && move.type() != EN_PASSANT
                && move.type() != KING_CASTLE
                && move.type() != QUEEN_CASTLE) {
            return makeRegularMove(bitboard, source, destination);
        }

        if (promotionChoice != null) {
            return makePromotionMove(bitboard, source, destination, pieceType, pieceColor, sideToMove, promotionChoice);
        }

        if (move.type() == EN_PASSANT) {
            return makeEnPassantMove(bitboard, source, destination, sideToMove);
        }

        if (move.type() == KING_CASTLE) {
            return makeKingSideCastle(bitboard, source, destination, sideToMove);
        }

        return makeQueenSideCastle(bitboard, source, destination, sideToMove);
    }

    // TODO: Maybe also check if destination is set
    private long makeRegularMove(long bitboard, int source, int destination) {
        if (isSet(bitboard, source)) {
            bitboard = clear(bitboard, source);
            return set(bitboard, destination);
        }

        return clear(bitboard, destination);
    }

    private long makePromotionMove(
            long bitboard,
            int source,
            int destination,
            PieceType pieceType,
            Color pieceColor,
            Color sideToMove,
            PromotionChoice choice) {
        if (isSet(bitboard, source)) {
            return clear(bitboard, source);
        }

        if (pieceType != choice.type() && pieceColor != sideToMove) {
            return clear(bitboard, destination);
        }

        return set(bitboard, destination);
    }

    private long makeEnPassantMove(long bitboard, int source, int destination, Color sideToMove) {
        if (isSet(bitboard, source)) {
            bitboard = clear(bitboard, source);
            return set(bitboard, destination);
        }

        var enPassantPawn = sideToMove.isWhite() ? destination - PAWN_PUSH_OFFSET : destination + PAWN_PUSH_OFFSET;
        if (isSet(bitboard, enPassantPawn)) {
            return clear(bitboard, enPassantPawn);
        }

        return bitboard;
    }

    private long makeKingSideCastle(long bitboard, int source, int destination, Color sideToMove) {
        var rookMove = sideToMove.isWhite() ? WHITE_KING_SIDE_CASTLE.get(1) : BLACK_KING_SIDE_CASTLE.get(1);
        return makeCastle(bitboard, source, destination, rookMove);
    }

    private long makeQueenSideCastle(long bitboard, int source, int destination, Color sideToMove) {
        var rookMove = sideToMove.isWhite() ? WHITE_QUEEN_SIDE_CASTLE.get(1) : BLACK_QUEEN_SIDE_CASTLE.get(1);
        return makeCastle(bitboard, source, destination, rookMove);
    }

    private long makeCastle(long bitboard, int source, int destination, Move rookMove) {
        if (isSet(bitboard, source)) {
            bitboard = clear(bitboard, source);
            return set(bitboard, destination);
        }

        var rookSource = rookMove.source();
        var rookDestination = rookMove.destination();

        if (isSet(bitboard, rookSource)) {
            bitboard = clear(bitboard, source);
            return set(bitboard, rookDestination);
        }

        return bitboard;
    }

    private CastlingRights updateCastlingRights(CastlingRights castlingRights, Color sideToMove, Piece piece) {
        if (!piece.isKing() && !piece.isRook()) {
            return castlingRights;
        }

        if (piece.isKing()) {
            return castlingRights.disable(sideToMove);
        }

        var square = piece.square();

        if (square == WHITE_KING_SIDE_ROOK || square == BLACK_KING_SIDE_ROOK) {
            return castlingRights.disableKingSide(sideToMove);
        }

        if (square == WHITE_QUEEN_SIDE_ROOK || square == BLACK_QUEEN_SIDE_ROOK) {
            return castlingRights.disableQueenSide(sideToMove);
        }

        return castlingRights;
    }

    private @Nullable Integer updateEnPassantTarget(Move move, Color sideToMove) {
        if (move.type() != MoveType.DOUBLE_PUSH) {
            return null;
        }

        return sideToMove.isWhite() ? move.destination() + PAWN_PUSH_OFFSET : move.destination() - PAWN_PUSH_OFFSET;
    }
}
