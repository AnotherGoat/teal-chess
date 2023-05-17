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

final class QueenMoveGenerator implements MoveGenerator, OrthogonalGenerator, DiagonalGenerator {

    @Override
    public List<Move> generate(Position position) {
        var board = position.board();
        var sideToMove = position.sideToMove();
        var queens = board.queens(sideToMove);

        if (queens == 0) {
            return emptyList();
        }

        var emptySquares = board.emptySquares();
        var occupiedSquares = board.occupiedSquares();
        var capturablePieces = board.capturablePieces(sideToMove);

        var moves = new ArrayList<Move>();

        var possibleQueens = queens;
        var nextQueen = firstBit(possibleQueens);

        do {
            var orthogonalMoves = orthogonalMoves(nextQueen, occupiedSquares);
            addMoves(moves, MoveType.NORMAL, orthogonalMoves & emptySquares, nextQueen);
            addMoves(moves, MoveType.CAPTURE, orthogonalMoves & capturablePieces, nextQueen);

            var diagonalMoves = diagonalMoves(nextQueen, occupiedSquares);
            addMoves(moves, MoveType.NORMAL, diagonalMoves & emptySquares, nextQueen);
            addMoves(moves, MoveType.CAPTURE, diagonalMoves & capturablePieces, nextQueen);

            possibleQueens = clear(possibleQueens, nextQueen);
            nextQueen = firstBit(possibleQueens);
        } while (isSet(possibleQueens, nextQueen));

        return moves;
    }

    QueenMoveGenerator() {}
}
