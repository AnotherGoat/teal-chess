/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.square.AlgebraicConverter;
import com.vmardones.tealchess.square.Square;

final class KnightMoveGenerator implements MoveGenerator, LookupGenerator {

    private static final long FILES_AB = 0x03_03_03_03_03_03_03_03L;
    private static final long FILES_GH = 0xc0_c0_c0_c0_c0_c0_c0_c0L;
    private static final long KNIGHT_PATTERN = 0x0a_11_00_11_0aL;
    private static final int KNIGHT_PATTERN_CENTER = Square.c3;

    @Override
    public List<Move> generate(Position position) {
        var board = position.board();
        var sideToMove = position.sideToMove();
        var knights = board.knights(sideToMove);

        if (knights == 0) {
            return emptyList();
        }

        var moves = new ArrayList<Move>();

        var emptySquares = board.emptySquares();
        addKnightMoves(moves, MoveType.NORMAL, knights, emptySquares);

        var capturablePieces = board.capturablePieces(sideToMove);
        addKnightMoves(moves, MoveType.CAPTURE, knights, capturablePieces);

        return moves;
    }

    KnightMoveGenerator() {}

    private void addKnightMoves(List<Move> moves, MoveType type, long knights, long intersection) {
        var nextKnight = firstBit(knights);

        do {
            var movesToAdd = shiftPattern(KNIGHT_PATTERN, KNIGHT_PATTERN_CENTER, nextKnight) & intersection;

            var fileIndex = AlgebraicConverter.fileIndex(nextKnight);
            if (fileIndex < 4) {
                movesToAdd = movesToAdd & ~FILES_GH;
            } else {
                movesToAdd = movesToAdd & ~FILES_AB;
            }

            addMoves(moves, type, movesToAdd, nextKnight);

            knights = clear(knights, nextKnight);
            nextKnight = firstBit(knights);
        } while (isSet(knights, nextKnight));
    }
}
