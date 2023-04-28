/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser;

import com.vmardones.tealchess.ExcludeFromGeneratedReport;
import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;

public final class FenSerializer {

    public static String serialize(Position position) {

        var ranks = serializeBoard(position.board());
        var sideToMove = position.sideToMove().fen();
        var castles = position.castlingRights().fen();
        var enPassantTarget = serializeEnPassantTarget(position.enPassantTarget(), position.sideToMove());
        var halfmove = String.valueOf(position.halfmoveClock());
        var fullmove = String.valueOf(position.fullmoveCounter());

        return String.join(" ", ranks, sideToMove, castles, enPassantTarget, halfmove, fullmove);
    }

    @ExcludeFromGeneratedReport
    private FenSerializer() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }

    private static String serializeBoard(Board board) {

        var result = new StringBuilder();

        var squares = board.squares();
        var emptyCounter = 0;

        for (int i = 0; i < squares.size(); i++) {
            var piece = squares.get(i).piece();

            if (piece == null) {
                emptyCounter++;
            } else {
                if (emptyCounter != 0) {
                    result.append(emptyCounter);
                    emptyCounter = 0;
                }

                result.append(piece.fen());
            }

            var squareCounter = i + 1;

            if (squareCounter % Board.SIDE_LENGTH == 0) {
                if (emptyCounter != 0) {
                    result.append(emptyCounter);
                    emptyCounter = 0;
                }

                if (squareCounter < squares.size()) {
                    result.append("/");
                }
            }
        }

        return result.toString();
    }

    private static String serializeEnPassantTarget(@Nullable Pawn pawn, Color sideToMove) {
        if (pawn == null) {
            return "-";
        }

        var coordinate = pawn.coordinate().up(sideToMove.direction());

        if (coordinate == null) {
            throw new AssertionError("Unreachable statement");
        }

        return coordinate.san();
    }
}
