/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.player;

/** Represents a chess piece's color, which can be white or black. */
public enum Color {
    /** The white side, at the bottom of the board. */
    WHITE(1),
    /** The black side, at the top of the board. */
    BLACK(-1);

    private final int direction;

    /* Alternate color construction */

    /**
     * Alternative method to get a color, useful for parsing.
     * Only valid options are "w" for white and "b" for black.
     *
     * @param symbol The color symbol.
     * @return The color with the asked symbol.
     */
    public static Color fromSymbol(String symbol) {
        return switch (symbol) {
            case "w" -> Color.WHITE;
            case "b" -> Color.BLACK;
            default -> throw new ColorSymbolException(symbol);
        };
    }

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
        return String.valueOf(super.toString().toLowerCase().charAt(0));
    }

    Color(int direction) {
        this.direction = direction;
    }
}
