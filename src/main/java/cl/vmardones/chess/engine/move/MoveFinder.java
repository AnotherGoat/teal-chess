/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import java.util.List;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.engine.board.Coordinate;
import org.eclipse.jdt.annotation.Nullable;

public final class MoveFinder {

    private static final Logger LOG = LogManager.getLogger(MoveFinder.class);

    private MoveFinder() {
        throw new UnsupportedOperationException("You cannot instantiate me!");
    }

    /**
     * Given a list of legal moves, choose the first one that goes from the source to the destination.
     *
     * @param from Source coordinate.
     * @param to Destination coordinate.
     * @return Move that goes from the source to the destination, if possible.
     */
    public static @Nullable Move choose(List<Move> legalMoves, Coordinate from, Coordinate to) {

        if (from.equals(to)) {
            return null;
        }

        LOG.debug(
                "Legal moves: {}",
                legalMoves.stream()
                        .filter(move -> move.getSource().equals(from))
                        .toList());

        return legalMoves.stream().filter(isMovePossible(from, to)).findFirst().orElse(null);
    }

    private static Predicate<Move> isMovePossible(Coordinate source, Coordinate destination) {
        return move -> move.getSource().equals(source) && move.destination().equals(destination);
    }
}
