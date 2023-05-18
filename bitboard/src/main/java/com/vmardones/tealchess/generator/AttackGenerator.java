/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.square.AlgebraicConverter;
import com.vmardones.tealchess.square.Square;

final class AttackGenerator implements DiagonalGenerator, LookupGenerator, OrthogonalGenerator {

    private static final long FILE_A = 0x01_01_01_01_01_01_01_01L;
    private static final long FILE_H = 0x80_80_80_80_80_80_80_80L;
    private static final long FILES_AB = 0x03_03_03_03_03_03_03_03L;
    private static final long FILES_GH = 0xc0_c0_c0_c0_c0_c0_c0_c0L;
    private static final long KNIGHT_PATTERN = 0x0a_11_00_11_0aL;
    private static final int KNIGHT_PATTERN_CENTER = Square.c3;
    private static final long KING_PATTERN = 0x07_05_07L;
    private static final int KING_PATTERN_CENTER = Square.b2;
    private static final int LEFT_PAWN_CAPTURE_OFFSET = 7;
    private static final int RIGHT_PAWN_CAPTURE_OFFSET = 9;

    /**
     * Generate a bitboard with all the squares attacked by one of the sides.
     * @param position The position to analyze.
     * @param attacker The side to calculate attacks for.
     * @return Attacked squares bitboard.
     */
    long generate(Position position, Color attacker) {
        var board = position.board();

        var attacks = 0L;

        var pawns = board.pawns(attacker);
        if (attacker.isWhite()) {
            attacks = addWhitePawnAttacks(attacks, pawns);
        } else {
            attacks = addBlackPawnAttacks(attacks, pawns);
        }

        var knights = board.knights(attacker);
        attacks = addKnightAttacks(attacks, knights);

        var rooks = board.rooks(attacker);
        var queens = board.queens(attacker);
        var occupiedSquares = board.occupiedSquares();
        attacks = addOrthogonalAttacks(attacks, rooks | queens, occupiedSquares);

        var bishops = board.bishops(attacker);
        attacks = addDiagonalAttacks(attacks, bishops | queens, occupiedSquares);

        var king = board.kings(attacker);
        attacks = addKingAttacks(attacks, king);

        return attacks;
    }

    private long addWhitePawnAttacks(long attacks, long pawns) {
        if (pawns == 0) {
            return attacks;
        }

        var leftCapturePawns = pawns << LEFT_PAWN_CAPTURE_OFFSET;
        attacks |= leftCapturePawns & ~FILE_H;

        var rightCapturePawns = pawns << RIGHT_PAWN_CAPTURE_OFFSET;
        attacks |= rightCapturePawns & ~FILE_A;

        return attacks;
    }

    private long addBlackPawnAttacks(long attacks, long pawns) {
        if (pawns == 0) {
            return attacks;
        }

        var leftCapturePawns = pawns >> RIGHT_PAWN_CAPTURE_OFFSET;
        attacks |= leftCapturePawns & ~FILE_H;

        var rightCapturePawns = pawns >> LEFT_PAWN_CAPTURE_OFFSET;
        attacks |= rightCapturePawns & ~FILE_A;

        return attacks;
    }

    private long addKnightAttacks(long attacks, long knights) {
        if (knights == 0) {
            return attacks;
        }

        var nextKnight = firstBit(knights);

        do {
            var attacksToAdd = shiftPattern(KNIGHT_PATTERN, KNIGHT_PATTERN_CENTER, nextKnight);

            var fileIndex = AlgebraicConverter.fileIndex(nextKnight);
            if (fileIndex < 4) {
                attacksToAdd = attacksToAdd & ~FILES_GH;
            } else {
                attacksToAdd = attacksToAdd & ~FILES_AB;
            }

            attacks |= attacksToAdd;

            knights = clear(knights, nextKnight);
            nextKnight = firstBit(knights);
        } while (isSet(knights, nextKnight));

        return attacks;
    }

    private long addOrthogonalAttacks(long attacks, long orthogonalSliders, long occupiedSquares) {
        if (orthogonalSliders == 0) {
            return attacks;
        }

        var nextSlider = firstBit(orthogonalSliders);

        do {
            var orthogonalAttacks = orthogonalMoves(nextSlider, occupiedSquares);

            attacks |= orthogonalAttacks;

            orthogonalSliders = clear(orthogonalSliders, nextSlider);
            nextSlider = firstBit(orthogonalSliders);
        } while (isSet(orthogonalSliders, nextSlider));

        return attacks;
    }

    private long addDiagonalAttacks(long attacks, long diagonalSliders, long occupiedSquares) {
        if (diagonalSliders == 0) {
            return attacks;
        }

        var nextSlider = firstBit(diagonalSliders);

        do {
            var diagonalAttacks = diagonalMoves(nextSlider, occupiedSquares);

            attacks |= diagonalAttacks;

            diagonalSliders = clear(diagonalSliders, nextSlider);
            nextSlider = firstBit(diagonalSliders);
        } while (isSet(diagonalSliders, nextSlider));

        return attacks;
    }

    private long addKingAttacks(long attacks, long king) {

        var kingSquare = firstBit(king);
        var attacksToAdd = shiftPattern(KING_PATTERN, KING_PATTERN_CENTER, kingSquare);

        var fileIndex = AlgebraicConverter.fileIndex(kingSquare);
        if (fileIndex < 4) {
            attacksToAdd = attacksToAdd & ~FILE_H;
        } else {
            attacksToAdd = attacksToAdd & ~FILE_A;
        }

        attacks |= attacksToAdd;
        return attacks;
    }

    AttackGenerator() {}
}
