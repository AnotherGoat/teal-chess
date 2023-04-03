/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.player.Alliance;
import java.util.stream.Stream;

/**
 * The king piece. The most important piece in the game, must be defended at all costs. It moves
 * like the queen, but only one space at a time. It also cannot move into a position where it could
 * be captured.
 */
public final class King extends JumpingPiece {

  public King(Coordinate position, Alliance alliance) {
    this(position, alliance, true);
  }

  @Override
  public King moveTo(Coordinate destination) {
    return new King(destination, alliance, false);
  }

  private King(Coordinate position, Alliance alliance, boolean firstMove) {
    super(
        position,
        alliance,
        firstMove,
        Stream.concat(ORTHOGONALS.stream(), DIAGONALS.stream()).toList());
  }
}
