/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.*;
import java.util.stream.Stream;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.Vector;
import org.eclipse.jdt.annotation.Nullable;

final class DestinationFinder {

    private final Board board;

    DestinationFinder(Board board) {
        this.board = board;
    }

    Stream<Coordinate> calculateDestinations(Piece piece) {

        var vectors = piece.moveVectors();
        var coordinate = piece.coordinate();

        if (!piece.sliding()) {
            return vectors.stream()
                    .map(vector -> coordinate.toOrNull(vector.x(), vector.y()))
                    .filter(Objects::nonNull);
        }

        return vectors.stream().flatMap(vector -> calculateSlides(piece, vector));
    }

    private Stream<Coordinate> calculateSlides(Piece piece, Vector vector) {

        var destinations = Stream.<Coordinate>builder();

        // TODO: Refactor this method to not use break and continue in such a confusing way
        for (var i = 1; i < Board.SIDE_LENGTH; i++) {
            var destination = findDestination(piece, vector, i);

            if (destination == null) {
                break;
            }

            var destinationPiece = board.pieceAt(destination);

            if (destinationPiece == null) {
                destinations.add(destination);
                continue;
            }

            if (piece.isEnemyOf(destinationPiece)) {
                destinations.add(destination);
            }

            break;
        }

        return destinations.build();
    }

    private @Nullable Coordinate findDestination(Piece piece, Vector vector, int distance) {
        var start = piece.coordinate();

        return start.toOrNull(vector.x() * distance, vector.y() * distance);
    }
}
