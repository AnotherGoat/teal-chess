/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;

import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.piece.King;
import com.vmardones.tealchess.piece.Piece;

/** A human player, that plays using the mouse. */
public final class HumanPlayer extends Player {
    public HumanPlayer(Color color, King king, List<Piece> pieces, List<Move> legals, PlayerStatus playerStatus) {
        super(color, king, pieces, legals, playerStatus);
    }
}
