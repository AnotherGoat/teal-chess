/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.assets;

// TODO: Add other colors, like square and piece highlights
public enum ColorTheme {
    WIKIPEDIA("#ffce9e", "#d18b47"),
    LICHESS_ORG("#f0d9b5", "#b58863"),
    CHESS_COM("#eeeed2", "#769656");

    private final String lightColor;
    private final String darkColor;

    ColorTheme(String lightColor, String darkColor) {
        this.lightColor = lightColor;
        this.darkColor = darkColor;
    }

    /* Getters */

    public String lightColor() {
        return lightColor;
    }

    public String darkColor() {
        return darkColor;
    }
}
