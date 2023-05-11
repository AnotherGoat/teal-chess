/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.assets;

// TODO: Add other colors, like square and piece highlights
public enum BoardTheme {
    BLUE("#dee3e6", "#8ca2ad"),
    BUBBLEGUM("#ffffff", "#fcd8dd"),
    CHECKERS("#c74c51", "#303030"),
    CHESS_COM("#eeeed2", "#769656"),
    FOREST("#c7f0d8", "#43523d"),
    GLASS("#697282", "#232a36"),
    GOLD("#eae052", "#d8b208"),
    INSTANT_CHESS("#ececec", "#c1c18e"),
    LICHESS_ORG("#f0d9b5", "#b58863"),
    LIGHT("#dcdcdc", "#ababab"),
    MARINE("#8298af", "#688195"),
    MONOCHROME("#bbbbbb", "#444444"),
    ORANGE("#fce4b2", "#d08b18"),
    PINK("#eff0c2", "#f27676"),
    PURPLE("#9f90b0", "#7d4a8d"),
    RED("#f0d8bf", "#ba5546"),
    SKY("#efefef", "#c2d7e2"),
    TAN("#edc9a2", "#d3a36a"),
    TEAL("#66b2b2", "#006666"),
    TOURNAMENT("#eeeeec", "#31684b"),
    WIKIPEDIA("#ffce9e", "#d18b47"),
    YELLOW("#f1f6b2", "#58915c");

    private final String lightColor;
    private final String darkColor;

    BoardTheme(String lightColor, String darkColor) {
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

    public BoardTheme previous() {
        var index = Math.floorMod(ordinal() - 1, values().length);
        return values()[index];
    }

    public BoardTheme next() {
        var index = Math.floorMod(ordinal() + 1, values().length);
        return values()[index];
    }
}
