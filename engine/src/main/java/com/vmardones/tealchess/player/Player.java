/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

import java.util.List;
import java.util.Set;

import com.vmardones.tealchess.move.*;
import com.vmardones.tealchess.piece.King;
import com.vmardones.tealchess.piece.Piece;

/**
 * The entity that controls the pieces in one side of the board. It can be controlled either by a
 * human or an AI.
 *
 * @param color  The player's side of the board.
 * @param king   The player's king.
 * @param pieces The player's pieces (including the king).
 * @param legals The legal moves of the player.
 * @param status The state of the player, which may limit their moves.
 */
public record Player(Color color, King king, Set<Piece> pieces, List<LegalMove> legals, PlayerStatus status) {

    /* Getters */

    @Override
    public Set<Piece> pieces() {
        return unmodifiableSet(pieces);
    }

    @Override
    public List<LegalMove> legals() {
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
