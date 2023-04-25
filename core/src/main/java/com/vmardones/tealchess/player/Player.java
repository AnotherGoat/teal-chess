/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;

import static java.util.Collections.unmodifiableList;

import java.util.List;

import com.vmardones.tealchess.move.*;
import com.vmardones.tealchess.piece.King;
import com.vmardones.tealchess.piece.Piece;

/**
 * The entity that controls the pieces in one side of the board. It can be controlled either by a
 * human or an AI.
 */
public abstract sealed class Player permits HumanPlayer {

    protected final Color color;
    protected final King king;
    protected final List<Piece> pieces;
    protected final List<LegalMove> legals;
    protected final PlayerStatus status;

    /* Player creation */

    /**
     * Create a new player.
     *
     * @param color The player's side of the board.
     * @param king The player's king.
     * @param pieces The player's pieces (including the king).
     * @param legals The legal moves of the player.
     * @param status The state of the player, which may limit their moves.
     */
    protected Player(Color color, King king, List<Piece> pieces, List<LegalMove> legals, PlayerStatus status) {
        this.color = color;
        this.king = king;
        this.pieces = pieces;
        this.legals = legals;
        this.status = status;
    }

    /* Getters */

    public Color color() {
        return color;
    }

    public King king() {
        return king;
    }

    public List<Piece> pieces() {
        return unmodifiableList(pieces);
    }

    public List<LegalMove> legals() {
        return unmodifiableList(legals);
    }

    public PlayerStatus status() {
        return status;
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

        return String.format(template, color.name());
    }
}
