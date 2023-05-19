/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.square;

import java.util.List;
import java.util.Objects;

import com.vmardones.tealchess.parser.pgn.San;

/**
 * A coordinate is a higher level abstraction of a square.
 * It includes abstractions that the frontend code will need more frequently, like knowing its rank and file.
 * Do not use this class for expensive calculations like move generation and move making.
 */
public final class Coordinate implements San {

    private static final List<Coordinate> COORDINATE_CACHE = fillCoordinateCache();
    private final int index;
    private final String algebraicNotation;
    private final String file;
    private final int fileIndex;
    private final int rank;
    private final int rankIndex;

    /* Coordinate creation */

    /**
     * Create a coordinate using a square index.
     *
     * @param square The index of the square.
     * @return The created coordinate.
     */
    public static Coordinate forSquare(int square) {
        return COORDINATE_CACHE.get(square);
    }

    /* Getters */

    public int squareIndex() {
        return index;
    }

    @Override
    public String san() {
        return algebraicNotation;
    }

    /**
     * Obtains the file (row in chess terminology) of this coordinate. The file is a letter between lowercase a (left side or
     * queen's side) and lowercase h (right side or king's side).
     *
     * @return The coordinate's file.
     * @see <a href="https://www.chessprogramming.org/Files">Files</a>
     */
    public String file() {
        return file;
    }

    /**
     * Obtains the index of this coordinate's file. The index goes from 0 (file a) to 7 (file h).
     * @return The index of the coordinate's file.
     */
    public int fileIndex() {
        return fileIndex;
    }

    /**
     * Obtains the rank (row in chess terminology) of this coordinate. The rank is a number between 1
     * (bottom of the board, white side) and 8 (top of the board, black side).
     *
     * @return The coordinate's rank.
     * @see <a href="https://www.chessprogramming.org/Ranks">Ranks</a>
     */
    public int rank() {
        return rank;
    }

    /**
     * Obtains the index of this coordinate's rank. The index goes from 0 (rank 1) to 7 (rank 8).
     * @return The index of the coordinate's rank.
     */
    public int rankIndex() {
        return rankIndex;
    }

    /* equals, hashCode and toString */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var other = (Coordinate) o;
        return index == other.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    /**
     * Get the algebraic notation representation of this coordinate.
     *
     * @return The coordinate's algebraic notation.
     */
    @Override
    public String toString() {
        return san();
    }

    private static List<Coordinate> fillCoordinateCache() {
        return Square.all().stream().map(Coordinate::new).toList();
    }

    private Coordinate(int index) {
        this.index = index;

        algebraicNotation = AlgebraicConverter.toAlgebraic(index);
        fileIndex = AlgebraicConverter.fileIndex(index);
        rankIndex = AlgebraicConverter.rankIndex(index);
        file = AlgebraicConverter.fileFromIndex(fileIndex);
        rank = AlgebraicConverter.rankFromIndex(rankIndex);
    }
}
