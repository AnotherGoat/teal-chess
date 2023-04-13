/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.parser;

import java.util.regex.Pattern;

import cl.vmardones.chess.ExcludeFromGeneratedReport;
import cl.vmardones.chess.engine.board.AlgebraicNotationException;
import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.player.Alliance;
import cl.vmardones.chess.engine.player.UnknownSymbolException;
import org.eclipse.jdt.annotation.Nullable;

/**
 * FEN (Forsyth-Edwards Notation) string parser.
 */
public final class FenParser {

    private static final Pattern CASTLING_PATTERN = Pattern.compile("-|K?Q?k?q?");

    public static Board parse(String fen) {

        if (!isPrintableAscii(fen)) {
            throw new FenParseException("FEN string is not ASCII or contains ASCII control characters: " + fen);
        }

        var parts = fen.split(" ");

        if (parts.length != 6) {
            throw new FenParseException("FEN string doesn't have exactly 6 data fields: " + fen);
        }

        var piecePlacement = parts[0];
        var activeColor = parseActiveColor(parts[1]);
        var castles = parseCastles(parts[2]);
        var pawn = parseEnPassantTarget(parts[3], activeColor);
        var halfmove = parseHalfmove(parts[4]);
        int fullmove = parseFullmove(parts[5]);

        return null;
    }

    @ExcludeFromGeneratedReport
    private FenParser() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }

    private static boolean isPrintableAscii(String text) {
        return text.chars().allMatch(character -> character >= 0x20 && character < 0x7F);
    }

    // TODO: Replace nextTurnMaker with activeColor everywhere
    // TODO: Replace Alliance with Color everywhere
    private static Alliance parseActiveColor(String data) {
        try {
            return Alliance.fromSymbol(data);
        } catch (UnknownSymbolException e) {
            throw new FenParseException("Illegal alliance symbol: " + data);
        }
    }

    private static String parseCastles(String data) {
        if (!CASTLING_PATTERN.matcher(data).matches()) {
            throw new FenParseException("Castling availability is incorrect: " + data);
        }

        return data;
    }

    private static @Nullable Pawn parseEnPassantTarget(String data, Alliance activeColor) {
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
}
