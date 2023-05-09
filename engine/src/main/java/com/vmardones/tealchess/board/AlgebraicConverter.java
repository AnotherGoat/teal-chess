/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import java.util.regex.Pattern;

final class AlgebraicConverter {

    private static final String FILES = "abcdefgh";
    static final int SIDE_LENGTH = FILES.length();
    static final int NUMBER_OF_SQUARES = SIDE_LENGTH * SIDE_LENGTH;
    private static final Pattern ALGEBRAIC_PATTERN = Pattern.compile("^[a-h][1-8]$");

    static int toIndex(String algebraicNotation) {
        if (!ALGEBRAIC_PATTERN.matcher(algebraicNotation).matches()) {
            throw new AlgebraicNotationException("Invalid algebraic notation: " + algebraicNotation);
        }

        return calculateIndex(algebraicNotation);
    }

    static String toAlgebraic(int index) {

        var fileIndex = index % SIDE_LENGTH;
        var fileChar = FILES.charAt(fileIndex);
        var rank = SIDE_LENGTH - index / SIDE_LENGTH;

        return String.valueOf(fileChar) + rank;
    }

    static String fileByIndex(int index) {
        try {
            return String.valueOf(FILES.charAt(index));
        } catch (IndexOutOfBoundsException e) {
            throw new AlgebraicNotationException("File index out of bounds: " + index);
        }
    }

    private AlgebraicConverter() {}

    private static int calculateIndex(String algebraicNotation) {

        var fileIndex = algebraicNotation.charAt(0) - 'a';
        var rank = Character.digit(algebraicNotation.charAt(1), 10);
        var rankValue = SIDE_LENGTH * (SIDE_LENGTH - rank);

        return fileIndex + rankValue;
    }
}
