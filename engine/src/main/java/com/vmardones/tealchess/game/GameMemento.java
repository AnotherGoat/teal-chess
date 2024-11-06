/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.player.Player;
import com.vmardones.tealchess.position.Position;
import org.jspecify.annotations.Nullable;

public record GameMemento(Position position, Player whitePlayer, Player blackPlayer, @Nullable Move lastMove) {

    public Player player() {
        return position.sideToMove().isWhite() ? whitePlayer : blackPlayer;
    }

    public Player opponent() {
        return position.sideToMove().isWhite() ? blackPlayer : whitePlayer;
    }
}
