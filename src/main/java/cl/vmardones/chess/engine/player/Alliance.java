/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.player;

/** Represents a chess piece's color, which can be white or black. */
public enum Alliance {
    /** The white side, at the bottom of the board. */
    WHITE(1),
    /** The black side, at the top of the board. */
    BLACK(-1);

    private final int direction;

    /* Getters */

    public int direction() {
        return direction;
    }

    public Alliance opposite() {
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
        return String.valueOf(super.toString().toLowerCase().charAt(0));
    }

    Alliance(int direction) {
        this.direction = direction;
    }
}
