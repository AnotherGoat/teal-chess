/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.reverse;
import static com.vmardones.tealchess.board.BitboardManipulator.singleBit;

import java.util.List;

import com.vmardones.tealchess.square.AlgebraicConverter;

interface OrthogonalGenerator {

    List<Long> RANK_MASKS = List.of(
            0xFFL,
            0xFF_00L,
            0xFF_00_00L,
            0xFF_00_00_00L,
            0xFF_00_00_00_00L,
            0xFF_00_00_00_00_00L,
            0xFF_00_00_00_00_00_00L,
            0xFF_00_00_00_00_00_00_00L);

    List<Long> FILE_MASKS = List.of(
            0x01_01_01_01_01_01_01_01L,
            0x02_02_02_02_02_02_02_02L,
            0x04_04_04_04_04_04_04_04L,
            0x08_08_08_08_08_08_08_08L,
            0x10_10_10_10_10_10_10_10L,
            0x20_20_20_20_20_20_20_20L,
            0x40_40_40_40_40_40_40_40L,
            0x80_80_80_80_80_80_80_80L);

    default long orthogonalMoves(int square, long occupiedSquares) {
        var slider = singleBit(square);

        var horizontalMask = RANK_MASKS.get(AlgebraicConverter.rankIndex(square));
        var horizontalMoves = shortHyperbolaQuintessence(slider, occupiedSquares, horizontalMask);

        var verticalMask = FILE_MASKS.get(AlgebraicConverter.fileIndex(square));
        var verticalMoves = hyperbolaQuintessence(slider, occupiedSquares, verticalMask);

        return horizontalMoves | verticalMoves;
    }

    private long shortHyperbolaQuintessence(long slider, long occupiedSquares, long mask) {
        var leftAttacks = occupiedSquares - 2 * slider;
        var reversedRightAttacks = reverse(occupiedSquares) - 2 * reverse(slider);
        var rightAttacks = reverse(reversedRightAttacks);

        return (leftAttacks ^ rightAttacks) & mask;
    }

    private long hyperbolaQuintessence(long slider, long occupiedSquares, long mask) {
        var occupiedLine = occupiedSquares & mask;

        var leftAttacks = occupiedLine - 2 * slider;
        var reversedRightAttacks = reverse(occupiedLine) - 2 * reverse(slider);
        var rightAttacks = reverse(reversedRightAttacks);

        return (leftAttacks ^ rightAttacks) & mask;
    }
}
