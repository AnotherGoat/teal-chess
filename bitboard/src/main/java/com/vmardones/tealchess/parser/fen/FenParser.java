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
import com.vmardones.tealchess.color.ColorSymbolException;
import com.vmardones.tealchess.parser.AlgebraicConverter;
import com.vmardones.tealchess.piece.*;
import com.vmardones.tealchess.position.Position;

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

        if (!isPrintableAscii(fen)) {
            throw new FenParseException("FEN string is not ASCII or contains ASCII control characters: " + fen);
        }

        var parts = fen.split(" ", -1);

        if (parts.length != DATA_FIELDS_LENGTH) {
            throw new FenParseException("FEN string doesn't have exactly 6 data fields: " + fen);
        }

        var ranks = parseRanks(parts[0]);
        var sideToMove = parseSideToMove(parts[1]);
        var halfmove = parseHalfmoveClock(parts[4]);
        int fullmove = parseFullmoveCounter(parts[5]);

        return buildPosition(ranks, sideToMove, halfmove, fullmove);
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

        var ranks = data.split("/", -1);

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

    private static Color parseSideToMove(String data) {
        try {
            return Color.fromSymbol(data);
        } catch (ColorSymbolException e) {
            throw new FenParseException("Illegal color symbol: " + data);
        }
    }

    private static int parseHalfmoveClock(String data) {
        int halfmoveClock;

        try {
            halfmoveClock = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            throw new FenParseException("Halfmove clock is not an integer: " + data);
        }

        if (halfmoveClock < 0) {
            throw new FenParseException("Halfmove clock cannot be negative: " + halfmoveClock);
        }

        return halfmoveClock;
    }

    private static int parseFullmoveCounter(String data) {
        int fullmoveCounter;

        try {
            fullmoveCounter = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            throw new FenParseException("Fullmove counter is not an integer: " + data);
        }

        if (fullmoveCounter < 1) {
            throw new FenParseException("Fullmove counter cannot be negative or zero: " + fullmoveCounter);
        }

        return fullmoveCounter;
    }

    private static Position buildPosition(
            List<String> ranks, Color sideToMove, int halfmoveClock, int fullmoveCounter) {

        var board = buildBoard(ranks);
        return new Position(board, sideToMove, halfmoveClock, fullmoveCounter);
    }

    private static Board buildBoard(List<String> ranks) {
        var pieces = generatePieces(ranks);

        var whiteKingCoordinate = findKing(pieces, Color.WHITE);
        var blackKingCoordinate = findKing(pieces, Color.BLACK);

        return Board.builder(whiteKingCoordinate, blackKingCoordinate)
                .withAll(pieces)
                .build();
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
                    var rankIndex = Board.SIDE_LENGTH - i;
                    var coordinate = AlgebraicConverter.toCoordinate(fileCounter, rankIndex);

                    pieces.add(Piece.fromSymbol(String.valueOf(symbol), coordinate));
                    fileCounter++;
                }
            }
        }

        return unmodifiableList(pieces);
    }

    private static int findKing(List<Piece> pieces, Color color) {
        return pieces.stream()
                .filter(piece -> piece.isKing() && piece.color() == color)
                .mapToInt(Piece::coordinate)
                .findFirst()
                .orElseThrow(AssertionError::new);
    }

    private FenParser() {}
}
