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
        addAsset("light.png", Texture.class, createSquare(Color.valueOf("#FFCE9E")));
        addAsset("dark.png", Texture.class, createSquare(Color.valueOf("#D18B47")));
        addAsset("highlight.png", Texture.class, createSquare(Color.TEAL.mul(1, 1, 1, 0.6f)));
        addAsset("destination.png", Texture.class, createCircle(Color.TEAL, 7));
        addAsset("target.png", Texture.class, createTarget(Color.TEAL));
        addAsset("last_move.png", Texture.class, createSquare(Color.FOREST.mul(1, 1, 1, 0.5f)));
        addAsset("check.png", Texture.class, createSquare(Color.SCARLET.mul(1, 1, 1, 0.7f)));
        addAsset("promotion.png", Texture.class, createCircle(Color.LIGHT_GRAY, 2));
        addAsset("dark_tint.png", Texture.class, createSquare(Color.BLACK.mul(1, 1, 1, 0.5f)));

        PIECE_CODES.forEach(code -> {
            var texture = new Texture(loadPiecePixmap(code));
            addAsset(code + ".png", Texture.class, texture);
        });
    }

    /* Setters */

    public void theme(PieceTheme value) {
        theme = value;
    }

    private Texture createSquare(Color color) {
        var pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        return new Texture(pixmap);
    }

    private static Texture createCircle(Color color, int radiusScale) {
        var pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillCircle(SQUARE_SIZE / 2, SQUARE_SIZE / 2, SQUARE_SIZE / radiusScale);

        return new Texture(pixmap);
    }

    private static Texture createTarget(Color color) {
        var pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(1, 1, 1, 0);
        pixmap.fillCircle(SQUARE_SIZE / 2, SQUARE_SIZE / 2, 2 + SQUARE_SIZE / 2);

        return new Texture(pixmap);
    }

    private Pixmap loadPiecePixmap(String pieceCode) {
        return SvgLoader.load(formatIconPath(pieceCode), PIECE_SIZE);
    }

    private String formatIconPath(String pieceCode) {
        return "%s/%s/%s.svg".formatted(PIECE_ICON_PATH, theme, pieceCode);
    }
}
