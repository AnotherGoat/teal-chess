/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import java.util.Collections;
import java.util.List;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.player.Color;
import cl.vmardones.chess.engine.player.Player;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A chess position. The state of the game at a specific point in time.
 * @see <a href="https://www.chessprogramming.org/Chess_Position">Chess Position</a>
 * @see <a href="https://www.chessprogramming.org/Side_to_move">Side to move</a>
 */
record Position(Board board, Color sideToMove, Player whitePlayer, Player blackPlayer, @Nullable Move lastMove) {

    Player player() {
        return switch (sideToMove) {
            case WHITE -> whitePlayer;
            case BLACK -> blackPlayer;
        };
    }

    List<Move> playerLegals() {
        return Collections.unmodifiableList(player().legals());
    }

    Player opponent() {
        return switch (sideToMove) {
            case WHITE -> blackPlayer;
            case BLACK -> whitePlayer;
        };
    }

    List<Move> opponentLegals() {
        return Collections.unmodifiableList(opponent().legals());
    }
}
