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

final class BishopMoveGenerator implements MoveGenerator, DiagonalGenerator {

    @Override
    public List<Move> generate(Position position) {
        var board = position.board();
        var sideToMove = position.sideToMove();
        var bishops = board.bishops(sideToMove);

        if (bishops == 0) {
            return emptyList();
        }

        var emptySquares = board.emptySquares();
        var occupiedSquares = board.occupiedSquares();
        var capturablePieces = board.capturablePieces(sideToMove);

        var moves = new ArrayList<Move>();

        var nextBishop = firstBit(bishops);

        do {
            var diagonalMoves = diagonalMoves(nextBishop, occupiedSquares);
            addMoves(moves, MoveType.NORMAL, diagonalMoves & emptySquares, nextBishop);
            addMoves(moves, MoveType.CAPTURE, diagonalMoves & capturablePieces, nextBishop);

            bishops = clear(bishops, nextBishop);
            nextBishop = firstBit(bishops);
        } while (isSet(bishops, nextBishop));

        return moves;
    }

    BishopMoveGenerator() {}
}
