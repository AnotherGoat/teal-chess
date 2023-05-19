/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

import com.vmardones.tealchess.square.Coordinate;

/**
 * Filters moves based on source and destination coordinates.
 * Mainly used by frontend code.
 */
public final class MoveFinder {

    public MoveFinder() {}

    public Set<Coordinate> findDestinations(List<Move> legalMoves, Coordinate from) {
        return legalMoves.stream()
                .filter(move -> move.source() == from.squareIndex())
                .map(Move::destination)
                .map(Coordinate::forSquare)
                .collect(toSet());
    }

    /**
     * Given a list of legal moves, find the ones that go from the source to the destination.
     *
     * @param from Source coordinate.
     * @param to Destination coordinate.
     * @return Move that goes from the source to the destination, if possible.
     */
    public List<Move> find(List<Move> legalMoves, Coordinate from, Coordinate to) {

        if (from.equals(to)) {
            return emptyList();
        }

        return legalMoves.stream()
                .filter(move -> move.source() == from.squareIndex() && move.destination() == to.squareIndex())
                .toList();
    }
}
