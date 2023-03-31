/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.player;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents a chess piece's color, which can be white or black. */
@AllArgsConstructor
@Getter
public enum Alliance {
  /** The white side, at the bottom of the board. */
  WHITE(1),
  /** The black side, at the top of the board. */
  BLACK(-1);

  private final int direction;

  /**
   * Chooses the first player of this alliance.
   *
   * @param players Players to choose from
   * @return The chosen player
   */
  public Player choosePlayer(List<Player> players) {
    return players.stream()
        .filter(player -> player.getAlliance() == this)
        .findFirst()
        .orElse(players.get(0));
  }

  public Alliance getOpposite() {
    return switch (this) {
      case WHITE -> BLACK;
      case BLACK -> WHITE;
    };
  }

  public int getOppositeDirection() {
    return getOpposite().getDirection();
  }

  @Override
  public String toString() {
    return String.valueOf(super.toString().toLowerCase().charAt(0));
  }
}
