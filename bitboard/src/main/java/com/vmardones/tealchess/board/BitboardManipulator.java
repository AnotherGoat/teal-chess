/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import java.util.ArrayList;
import java.util.List;

public final class BitboardManipulator {

    /**
     * Get a bitboard with a single bit at the position that represents a specific square.
     * @param square The square.
     * @return Single bit at the requested square.
     */
    public static long singleBit(int square) {
        return 1L << square;
    }

    /**
     * Set the bit at a specific square to 1.
     * A 0 will become a 1, an existing 1 will stay the same.
     * @param bitboard The bitboard.
     * @param square The square to set.
     * @return The bitboard with the set bit.
     */
    public static long set(long bitboard, int square) {
        return bitboard | singleBit(square);
    }

    /**
     * Clear the bit at a specific square.
     * A 1 will become a 0, an existing 0 will stay the same.
     * @param bitboard The bitboard.
     * @param square The square to clear.
     * @return The bitboard with the cleared bit.
     */
    public static long clear(long bitboard, int square) {
        return bitboard & ~singleBit(square);
    }

    /**
     * Toggle the bit at a specific square.
     * A 1 will be toggled to 0 and a 0 will be toggled to 1.
     * @param bitboard The bitboard.
     * @param square The square to toggle.
     * @return The bitboard with the toggled bit.
     */
    public static long toggle(long bitboard, int square) {
        return bitboard ^ singleBit(square);
    }

    /**
     * Check if the bit at a specific square is a 1.
     * @param bitboard The bitboard to check.
     * @param square The square to check.
     * @return True if the bit is set (is equal to 1), false otherwise.
     */
    public static boolean isSet(long bitboard, int square) {
        return (bitboard & singleBit(square)) != 0;
    }

    /**
     * Find the square represented by the first bit in a bitboard.
     * If the input is 0, the first bit will be 64 (outside the bitboard).
     * Please note that the returned square is inclusive, which means isSet(square) will always be true.
     * @param bitboard The bitboard to check.
     * @return Square represented by the first bit.
     */
    public static int firstBit(long bitboard) {
        return Long.numberOfTrailingZeros(bitboard);
    }

    /**
     * Find the square represented by the last bit in a bitboard.
     * If the input is 0, the first bit will be -1 (outside the bitboard).
     * Please note that the returned square is inclusive, which means isSet(square) will always be true.
     * @param bitboard The bitboard to check.
     * @return Square represented by the last bit.
     */
    public static int lastBit(long bitboard) {
        return 63 - Long.numberOfLeadingZeros(bitboard);
    }

    public static long reverse(long bitboard) {
        return Long.reverseBytes(bitboard);
    }

    /**
     * Count the number of set bits in a bitboard.
     * @param bitboard The bitboard to check.
     * @return Number of set bits.
     */
    public static int bitCount(long bitboard) {
        if (bitboard == 0L) {
            return 0;
        }

        var count = 0;
        var nextBit = firstBit(bitboard);

        do {
            bitboard = clear(bitboard, nextBit);
            count++;
            nextBit = firstBit(bitboard);
        } while (isSet(bitboard, nextBit));

        return count;
    }

    /**
     * Represent a bitboard as a human-readable String.
     * This representation adds a line break after every 8th bit, to draw the chessboard as a square.
     * The representation has squares equal to the ones in a real chessboard, which are not in the same as the one in which the bitboard is stored internally.
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
