/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.firstBit;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.square.AlgebraicConverter;
import com.vmardones.tealchess.square.Square;

final class KingMoveGenerator implements MoveGenerator, LookupGenerator {

    private static final long KING_PATTERN = 0x07_05_07L;
    private static final int KING_PATTERN_CENTER = Square.b2;
    private static final long FILE_A = 0x01_01_01_01_01_01_01_01L;
    private static final long FILE_H = 0x80_80_80_80_80_80_80_80L;

    @Override
    public List<Move> generate(Position position) {
        var board = position.board();
        var sideToMove = position.sideToMove();
        var king = board.kings(sideToMove);

        var moves = new ArrayList<Move>();

        var emptySquares = board.emptySquares();
        addKingMoves(moves, MoveType.NORMAL, king, emptySquares);

        var capturablePieces = board.capturablePieces(sideToMove);
        addKingMoves(moves, MoveType.CAPTURE, king, capturablePieces);

        return moves;
    }

    KingMoveGenerator() {}

    private void addKingMoves(List<Move> moves, MoveType type, long king, long intersection) {

        var kingSquare = firstBit(king);
        var movesToAdd = shiftPattern(KING_PATTERN, KING_PATTERN_CENTER, kingSquare) & intersection;

        var fileIndex = AlgebraicConverter.fileIndex(kingSquare);
        if (fileIndex < 4) {
            movesToAdd = movesToAdd & ~FILE_H;
        } else {
            movesToAdd = movesToAdd & ~FILE_A;
        }

        addMoves(moves, type, movesToAdd, kingSquare);
    }
}
