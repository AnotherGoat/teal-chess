/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser.fen;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.piece.*;
import com.vmardones.tealchess.position.Position;
import org.eclipse.jdt.annotation.Nullable;

/**
 * FEN (Forsyth-Edwards Notation) parser.
 * @see <a href="https://www.chessprogramming.org/Forsyth-Edwards_Notation">Forsyth-Edwards Notation</a>
 */
public final class FenParser {

    private static final Pattern PIECES_PATTERN = Pattern.compile("[/PNBRQKpnbrqk1-8]*");
    private static final Pattern CASTLING_PATTERN = Pattern.compile("-|K?Q?k?q?");
    private static final Pattern EN_PASSANT_PATTERN = Pattern.compile("-|[a-h][36]");
    private static final int DATA_FIELDS_LENGTH = 6;

    public static Position parse(String fen) {
        var castles = parseCastles(parts[2]);
        var enPassantTarget = parseEnPassantTarget(parts[3]);

        return buildPosition(castles, enPassantTarget);
    }

    private static Position buildPosition(
            CastlingRights castlingRights,
            @Nullable Coordinate enPassantTarget) {

        return new Position(board, sideToMove, halfmoveClock, fullmoveCounter);
    }

    private static Board buildBoard(List<String> ranks) {
        var pieces = generatePieces(ranks);

        var whiteKing = findKing(pieces, Color.WHITE);
        var blackKing = findKing(pieces, Color.BLACK);

        return Board.builder(whiteKing, blackKing).withAll(pieces).build();
    }

    private static List<Piece> generatePieces(List<String> ranks) {

        List<Piece> pieces = new ArrayList<>();

        for (int i = 0; i < ranks.size(); i++) {
            var rank = ranks.get(i);
            var fileCounter = 0;

            for (int j = 0; j < rank.length(); j++) {
                var symbol = rank.charAt(j);

                if (Character.isDigit(symbol)) {
                    fileCounter += Character.digit(symbol, 10);
                } else {
                    var file = Coordinate.fileByIndex(fileCounter);
                    var rankIndex = Board.SIDE_LENGTH - i;
                    var coordinate = Coordinate.of(file + rankIndex);

                    pieces.add(Piece.fromSymbol(String.valueOf(symbol), coordinate));
                    fileCounter++;
                }
            }
        }

        return unmodifiableList(pieces);
    }

    private static Piece findKing(List<Piece> pieces, Color color) {
        return pieces.stream()
                .filter(piece -> piece.isKing() && piece.color() == color)
                .findFirst()
                .orElseThrow(AssertionError::new);
    }
}
