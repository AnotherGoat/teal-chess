/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.assets;

import java.util.List;
import java.util.Locale;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.vmardones.tealchess.Initializer;
import com.vmardones.tealchess.io.settings.SettingManager;

public final class AssetLoader extends AssetManager {

    public static final int SQUARE_SIZE = 72;
    private static final int HALF_SIZE = SQUARE_SIZE / 2;
    private static final String PIECE_ICON_PATH = "art/piece";
    private static final List<String> PIECE_CODES =
            List.of("wP", "wN", "wB", "wR", "wQ", "wK", "bP", "bN", "bB", "bR", "bQ", "bK");
    private static final String FONT_NAME = "NotoSans-SemiBold";
    private static final int FONT_SIZE = 14;

    private final SettingManager settings;

    public AssetLoader(SettingManager settings) {
        this.settings = settings;
    }

    @Initializer
    public void reload() {
        var colorTheme = settings.colorTheme();
        var lightColor = Color.valueOf(colorTheme.lightColor());
        var darkColor = Color.valueOf(colorTheme.darkColor());

        addAsset("light_font.ttf", BitmapFont.class, loadFont(lightColor.cpy().mul(1.2f)));
        addAsset("dark_font.ttf", BitmapFont.class, loadFont(darkColor.cpy().mul(0.8f)));
        addAsset("light.png", Texture.class, createSquare(lightColor));
        addAsset("dark.png", Texture.class, createSquare(darkColor));

        addAsset("highlight.png", Texture.class, createSquare(Color.TEAL.mul(1, 1, 1, 0.6f)));
        addAsset("destination.png", Texture.class, createCircle(Color.TEAL, 7));
        addAsset("target.png", Texture.class, createTarget(Color.TEAL));
        addAsset("attack.png", Texture.class, createTarget(Color.SCARLET)); // TODO: Unused
        addAsset("last_move.png", Texture.class, createSquare(Color.FOREST.mul(1, 1, 1, 0.5f)));
        addAsset("check.png", Texture.class, createSquare(Color.SCARLET.mul(1, 1, 1, 0.7f)));
        addAsset("promotion.png", Texture.class, createCircle(Color.LIGHT_GRAY, 2));
        addAsset("dark_tint.png", Texture.class, createSquare(Color.BLACK.mul(1, 1, 1, 0.5f)));

        PIECE_CODES.forEach(code -> {
            var texture = new Texture(loadPiecePixmap(code, settings.pieceTheme()));
            addAsset(code + ".png", Texture.class, texture);
        });
    }

    private BitmapFont loadFont(Color color) {
        return FontLoader.load(FONT_NAME, FONT_SIZE, color);
    }

    /* Getters and setters */

    private Texture createSquare(Color color) {
        var pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        return new Texture(pixmap);
    }

    private static Texture createCircle(Color color, int radiusScale) {
        var pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillCircle(HALF_SIZE, HALF_SIZE, SQUARE_SIZE / radiusScale);

        return new Texture(pixmap);
    }

    private static Texture createTarget(Color color) {
        var pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(1, 1, 1, 0);
        pixmap.fillCircle(HALF_SIZE, HALF_SIZE, 2 + HALF_SIZE);

        return new Texture(pixmap);
    }

    private Pixmap loadPiecePixmap(String pieceCode, PieceTheme theme) {
        return SvgLoader.load(formatIconPath(pieceCode, theme), SQUARE_SIZE);
    }

    private String formatIconPath(String pieceCode, PieceTheme theme) {
        return "%s/%s/%s.svg".formatted(PIECE_ICON_PATH, theme.name().toLowerCase(Locale.ROOT), pieceCode);
    }
}
