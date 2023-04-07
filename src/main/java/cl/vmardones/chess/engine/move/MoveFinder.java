/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import java.util.List;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.engine.board.Position;
import org.eclipse.jdt.annotation.Nullable;

public final class MoveFinder {

    private static final Logger LOG = LogManager.getLogger(MoveFinder.class);

    private MoveFinder() {
        throw new UnsupportedOperationException("You cannot instantiate me!");
    }

    /**
     * Given a list of legal moves, choose the first one that goes from the source to the destination.
     *
     * @param from Source position.
     * @param to Destination position.
     * @return Move that goes from the source to the destination, if possible.
     */
    public static @Nullable Move choose(List<Move> legalMoves, Position from, Position to) {

        if (from.equals(to)) {
            return null;
        }

        LOG.debug(
                "Legal moves: {}",
                legalMoves.stream().filter(move -> move.source().equals(from)).toList());

        return legalMoves.stream().filter(isMovePossible(from, to)).findFirst().orElse(null);
    }

    private static Predicate<Move> isMovePossible(Position source, Position destination) {
        return move -> move.source().equals(source) && move.destination().equals(destination);
    }
}
