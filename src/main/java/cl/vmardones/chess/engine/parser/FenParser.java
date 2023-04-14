/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import cl.vmardones.chess.ExcludeFromGeneratedReport;
import cl.vmardones.chess.engine.board.AlgebraicNotationException;
import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.piece.*;
import cl.vmardones.chess.engine.player.Color;
import cl.vmardones.chess.engine.player.ColorSymbolException;
import org.eclipse.jdt.annotation.Nullable;

/**
 * FEN (Forsyth-Edwards Notation) string parser.
 */
public final class FenParser {

    private static final Pattern PIECES_PATTERN = Pattern.compile("[/PNBRQKpnbrqk1-8]*");
    private static final Pattern CASTLING_PATTERN = Pattern.compile("-|K?Q?k?q?");
    private static final int DATA_FIELDS_LENGTH = 6;
    private static final String FILES = "abcdefgh";

    public static Board parse(String fen) {

        if (!isPrintableAscii(fen)) {
            throw new FenParseException("FEN string is not ASCII or contains ASCII control characters: " + fen);
        }

        var parts = fen.split(" ");

        if (parts.length != DATA_FIELDS_LENGTH) {
            throw new FenParseException("FEN string doesn't have exactly 6 data fields: " + fen);
        }

        var ranks = parseRanks(parts[0]);
        var activeColor = parseActiveColor(parts[1]);
        var castles = parseCastles(parts[2]);
        var enPassantPawn = parseEnPassantTarget(parts[3], activeColor);
        var halfmove = parseHalfmove(parts[4]);
        int fullmove = parseFullmove(parts[5]);

        return buildBoard(ranks, activeColor, castles, enPassantPawn, halfmove, fullmove);
    }

    @ExcludeFromGeneratedReport
    private FenParser() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }

    private static boolean isPrintableAscii(String text) {
        return text.chars().allMatch(character -> character >= 0x20 && character < 0x7F);
    }

    private static List<String> parseRanks(String data) {
        if (!PIECES_PATTERN.matcher(data).matches()) {
            throw new FenParseException("Piece placement data contains invalid characters: " + data);
        }

        if (!data.contains("K") || !data.contains("k")) {
            throw new FenParseException("At least one of the kings is missing: " + data);
        }

        if (containsMultiple(data, 'K') || containsMultiple(data, 'k')) {
            throw new FenParseException("The board has more than 2 kings: " + data);
        }

        var ranks = data.split("/");

        if (ranks.length != Board.SIDE_LENGTH) {
            throw new FenParseException("Piece placement data doesn't have exactly 8 ranks: " + data);
        }

        return Arrays.asList(ranks);
    }

    private static boolean containsMultiple(String text, char character) {
        var index = text.indexOf(character);

        while (index >= 0) {
            if (text.indexOf(character, index + 1) > 0) {
                return true;
            }

            index = text.indexOf(character, index + 1);
        }

        return false;
    }

    private static Color parseActiveColor(String data) {
        try {
            return Color.fromSymbol(data);
        } catch (ColorSymbolException e) {
            throw new FenParseException("Illegal color symbol: " + data);
        }
    }

    private static String parseCastles(String data) {
        if (!CASTLING_PATTERN.matcher(data).matches()) {
            throw new FenParseException("Castling availability is incorrect: " + data);
        }

        return data;
    }

    private static @Nullable Pawn parseEnPassantTarget(String data, Color activeColor) {
        if (data.equals("-")) {
            return null;
        }

        try {
            return new Pawn(data, activeColor.opposite());
        } catch (AlgebraicNotationException e) {
            throw new FenParseException("En passant target is not a valid position: " + data);
        }
    }

    private static int parseHalfmove(String data) {
        int halfmove;

        try {
            halfmove = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            throw new FenParseException("Halfmove clock is not an integer: " + data);
        }

        if (halfmove < 0) {
            throw new FenParseException("Halfmove clock cannot be negative: " + halfmove);
        }

        return halfmove;
    }

    private static int parseFullmove(String data) {
        int fullmove;

        try {
            fullmove = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            throw new FenParseException("Fullmove number is not an integer: " + data);
        }

        if (fullmove < 0) {
            throw new FenParseException("Fullmove number cannot be negative: " + fullmove);
        }

        return fullmove;
    }

    // TODO: The data for active color, castles, halfmove and fullmove isn't used yet
    // TODO: A similar method will probably be used to build a turn instead of a board
    private static Board buildBoard(
            List<String> ranks, Color activeColor, String castles, Pawn enPassantPawn, int halfmove, int fullmove) {

        var pieces = generatePieces(ranks);
        var whiteKing = findKing(pieces, Color.WHITE);
        var blackKing = findKing(pieces, Color.BLACK);

        var builder = Board.builder(whiteKing, blackKing);

        builder.withAll(pieces);
        builder.enPassantPawn(enPassantPawn);

        return builder.build();
    }

    private static List<Piece> generatePieces(List<String> ranks) {

        List<Piece> pieces = new ArrayList<>();

        for (int i = 0; i < ranks.size(); i++) {
            var rank = ranks.get(i);
            var fileCounter = 0;

            for (int j = 0; j < rank.length(); j++) {
                var data = rank.charAt(j);

                if (Character.isDigit(data)) {
                    fileCounter += Character.getNumericValue(data);
                } else {
                    var file = String.valueOf(FILES.charAt(fileCounter));
                    var rankIndex = Board.SIDE_LENGTH - i;
                    var position = file + rankIndex;

                    pieces.add(Piece.fromSymbol(String.valueOf(data), position));
                    fileCounter++;
                }
            }
        }

        return Collections.unmodifiableList(pieces);
    }

    private static King findKing(List<Piece> pieces, Color color) {
        return pieces.stream()
                .filter(piece -> piece instanceof King && piece.color() == color)
                .map(King.class::cast)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Unreachable statement"));
    }
}
