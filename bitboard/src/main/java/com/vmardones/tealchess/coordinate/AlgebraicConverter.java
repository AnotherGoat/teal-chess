/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.coordinate;

import java.util.regex.Pattern;

public final class AlgebraicConverter {

    private static final String FILES = "abcdefgh";
    private static final int SIDE_LENGTH = FILES.length();
    private static final Pattern ALGEBRAIC_PATTERN = Pattern.compile("^[a-h][1-8]$");

    // TODO: Move the validations somewhere else
    public static int fileToIndex(String file) {
        var result = file.charAt(0) - 'a';

        if (result < 0 || result >= SIDE_LENGTH) {
            throw new AlgebraicConverterException("Unknown file: " + file);
        }

        return file.charAt(0) - 'a';
    }

    public static String fileFromIndex(int fileIndex) {
        if (fileIndex < 0 || fileIndex >= SIDE_LENGTH) {
            throw new AlgebraicConverterException("File index out of bounds: " + fileIndex);
        }

        return String.valueOf(FILES.charAt(fileIndex));
    }

    public static int rankToIndex(int rank) {
        if (rank < 1 || rank > SIDE_LENGTH) {
            throw new AlgebraicConverterException("Unknown rank: " + rank);
        }

        return rank - 1;
    }

    public static int rankFromIndex(int rankIndex) {
        if (rankIndex < 0 || rankIndex >= SIDE_LENGTH) {
            throw new AlgebraicConverterException("Rank index out of bounds: " + rankIndex);
        }

        return rankIndex + 1;
    }

    // TODO: Document usage of LSF to represent coordinates
    public static int toCoordinate(int fileIndex, int rankIndex) {
        if (fileIndex < 0 || fileIndex >= SIDE_LENGTH) {
            throw new AlgebraicConverterException("File index out of bounds: " + fileIndex);
        }

        if (rankIndex < 0 || rankIndex >= SIDE_LENGTH) {
            throw new AlgebraicConverterException("Rank index out of bounds: " + rankIndex);
        }

        return SIDE_LENGTH * rankIndex + fileIndex;
    }

    public static int toCoordinate(String algebraicNotation) {
        if (!ALGEBRAIC_PATTERN.matcher(algebraicNotation).matches()) {
            throw new AlgebraicConverterException("Invalid algebraic notation: " + algebraicNotation);
        }

        return calculateCoordinate(algebraicNotation);
    }

    public static String toAlgebraic(int index) {

        var fileIndex = index % SIDE_LENGTH;
        var fileChar = FILES.charAt(fileIndex);
        var rank = SIDE_LENGTH - index / SIDE_LENGTH;

        return String.valueOf(fileChar) + rank;
    }

    private AlgebraicConverter() {}

    private static int calculateCoordinate(String algebraicNotation) {

        var fileIndex = algebraicNotation.charAt(0) - 'a';
        var rankIndex = Character.digit(algebraicNotation.charAt(1), 10) - 1;

        return toCoordinate(fileIndex, rankIndex);
    }
}
