/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser.fen;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.game.Position;
import org.eclipse.jdt.annotation.Nullable;

public final class FenSerializer {

    public static String serialize(Position position) {

        var ranks = serializeBoard(position.board());
        var sideToMove = position.sideToMove().fen();
        var castles = position.castlingRights().fen();
        var enPassantTarget = serializeEnPassantTarget(position.enPassantTarget());
        var halfmove = String.valueOf(position.halfmoveClock());
        var fullmove = String.valueOf(position.fullmoveCounter());

        return String.join(" ", ranks, sideToMove, castles, enPassantTarget, halfmove, fullmove);
    }

    private FenSerializer() {}

    private static String serializeBoard(Board board) {

        var result = new StringBuilder();

        var pieces = board.mailbox().values().stream().toList();
        var emptyCounter = 0;

        for (int i = 0; i < pieces.size(); i++) {
            var piece = pieces.get(i);

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

                if (squareCounter < pieces.size()) {
                    result.append("/");
                }
            }
        }

        return result.toString();
    }

    private static String serializeEnPassantTarget(@Nullable Coordinate enPassantTarget) {
        if (enPassantTarget == null) {
            return "-";
        }

        return enPassantTarget.san();
    }
}
