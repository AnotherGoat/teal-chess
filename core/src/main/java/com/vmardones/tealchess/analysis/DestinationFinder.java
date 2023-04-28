/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.*;
import java.util.stream.Stream;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.Vector;

final class DestinationFinder {

    private final Board board;

    DestinationFinder(Board board) {
        this.board = board;
    }

    Stream<Square> calculateDestinations(Piece piece) {

        var vectors = piece.moveVectors();
        var coordinate = piece.coordinate();

        if (!piece.sliding()) {
            return vectors.stream()
                    .map(vector -> coordinate.to(vector.x(), vector.y()))
                    .filter(Objects::nonNull)
                    .map(board::squareAt);
        }

        return vectors.stream().flatMap(vector -> calculateSlides(piece, coordinate, vector));
    }

    private Stream<Square> calculateSlides(Piece piece, Coordinate coordinate, Vector vector) {

        var destinations = Stream.<Square>builder();

        for (var i = 1; i < Board.SIDE_LENGTH; i++) {
            var destination = coordinate.to(vector.x() * i, vector.y() * i);

            if (destination == null) {
                break;
            }

            var square = board.squareAt(destination);
            var destinationPiece = square.piece();

            if (destinationPiece == null) {
                destinations.add(square);
                continue;
            }

            if (piece.isEnemyOf(destinationPiece)) {
                destinations.add(square);
                break;
            }

            break;
        }

        return destinations.build();
    }
}
