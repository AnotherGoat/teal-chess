/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.*;
import java.util.stream.IntStream;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.board.Square;
import cl.vmardones.chess.engine.player.Color;

abstract sealed class SlidingPiece extends Piece permits Bishop, Queen, Rook {

    protected final List<int[]> moveVectors;

    protected SlidingPiece(PieceType type, String coordinate, Color color, boolean firstMove, List<int[]> moveVectors) {
        super(type, coordinate, color, firstMove);
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
