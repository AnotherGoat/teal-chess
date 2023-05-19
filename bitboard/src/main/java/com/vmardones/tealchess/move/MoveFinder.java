/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

public final class MoveFinder {

    public MoveFinder() {}

    public Set<Integer> findDestinations(List<Move> legalMoves, int from) {
        return legalMoves.stream()
                .filter(move -> move.source() == from)
                .map(Move::destination)
                .collect(toSet());
    }

    /**
     * Given a list of legal moves, find the ones that go from the source to the destination.
     *
     * @param from Source coordinate.
     * @param to Destination coordinate.
     * @return Move that goes from the source to the destination, if possible.
     */
    public List<Move> find(List<Move> legalMoves, int from, int to) {

        if (from == to) {
            return emptyList();
        }

        return legalMoves.stream()
                .filter(move -> move.source() == from && move.destination() == to)
                .toList();
    }
}
