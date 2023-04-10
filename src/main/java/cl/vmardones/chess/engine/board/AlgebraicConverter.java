/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import java.util.regex.Pattern;

import cl.vmardones.chess.ExcludeFromGeneratedReport;

final class AlgebraicConverter {

    private static final Pattern ALGEBRAIC_PATTERN = Pattern.compile("^[a-h][1-8]$");
    private static final String FILES = "abcdefgh";

    static int toIndex(String algebraicNotation) {
        if (!ALGEBRAIC_PATTERN.matcher(algebraicNotation).matches()) {
            throw new BadAlgebraicNotationException(algebraicNotation);
        }

        return calculateIndex(algebraicNotation);
    }

    static String toAlgebraic(int index) {

        var fileIndex = index % Board.SIDE_LENGTH;
        var fileChar = FILES.charAt(fileIndex);
        var rank = Board.SIDE_LENGTH - index / Board.SIDE_LENGTH;

        return String.valueOf(fileChar) + rank;
    }

    @ExcludeFromGeneratedReport
    private AlgebraicConverter() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }

    private static int calculateIndex(String algebraicNotation) {

        var fileIndex = algebraicNotation.charAt(0) - 'a';
        var rank = Character.getNumericValue(algebraicNotation.charAt(1));
        var rankValue = Board.SIDE_LENGTH * (Board.SIDE_LENGTH - rank);

        return fileIndex + rankValue;
    }
}
