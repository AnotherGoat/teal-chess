/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.player.Alliance;
import cl.vmardones.chess.engine.player.Player;
import java.util.Collections;
import java.util.List;

record Turn(Board board, Alliance moveMaker, Player whitePlayer, Player blackPlayer) {

  Player getPlayer() {
    return switch (moveMaker) {
      case WHITE -> whitePlayer;
      case BLACK -> blackPlayer;
    };
  }

  List<Move> getPlayerLegals() {
    return Collections.unmodifiableList(getPlayer().getLegals());
  }

  Player getOpponent() {
    return switch (moveMaker) {
      case WHITE -> blackPlayer;
      case BLACK -> whitePlayer;
    };
  }

  List<Move> getOpponentLegals() {
    return Collections.unmodifiableList(getOpponent().getLegals());
  }
}
