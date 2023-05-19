/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.square;

// TODO: Add paragraph breaks to other javadocs, similar to this one

import java.util.List;
import java.util.stream.IntStream;

import com.vmardones.tealchess.board.Board;

/**
 * A square is one of the 64 places where a piece can be found.
 * It's usually identified by its chess algebraic notation, which consists of the square's rank (a-h) folowed by its file (1-8).
 * Indexes are represented in Little-Endian Rank-File mapping (LERF).
 * <p>
 * This class is full of static constants, which contain the index of each square.
 * This is done to improve code readabilty (because it allows using static imports) and performance (because it avoids having to use {@code AlgebraicConverter} too often).
 * <p>
 * Note that these constants don't follow Java's code conventions.
 * Instead, they follow Chess conventions, where files are represented by lowercase letters.
 * <p>
 * Even if unused, none of these 64 constants should be deleted.
 * Feel free to use them in unit tests too!
 * @see AlgebraicConverter
 * @see <a href="https://www.chessprogramming.org/Squares">Squares</a>
 * @see <a href="https://www.chessprogramming.org/Square_Mapping_Considerations#Little-Endian_Rank-File_Mapping">Little-Endian Rank-File Mapping</a>
 */
public final class Square {
    public static final int a1 = 0, b1 = 1, c1 = 2, d1 = 3, e1 = 4, f1 = 5, g1 = 6, h1 = 7;
    public static final int a2 = 8, b2 = 9, c2 = 10, d2 = 11, e2 = 12, f2 = 13, g2 = 14, h2 = 15;
    public static final int a3 = 16, b3 = 17, c3 = 18, d3 = 19, e3 = 20, f3 = 21, g3 = 22, h3 = 23;
    public static final int a4 = 24, b4 = 25, c4 = 26, d4 = 27, e4 = 28, f4 = 29, g4 = 30, h4 = 31;
    public static final int a5 = 32, b5 = 33, c5 = 34, d5 = 35, e5 = 36, f5 = 37, g5 = 38, h5 = 39;
    public static final int a6 = 40, b6 = 41, c6 = 42, d6 = 43, e6 = 44, f6 = 45, g6 = 46, h6 = 47;
    public static final int a7 = 48, b7 = 49, c7 = 50, d7 = 51, e7 = 52, f7 = 53, g7 = 54, h7 = 55;
    public static final int a8 = 56, b8 = 57, c8 = 58, d8 = 59, e8 = 60, f8 = 61, g8 = 62, h8 = 63;

    private static final List<Integer> SQUARE_CACHE =
            IntStream.range(0, Board.NUMBER_OF_SQUARES).boxed().toList();

    public static List<Integer> all() {
        return SQUARE_CACHE;
    }

    private Square() {}
}
