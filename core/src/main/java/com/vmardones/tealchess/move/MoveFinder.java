/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.function.Predicate;

import com.vmardones.tealchess.ExcludeFromGeneratedReport;
import com.vmardones.tealchess.board.Coordinate;

public final class MoveFinder {

    /**
     * Given a list of legal moves, choose the first one that goes from the source to the destination.
     *
     * @param from Source coordinate.
     * @param to Destination coordinate.
     * @return Move that goes from the source to the destination, if possible.
     */
    public static List<LegalMove> choose(List<LegalMove> legalMoves, Coordinate from, Coordinate to) {

        if (from.equals(to)) {
            return emptyList();
        }

        return legalMoves.stream().filter(isMovePossible(from, to)).toList();
    }

    @ExcludeFromGeneratedReport
    private MoveFinder() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }

    private static Predicate<LegalMove> isMovePossible(Coordinate source, Coordinate destination) {
        return legal -> legal.move().source().equals(source)
                && legal.move().destination().equals(destination);
    }
}
