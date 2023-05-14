/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.color;

import com.vmardones.tealchess.parser.Unicode;
import com.vmardones.tealchess.parser.fen.Fen;

/**
 * Represents a chess piece's or square's color, which can be white or black.
 * @see <a href="https://www.chessprogramming.org/Color">Color</a>
 */
public enum Color implements Fen, Unicode {
    WHITE("w", "□"),
    BLACK("b", "■");

    private final String symbol;
    private final String unicodeSymbol;

    /* Static factory methods */

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

    @Override
    public String fen() {
        return symbol;
    }

    @Override
    public String unicode() {
        return unicodeSymbol;
    }

    Color(String symbol, String unicodeSymbol) {
        this.symbol = symbol;
        this.unicodeSymbol = unicodeSymbol;
    }
}
