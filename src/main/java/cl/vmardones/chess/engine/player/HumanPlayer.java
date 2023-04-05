/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.player;

import java.util.List;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.move.Move;

/** A human player, that plays using the mouse. */
public final class HumanPlayer extends Player {
    public HumanPlayer(Alliance alliance, Board board, List<Move> legals, List<Move> opponentLegals) {
        super(alliance, board, legals, opponentLegals);
    }
}
