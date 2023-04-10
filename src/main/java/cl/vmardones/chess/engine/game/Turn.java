/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import java.util.Collections;
import java.util.List;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.player.Alliance;
import cl.vmardones.chess.engine.player.Player;

// TODO: Check if we only need to store each player's legal moves
record Turn(Board board, Alliance moveMaker, Player whitePlayer, Player blackPlayer) {

    Player player() {
        return switch (moveMaker) {
            case WHITE -> whitePlayer;
            case BLACK -> blackPlayer;
        };
    }

    List<Move> playerLegals() {
        return Collections.unmodifiableList(player().legals());
    }

    Player opponent() {
        return switch (moveMaker) {
            case WHITE -> blackPlayer;
            case BLACK -> whitePlayer;
        };
    }

    List<Move> opponentLegals() {
        return Collections.unmodifiableList(opponent().legals());
    }
}
