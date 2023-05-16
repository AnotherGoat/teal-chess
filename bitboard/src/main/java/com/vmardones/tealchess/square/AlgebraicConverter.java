/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.square;

import java.util.regex.Pattern;

public final class AlgebraicConverter {

    private static final String FILES = "abcdefgh";
    private static final int SIDE_LENGTH = FILES.length();
    private static final Pattern ALGEBRAIC_PATTERN = Pattern.compile("^[a-h][1-8]$");

    // TODO: Move the validations somewhere else
    public static int fileToIndex(String file) {
        var result = file.charAt(0) - 'a';

        if (result < 0 || result >= SIDE_LENGTH) {
            throw new AlgebraicNotationException("Unknown file: " + file);
        }

        return file.charAt(0) - 'a';
    }

    public static String fileFromIndex(int fileIndex) {
        if (fileIndex < 0 || fileIndex >= SIDE_LENGTH) {
            throw new AlgebraicNotationException("File index out of bounds: " + fileIndex);
        }

        return String.valueOf(FILES.charAt(fileIndex));
    }

    public static int rankToIndex(int rank) {
        if (rank < 1 || rank > SIDE_LENGTH) {
            throw new AlgebraicNotationException("Unknown rank: " + rank);
        }

        return rank - 1;
    }

    public static int rankFromIndex(int rankIndex) {
        if (rankIndex < 0 || rankIndex >= SIDE_LENGTH) {
            throw new AlgebraicNotationException("Rank index out of bounds: " + rankIndex);
        }

        return rankIndex + 1;
    }

    // TODO: Document usage of LSF to represent squares
    public static int toSquare(int fileIndex, int rankIndex) {
        if (fileIndex < 0 || fileIndex >= SIDE_LENGTH) {
            throw new AlgebraicNotationException("File index out of bounds: " + fileIndex);
        }

        if (rankIndex < 0 || rankIndex >= SIDE_LENGTH) {
            throw new AlgebraicNotationException("Rank index out of bounds: " + rankIndex);
        }

        return SIDE_LENGTH * rankIndex + fileIndex;
    }

    public static int toSquare(String algebraicNotation) {
        if (!ALGEBRAIC_PATTERN.matcher(algebraicNotation).matches()) {
            throw new AlgebraicNotationException("Invalid algebraic notation: " + algebraicNotation);
        }

        return calculateSquare(algebraicNotation);
    }

    public static String toAlgebraic(int square) {

        var fileIndex = fileIndex(square);
        var fileChar = FILES.charAt(fileIndex);
        var rank = rankFromIndex(rankIndex(square));

        return String.valueOf(fileChar) + rank;
    }

    public static int fileIndex(int square) {
        return square % SIDE_LENGTH;
    }

    public static int rankIndex(int square) {
        return square / SIDE_LENGTH;
    }

    private static int calculateSquare(String algebraicNotation) {

        var fileIndex = algebraicNotation.charAt(0) - 'a';
        var rankIndex = Character.digit(algebraicNotation.charAt(1), 10) - 1;

        return toSquare(fileIndex, rankIndex);
    }

    private AlgebraicConverter() {}
}
