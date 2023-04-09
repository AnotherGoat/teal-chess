/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import cl.vmardones.chess.ExcludeFromGeneratedReport;
import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.piece.Pawn;

public final class MoveMaker {

    /**
     * When a move is made, a new board is created, due to the board class being immutable.
     *
     * @param board The current board.
     * @param move The move to make.
     * @return The new board, after the move is made.
     */
    public static Board make(Board board, Move move) {
        var builder = board.nextTurnBuilder();
        var piece = move.piece();
        var otherPiece = move.otherPiece();
        var destination = move.destination().toString();
        var movedPiece = piece.moveTo(destination);

        builder.without(piece).without(otherPiece).with(movedPiece);

        if (move.type() == MoveType.PAWN_JUMP) {
            builder.enPassantPawn((Pawn) movedPiece);
        }

        var rookDestination = move.rookDestination();

        if (otherPiece != null && rookDestination != null) {
            builder.with(otherPiece.moveTo(rookDestination.toString()));
        }

        return builder.build();
    }

    @ExcludeFromGeneratedReport
    private MoveMaker() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
