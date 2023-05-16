/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import java.util.ArrayList;
import java.util.List;

public final class BitboardManipulator {

    public static long singleBit(int coordinate) {
        return 1L << coordinate;
    }

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

    public static int firstBit(long bitboard) {
        return Long.numberOfTrailingZeros(bitboard);
    }

    public static int lastBit(long bitboard) {
        return 63 - Long.numberOfLeadingZeros(bitboard);
    }

    /**
     * Represent a bitboard as a human-readable String.
     * This representation adds a line break after every 8th bit, to draw the chessboard as a square.
     * The representation has coordinates equal to the ones in a real chessboard, which are not the same as the order in which the bitboard is stored internally.
     * In other words, the bottom-left square is "a1" and the top-right square is "h8".
     * @param bitboard The bitboard.
     * @return String representation of the bitboard.
     */
    public static String toString(long bitboard) {
        var numberOfZeros = Long.numberOfLeadingZeros(bitboard);
        var fullBits = "0".repeat(numberOfZeros);

        if (bitboard != 0) {
            fullBits += Long.toBinaryString(bitboard);
        }

        var splitBytes = splitIntoBytes(fullBits);
        var result = new StringBuilder();

        for (var i = 0; i < Long.BYTES; i++) {
            var rank = splitBytes.get(i);
            result.append(rank).append("\n");
        }

        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    private static List<String> splitIntoBytes(String bitboard) {
        var parts = new ArrayList<String>();

        for (var i = 0; i < Long.SIZE; i += Long.BYTES) {
            var bits = bitboard.substring(i, i + Long.BYTES);
            var reversedBits = new StringBuilder(bits).reverse().toString();
            parts.add(reversedBits);
        }

        return parts;
    }

    private BitboardManipulator() {}
}
