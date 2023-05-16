/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;

import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.square.AlgebraicConverter;
import com.vmardones.tealchess.square.Square;

final class KnightMoveGenerator extends MoveGenerator {

    private static final long FILES_AB = 0x03_03_03_03_03_03_03_03L;
    private static final long FILES_GH = 0xc0_c0_c0_c0_c0_c0_c0_c0L;
    private static final long KNIGHT_RANGE = 0x0a_11_00_11_0aL;
    private static final long KNIGHT_RANGE_CENTER = Square.c3;

    private final long emptySquares;
    private final long knights;
    private final long capturablePieces;

    KnightMoveGenerator(Position position) {
        super(position);
        var board = position.board();
        var sideToMove = position.sideToMove();

        emptySquares = board.emptySquares();
        knights = board.knights(sideToMove);
        capturablePieces = board.capturablePieces(sideToMove);
    }

    @Override
    public List<Move> generate() {
        if (knights == 0) {
            return moves;
        }

        addNormalMoves();
        addCaptures();

        return moves;
    }

    private long moveKnightRange(int newCenter) {
        if (newCenter > KNIGHT_RANGE_CENTER) {
            return KNIGHT_RANGE << (newCenter - KNIGHT_RANGE_CENTER);
        }

        return KNIGHT_RANGE >> (KNIGHT_RANGE_CENTER - newCenter);
    }

    private void addNormalMoves() {
        addKnightMoves(MoveType.NORMAL, emptySquares);
    }

    private void addCaptures() {
        addKnightMoves(MoveType.CAPTURE, capturablePieces);
    }

    private void addKnightMoves(MoveType type, long intersection) {
        var possibleKnights = knights;
        var nextKnight = firstBit(possibleKnights);

        do {
            long possibleMoves = moveKnightRange(nextKnight) & intersection;

            var fileIndex = AlgebraicConverter.fileIndex(nextKnight);
            if (fileIndex < 4) {
                possibleMoves = possibleMoves & ~FILES_GH;
            } else {
                possibleMoves = possibleMoves & ~FILES_AB;
            }

            addMoves(type, possibleMoves, nextKnight);

            possibleKnights = clear(possibleKnights, nextKnight);
            nextKnight = firstBit(possibleKnights);
        } while (isSet(possibleKnights, nextKnight));
    }
}
