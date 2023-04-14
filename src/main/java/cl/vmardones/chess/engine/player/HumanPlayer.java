/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.player;

import java.util.List;

import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Piece;

/** A human player, that plays using the mouse. */
public final class HumanPlayer extends Player {
    public HumanPlayer(Color color, King king, List<Piece> pieces, List<Move> legals, PlayerStatus playerStatus) {
        super(color, king, pieces, legals, playerStatus);
    }
}
