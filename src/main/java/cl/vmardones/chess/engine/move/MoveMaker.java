/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Color;
import org.eclipse.jdt.annotation.Nullable;

/** The class responsible for realizing moves and building a new post-move board. */
public final class MoveMaker {

    /**
     * When a move is made, a new board is created, due to the board class being immutable.
     *
     * @param board The current board.
     * @param move The move to make.
     * @return The new board, after the move is made.
     */
    public Board make(Board board, Move move) {
        var piece = move.piece();
        var otherPiece = move.otherPiece();
        var destination = move.destination().toString();
        var movedPiece = piece.moveTo(destination);

        var builder = configureNextTurn(board, movedPiece);

        builder.without(piece).without(otherPiece).with(movedPiece);

        if (move.type() == MoveType.PAWN_JUMP) {
            builder.enPassantPawn((Pawn) movedPiece);
        }

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
    private Board.BoardBuilder configureNextTurn(Board board, @Nullable Piece movedPiece) {
        if (!movedPiece.isKing()) {
            return board.nextTurnBuilder();
        }

        if (movedPiece.color() == Color.WHITE) {
            return board.nextTurnBuilder((King) movedPiece, board.king(Color.BLACK));
        }

        return board.nextTurnBuilder(board.king(Color.WHITE), (King) movedPiece);
    }
}
