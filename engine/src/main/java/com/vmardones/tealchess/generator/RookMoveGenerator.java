/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.position.Position;

final class RookMoveGenerator implements MoveGenerator, OrthogonalGenerator {

    @Override
    public List<Move> generate(Position position) {
        var board = position.board();
        var sideToMove = position.sideToMove();
        var rooks = board.rooks(sideToMove);

        if (rooks == 0) {
            return new ArrayList<>();
        }

        var emptySquares = board.emptySquares();
        var occupiedSquares = board.occupiedSquares();
        var capturablePieces = board.capturablePieces(sideToMove);

        var moves = new ArrayList<Move>();

        var nextRook = firstBit(rooks);

        do {
            var orthogonalMoves = orthogonalMoves(nextRook, occupiedSquares);
            addMoves(moves, MoveType.NORMAL, orthogonalMoves & emptySquares, nextRook);
            addMoves(moves, MoveType.CAPTURE, orthogonalMoves & capturablePieces, nextRook);

            rooks = clear(rooks, nextRook);
            nextRook = firstBit(rooks);
        } while (isSet(rooks, nextRook));

        return moves;
    }

    RookMoveGenerator() {}
}
