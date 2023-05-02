/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public final class AssetLoader extends AssetManager {

    private static final String LIGHT_BACKGROUND = "#FFCE9E";
    private static final String DARK_BACKGROUND = "#D18B47";
    public static final int SQUARE_SIZE = 72;
    private static final String PIECE_ICON_PATH = "art/piece";
    private static final List<String> PIECE_CODES =
            List.of("wP", "wN", "wB", "wR", "wQ", "wK", "bP", "bN", "bB", "bR", "bQ", "bK");
    private static final int PIECE_SIZE = 64;

    private PieceTheme theme;

    public AssetLoader(PieceTheme theme) {
        this.theme = theme;
    }

    public void reload() {
        addAsset("light.png", Texture.class, createSquare(LIGHT_BACKGROUND));
        addAsset("dark.png", Texture.class, createSquare(DARK_BACKGROUND));
        addAsset("highlight.png", Texture.class, createHighlight());

        PIECE_CODES.forEach(code -> {
            var texture = new Texture(loadPiecePixmap(code));
            addAsset(code + ".png", Texture.class, texture);
        });
    }

    /* Setters */

    public void theme(PieceTheme value) {
        theme = value;
    }

    private Texture createSquare(String hexCode) {
        var pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGB888);
        pixmap.setColor(Color.valueOf(hexCode));
        pixmap.fill();

        return new Texture(pixmap);
    }

    private static Texture createHighlight() {
        var pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.TEAL);
        pixmap.fillCircle(SQUARE_SIZE / 2, SQUARE_SIZE / 2, SQUARE_SIZE / 6);

        return new Texture(pixmap);
    }

    private Pixmap loadPiecePixmap(String pieceCode) {
        return SvgLoader.load(formatIconPath(pieceCode), PIECE_SIZE);
    }

    private String formatIconPath(String pieceCode) {
        return "%s/%s/%s.svg".formatted(PIECE_ICON_PATH, theme, pieceCode);
    }
}
