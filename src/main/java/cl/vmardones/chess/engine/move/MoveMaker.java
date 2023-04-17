/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.game.CastlingRights;
import cl.vmardones.chess.engine.game.Position;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.piece.Rook;
import cl.vmardones.chess.engine.player.Color;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The class responsible for realizing moves and building a new post-move position.
 * @see <a href="https://www.chessprogramming.org/Make_Move">Make Move</a>
 */
public final class MoveMaker {

    /**
     * When a move is made, a new chess position is created, due to the position being immutable.
     *
     * @param position The current position.
     * @param move The move to make.
     * @return The new position, after the move is made.
     */
    public Position make(Position position, Move move) {

        var board = createBoard(position, move);
        var sideToMove = position.sideToMove().opposite();
        var castlingRights = updateCastlingRights(position, move);
        var enPassantPawn = updateEnPassantPawn(move);
        var halfmoveClock = updateHalfmoveClock(position, move);
        var fullmoveCounter = updateFullmoveCounter(position);

        return new Position(board, sideToMove, castlingRights, enPassantPawn, halfmoveClock, fullmoveCounter, move);
    }

    private Board createBoard(Position position, Move move) {

        var piece = move.piece();
        var otherPiece = move.otherPiece();
        var destination = move.destination().toString();
        var movedPiece = piece.moveTo(destination);

        var builder = configureBuilder(position.board(), movedPiece);

        builder.without(piece).without(otherPiece).with(movedPiece);

        var rookDestination = move.rookDestination();

        if (otherPiece != null && rookDestination != null) {
            builder.with(otherPiece.moveTo(rookDestination.toString()));
        }

        if (move.promotion()) {
            var pawn = (Pawn) movedPiece;
            builder.with(pawn.promote());
        }

        return builder.build();
    }

    // TODO: Check if this code can be cleaned somehow
    private Board.BoardBuilder configureBuilder(Board board, Piece movedPiece) {
        if (!movedPiece.isKing()) {
            return board.nextPositionBuilder();
        }

        if (movedPiece.color() == Color.WHITE) {
            return board.nextPositionBuilder((King) movedPiece, board.king(Color.BLACK));
        }

        return board.nextPositionBuilder(board.king(Color.WHITE), (King) movedPiece);
    }

    private @Nullable Pawn updateEnPassantPawn(Move move) {
        if (move.type() != MoveType.PAWN_PUSH) {
            return null;
        }

        var destination = move.destination().toString();

        return (Pawn) move.piece().moveTo(destination);
    }

    private CastlingRights updateCastlingRights(Position position, Move move) {
        var castlingRights = position.castlingRights();
        var piece = move.piece();

        if (!piece.isKing() || !piece.isRook()) {
            return castlingRights;
        }

        var sideToMove = position.sideToMove();

        if (piece.isKing()) {
            return castlingRights.disable(sideToMove);
        }

        var coordinate = piece.coordinate();

        if (coordinate.equals(Rook.WHITE_KING_SIDE) || coordinate.equals(Rook.BLACK_KING_SIDE)) {
            return castlingRights.disableKingSide(sideToMove);
        }

        if (coordinate.equals(Rook.WHITE_QUEEN_SIDE) || coordinate.equals(Rook.BLACK_QUEEN_SIDE)) {
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
        if (position.sideToMove() == Color.BLACK) {
            return position.fullmoveCounter() + 1;
        }

        return position.fullmoveCounter();
    }
}
