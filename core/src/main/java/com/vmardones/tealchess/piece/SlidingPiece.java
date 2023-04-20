/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.*;
import java.util.stream.IntStream;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.player.Color;

abstract sealed class SlidingPiece extends Piece permits Bishop, Queen, Rook {

    protected final List<int[]> moveVectors;

    protected SlidingPiece(PieceType type, String coordinate, Color color, List<int[]> moveVectors) {
        super(type, coordinate, color);
        this.moveVectors = moveVectors;
    }

    public List<int[]> moveVectors() {
        return moveVectors;
    }

    @Override
    public List<Coordinate> calculatePossibleDestinations(Board board) {
        return moveVectors.stream()
                .map(vector -> calculateOffsets(vector, board))
                .flatMap(Collection::stream)
                .toList();
    }

    private List<Coordinate> calculateOffsets(int[] vector, Board board) {
        var squares = IntStream.range(1, Board.SIDE_LENGTH + 1)
                .mapToObj(i -> coordinate().to(vector[0] * i, vector[1] * i))
                .filter(Objects::nonNull)
                .map(board::squareAt)
                .toList()
                .listIterator();

        return filterAccessible(squares).stream().map(Square::coordinate).toList();
    }

    // TODO: Remove this method, and check accesible squares somewhere else
    private List<Square> filterAccessible(Iterator<Square> squares) {

        List<Square> accessibleSquares = new ArrayList<>();

        while (squares.hasNext()) {
            var destination = squares.next();
            var pieceAtDestination = destination.piece();

            if (pieceAtDestination == null) {
                accessibleSquares.add(destination);
            } else {
                if (isEnemyOf(pieceAtDestination)) {
                    accessibleSquares.add(destination);
                }

                break;
            }
        }

        return accessibleSquares;
    }
}
