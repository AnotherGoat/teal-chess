/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser.fen;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Mailbox;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.square.AlgebraicConverter;
import org.jspecify.annotations.Nullable;

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

        var pieces = new Mailbox(board).pieces();
        var emptyCounter = 0;

        for (var rank = Board.SIDE_LENGTH - 1; rank >= 0; rank--) {
            for (var file = 0; file < Board.SIDE_LENGTH; file++) {

                var square = AlgebraicConverter.toSquare(file, rank);
                var piece = pieces.get(square);

                if (piece == null) {
                    emptyCounter++;
                } else {
                    if (emptyCounter != 0) {
                        result.append(emptyCounter);
                        emptyCounter = 0;
                    }

                    result.append(piece.fen());
                }
            }

            if (emptyCounter != 0) {
                result.append(emptyCounter);
                emptyCounter = 0;
            }

            if (rank > 0) {
                result.append("/");
            }
        }

        return result.toString();
    }

    private static String serializeEnPassantTarget(@Nullable Integer enPassantTarget) {
        if (enPassantTarget == null) {
            return "-";
        }

        return AlgebraicConverter.toAlgebraic(enPassantTarget);
    }
}
