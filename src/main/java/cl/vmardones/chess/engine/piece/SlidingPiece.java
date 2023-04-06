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
import cl.vmardones.chess.engine.player.Alliance;

abstract sealed class SlidingPiece extends Piece permits Bishop, Queen, Rook {

    protected final List<int[]> moveVectors;

    protected SlidingPiece(Coordinate position, Alliance alliance, boolean firstMove, List<int[]> moveVectors) {
        super(position, alliance, firstMove);
        this.moveVectors = moveVectors;
    }

    public List<int[]> moveVectors() {
        return moveVectors;
    }

    @Override
    protected List<Coordinate> calculatePossibleDestinations(Board board) {
        return moveVectors.stream()
                .map(vector -> calculateOffsets(vector, board))
                .flatMap(Collection::stream)
                .toList();
    }

    private List<Coordinate> calculateOffsets(int[] vector, Board board) {
        var squares = IntStream.range(1, Board.SIDE_LENGTH + 1)
                .mapToObj(i -> position().to(vector[0] * i, vector[1] * i))
                .filter(Objects::nonNull)
                .map(board::squareAt)
                .toList()
                .listIterator();

        return filterAccessible(squares).stream().map(Square::coordinate).toList();
    }

    // TODO: Replace this method with something more stream-friendly
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
