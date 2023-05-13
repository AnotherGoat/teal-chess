/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

public final class BitboardManipulator {

    public static long set(long bitboard, int coordinate) {
        return bitboard | singleBit(coordinate);
    }

    public static long clear(long bitboard, int coordinate) {
        return bitboard & ~singleBit(coordinate);
    }

    public static long toggle(long bitboard, int coordinate) {
        return bitboard ^ singleBit(coordinate);
    }

    public static boolean isSet(long bitboard, int coordinate) {
        return (bitboard & singleBit(coordinate)) != 0;
    }

    private static long singleBit(int position) {
        return 1L << position;
    }

    private BitboardManipulator() {}
}
