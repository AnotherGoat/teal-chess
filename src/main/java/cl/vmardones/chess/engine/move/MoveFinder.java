/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import cl.vmardones.chess.engine.board.Coordinate;
import java.util.List;
import java.util.function.Predicate;
import lombok.Generated;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MoveFinder {

  private static final Logger LOG = LoggerFactory.getLogger(MoveFinder.class);

  @Generated
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
        legalMoves.stream().filter(move -> move.getSource().equals(from)).toList());

    return legalMoves.stream().filter(isMovePossible(from, to)).findFirst().orElse(null);
  }

  private static Predicate<Move> isMovePossible(Coordinate source, Coordinate destination) {
    return move -> move.getSource().equals(source) && move.getDestination().equals(destination);
  }
}