/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;

import java.util.Locale;

import com.vmardones.tealchess.parser.fen.Fen;

public enum Color implements Fen {
    /** The white side, at the bottom of the board. */
    WHITE(1),
    /** The black side, at the top of the board. */
    BLACK(-1);

    private final int direction;

    /* Getters */

    public int direction() {
        return direction;
    }

    public Color opposite() {
        return switch (this) {
            case WHITE -> BLACK;
            case BLACK -> WHITE;
        };
    }

    public int oppositeDirection() {
        return opposite().direction();
    }

    /* toString */

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase(Locale.ROOT);
    }

    Color(int direction) {
        this.direction = direction;
    }
}
