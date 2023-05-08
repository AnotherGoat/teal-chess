/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;

import java.util.Locale;

import com.vmardones.tealchess.parser.fen.Fen;

/**
 * Represents a chess piece's color, which can be white or black.
 * @see <a href="https://www.chessprogramming.org/Color">Color</a>
 */
public enum Color implements Fen {
    /** The white side, at the bottom of the board. */
    WHITE("w", 1),
    /** The black side, at the top of the board. */
    BLACK("b", -1);

    private final String symbol;
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

    public boolean isWhite() {
        return this == WHITE;
    }

    public boolean isBlack() {
        return !isWhite();
    }

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

    @Override
    public String fen() {
        return symbol;
    }

    /* toString */

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase(Locale.ROOT);
    }

    Color(String symbol, int direction) {
        this.symbol = symbol;
        this.direction = direction;
    }
}
