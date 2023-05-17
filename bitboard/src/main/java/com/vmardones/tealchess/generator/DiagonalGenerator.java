/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;

import java.util.List;

import com.vmardones.tealchess.square.AlgebraicConverter;

interface DiagonalGenerator {

    List<Long> DIAGONAL_MASKS = List.of(
            0x01L,
            0x01_02L,
            0x01_02_04L,
            0x01_02_04_08L,
            0x01_02_04_08_10L,
            0x01_02_04_08_10_20L,
            0x01_02_04_08_10_20_40L,
            0x01_02_04_08_10_20_40_80L,
            0x02_04_08_10_20_40_80_00L,
            0x04_08_10_20_40_80_00_00L,
            0x08_10_20_40_80_00_00_00L,
            0x10_20_40_80_00_00_00_00L,
            0x20_40_80_00_00_00_00_00L,
            0x40_80_00_00_00_00_00_00L,
            0x80_00_00_00_00_00_00_00L);

    List<Long> ANTI_DIAGONAL_MASKS = List.of(
            0x80L,
            0x80_40L,
            0x80_40_20L,
            0x80_40_20_10L,
            0x80_40_20_10_08L,
            0x80_40_20_10_08_04L,
            0x80_40_20_10_08_04_02L,
            0x80_40_20_10_08_04_02_01L,
            0x40_20_10_08_04_02_01_00L,
            0x20_10_08_04_02_01_00_00L,
            0x10_08_04_02_01_00_00_00L,
            0x08_04_02_01_00_00_00_00L,
            0x04_02_01_00_00_00_00_00L,
            0x02_01_00_00_00_00_00_00L,
            0x01_00_00_00_00_00_00_00L);

    default long diagonalMoves(int square, long occupiedSquares) {
        var slider = singleBit(square);

        var diagonalMask = DIAGONAL_MASKS.get(AlgebraicConverter.diagonalIndex(square));
        var diagonalMoves = hyperbolaQuintessence(slider, occupiedSquares, diagonalMask);

        var antiDiagonalMask = ANTI_DIAGONAL_MASKS.get(AlgebraicConverter.antiDiagonalIndex(square));
        var antiDiagonalMoves = hyperbolaQuintessence(slider, occupiedSquares, antiDiagonalMask);

        return diagonalMoves | antiDiagonalMoves;
    }

    default long hyperbolaQuintessence(long slider, long occupiedSquares, long mask) {
        var occupiedLine = occupiedSquares & mask;

        var leftAttacks = occupiedLine - 2 * slider;
        var reversedRightAttacks = Long.reverse(occupiedLine) - 2 * Long.reverse(slider);
        var rightAttacks = Long.reverse(reversedRightAttacks);

        return (leftAttacks ^ rightAttacks) & mask;
    }
}
