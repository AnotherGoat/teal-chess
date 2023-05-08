/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.*;
import java.util.stream.Stream;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.Vector;
import org.eclipse.jdt.annotation.Nullable;

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
                    .map(vector -> coordinate.toOrNull(vector.x(), vector.y()))
                    .filter(Objects::nonNull)
                    .map(board::squareAt);
        }

        return vectors.stream().flatMap(vector -> calculateSlides(piece, vector));
    }

    private Stream<Square> calculateSlides(Piece piece, Vector vector) {

        var destinations = Stream.<Square>builder();

        // TODO: Refactor this method to not use break and continue in such a confusing way
        for (var i = 1; i < Board.SIDE_LENGTH; i++) {
            var destination = findSquare(piece, vector, i);

            if (destination == null) {
                break;
            }

            var destinationPiece = destination.piece();

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

    private @Nullable Square findSquare(Piece piece, Vector vector, int distance) {
        var start = piece.coordinate();
        var destination = start.toOrNull(vector.x() * distance, vector.y() * distance);

        if (destination == null) {
            return null;
        }

        return board.squareAt(destination);
    }
}
