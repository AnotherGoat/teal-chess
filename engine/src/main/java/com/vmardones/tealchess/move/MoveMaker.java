/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.game.CastlingRights;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.piece.King;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The class responsible for realizing moves and building a new post-move position.
 * @see <a href="https://www.chessprogramming.org/Make_Move">Make Move</a>
 */
public final class MoveMaker {

    private static final Coordinate WHITE_KING_SIDE = Coordinate.of("h1");
    private static final Coordinate WHITE_QUEEN_SIDE = Coordinate.of("a1");
    private static final Coordinate BLACK_KING_SIDE = Coordinate.of("h8");
    private static final Coordinate BLACK_QUEEN_SIDE = Coordinate.of("a8");

    /**
     * When a move is made, a new chess position is created, due to the position being immutable.
     *
     * @param position The position before the move.
     * @param legalMove The move to make.
     * @return The new position, after the move is made.
     */
    public Position make(Position position, LegalMove legalMove) {
        return make(position, legalMove.move());
    }

    /**
     * When a move is made, a new chess position is created, due to the position being immutable.
     *
     * @param position The position before the move.
     * @param move The move to make.
     * @return The new position, after the move is made.
     */
    public Position make(Position position, Move move) {

        var board = createBoard(position, move);
        var sideToMove = position.sideToMove().opposite();
        var castlingRights = updateCastlingRights(position, move);
        var enPassantTarget = updateEnPassantTarget(move, position.sideToMove());
        var halfmoveClock = updateHalfmoveClock(position, move);
        var fullmoveCounter = updateFullmoveCounter(position);

        return new Position(board, sideToMove, castlingRights, enPassantTarget, halfmoveClock, fullmoveCounter);
    }

    private Board createBoard(Position position, Move move) {

        var piece = move.piece();
        var otherPiece = move.otherPiece();
        var destination = move.destination();
        var movedPiece = piece.moveTo(destination);

        var builder = configureBuilder(position.board(), movedPiece);

        builder.without(piece).without(otherPiece).with(movedPiece);

        var rookDestination = move.rookDestination();

        if (otherPiece != null && rookDestination != null) {
            builder.with(otherPiece.moveTo(rookDestination));
        }

        var promotionChoice = move.promotionChoice();

        if (promotionChoice != null) {
            var pawn = (Pawn) movedPiece;
            builder.with(pawn.promote(promotionChoice));
        }

        return builder.build();
    }

    // TODO: Check if this code can be cleaned somehow
    private Board.BoardBuilder configureBuilder(Board board, Piece movedPiece) {
        if (!movedPiece.isKing()) {
            return board.nextPositionBuilder();
        }

        if (movedPiece.color().isWhite()) {
            return board.nextPositionBuilder((King) movedPiece, board.king(Color.BLACK));
        }

        return board.nextPositionBuilder(board.king(Color.WHITE), (King) movedPiece);
    }

    private @Nullable Square updateEnPassantTarget(Move move, Color sideToMove) {
        if (move.type() != MoveType.DOUBLE_PUSH) {
            return null;
        }

        var enPassantTarget = move.destination().down(sideToMove.direction());

        return Square.create(enPassantTarget, null);
    }

    private CastlingRights updateCastlingRights(Position position, Move move) {
        var castlingRights = position.castlingRights();
        var piece = move.piece();

        if (!piece.isKing() && !piece.isRook()) {
            return castlingRights;
        }

        var sideToMove = position.sideToMove();

        if (piece.isKing()) {
            return castlingRights.disable(sideToMove);
        }

        var coordinate = piece.coordinate();

        if (coordinate.equals(WHITE_KING_SIDE) || coordinate.equals(BLACK_KING_SIDE)) {
            return castlingRights.disableKingSide(sideToMove);
        }

        if (coordinate.equals(WHITE_QUEEN_SIDE) || coordinate.equals(BLACK_QUEEN_SIDE)) {
            return castlingRights.disableQueenSide(sideToMove);
        }

        return castlingRights;
    }

    private int updateHalfmoveClock(Position position, Move move) {

        if (move.type() == MoveType.CAPTURE || move.piece().isPawn()) {
            return 0;
        }

        return position.halfmoveClock() + 1;
    }

    private int updateFullmoveCounter(Position position) {
        if (position.sideToMove().isBlack()) {
            return position.fullmoveCounter() + 1;
        }

        return position.fullmoveCounter();
    }
}
