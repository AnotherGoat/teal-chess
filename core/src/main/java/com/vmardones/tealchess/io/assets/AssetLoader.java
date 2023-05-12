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
    public void load() {
        var boardTheme = settings.boardTheme();
        var lightColor = Color.valueOf(boardTheme.lightColor());
        var darkColor = Color.valueOf(boardTheme.darkColor());

        addFont("light_font", loadFont(lightColor.cpy().mul(1.2f)));
        addFont("dark_font", loadFont(darkColor.cpy().mul(0.8f)));
        addTexture("light", createSquare(lightColor));
        addTexture("dark", createSquare(darkColor));

        addTexture("source", createSquare(Color.TEAL, 0.6f));
        addTexture("destination", createCircle(Color.TEAL, 7));
        addTexture("target", createTarget(Color.TEAL));
        addTexture("attack", createTarget(Color.FIREBRICK));
        addTexture("last_move", createSquare(Color.FOREST, 0.5f));
        addTexture("check", createSquare(Color.FIREBRICK, 0.7f));
        addTexture("promotion", createCircle(Color.LIGHT_GRAY, 2, 0.9f));
        addTexture("dark_tint", createSquare(Color.BLACK, 0.5f));

        PIECE_CODES.forEach(code -> {
            var pixmap = loadPiecePixmap(code, settings.pieceTheme());
            addTexture(code, new Texture(pixmap));
            pixmap.dispose();
        });
    }

    public void reloadBoardTheme() {
        var boardTheme = settings.boardTheme();
        var lightColor = Color.valueOf(boardTheme.lightColor());
        var darkColor = Color.valueOf(boardTheme.darkColor());

        unload("light_font.ttf");
        addFont("light_font", loadFont(lightColor.cpy().mul(1.2f)));

        unload("dark_font.ttf");
        addFont("dark_font", loadFont(darkColor.cpy().mul(0.8f)));

        unload("light.png");
        addTexture("light", createSquare(lightColor));

        unload("dark.png");
        addTexture("dark", createSquare(darkColor));
    }

    /* Getters */
    public BitmapFont font(String fileName) {
        return get(fileName + ".ttf", BitmapFont.class);
    }

    public Texture texture(String fileName) {
        return get(fileName + ".png", Texture.class);
    }

    private void addFont(String fileName, BitmapFont font) {
        addAsset(fileName + ".ttf", BitmapFont.class, font);
    }

    private void addTexture(String fileName, Texture texture) {
        addAsset(fileName + ".png", Texture.class, texture);
    }

    private BitmapFont loadFont(Color color) {
        return FontLoader.load(FONT_NAME, FONT_SIZE, color);
    }

    private Color applyAlpha(Color color, float alpha) {
        return color.cpy().mul(1, 1, 1, alpha);
    }

    private Texture createSquare(Color color) {
        return createSquare(color, 1);
    }

    private Texture createSquare(Color color, float alpha) {
        var drawColor = applyAlpha(color, alpha);

        var pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(drawColor);
        pixmap.fill();

        var texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Texture createCircle(Color color, int radiusScale) {
        return createCircle(color, radiusScale, 1);
    }

    private Texture createCircle(Color color, int radiusScale, float alpha) {
        var drawColor = applyAlpha(color, alpha);

        var pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(drawColor);
        pixmap.fillCircle(HALF_SIZE, HALF_SIZE, SQUARE_SIZE / radiusScale - 1);

        var texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Texture createTarget(Color color) {
        var pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(1, 1, 1, 0);
        pixmap.fillCircle(HALF_SIZE, HALF_SIZE, 2 + HALF_SIZE);

        var texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Pixmap loadPiecePixmap(String pieceCode, PieceTheme theme) {
        return SvgLoader.load(formatIconPath(pieceCode, theme), SQUARE_SIZE);
    }

    private String formatIconPath(String pieceCode, PieceTheme theme) {
        return "%s/%s/%s.svg".formatted(PIECE_ICON_PATH, theme.name().toLowerCase(Locale.ROOT), pieceCode);
    }
}
