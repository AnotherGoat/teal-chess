/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;

import static java.util.Collections.unmodifiableList;

import java.util.List;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.move.*;

/**
 * The entity that controls the pieces in one side of the board. It can be controlled either by a
 * human or an AI.
 *
 * @param color  The player's side of the board.
 * @param legals The legal moves of the player.
 * @param status The state of the player, which may limit their moves.
 */
public record Player(Color color, List<Move> legals, PlayerStatus status) {

    /* Getters */

    @Override
    public List<Move> legals() {
        return unmodifiableList(legals);
    }

    /* toString */

    @Override
    public String toString() {
        var template =
                switch (status) {
                    case CHECKMATED -> "%s player, in checkmate!";
                    case STALEMATED -> "%s player, in stalemate!";
                    case CHECKED -> "%s player, in check!";
                    case NORMAL -> "%s player";
                };

        return String.format(template, color);
    }
}
