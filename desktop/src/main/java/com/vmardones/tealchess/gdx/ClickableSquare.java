/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.io.PieceLoader;
import com.vmardones.tealchess.io.PieceTheme;

final class ClickableSquare extends Actor {

    static final int SIZE = 72;
    private static final Texture LIGHT_TEXTURE = createBackground("#FFCE9E");
    private static final Texture DARK_TEXTURE = createBackground("#D18B47");
    private static final List<String> PIECE_CODES =
            List.of("wP", "wN", "wB", "wR", "wQ", "wK", "bP", "bN", "bB", "bR", "bQ", "bK");
    private static final Map<String, Texture> TEXTURES = new HashMap<>();

    static {
        PIECE_CODES.forEach(code -> TEXTURES.put(code, new Texture(PieceLoader.load(PieceTheme.CBURNETT, code))));
    }

    private Rectangle hitbox;
    private Square square;

    ClickableSquare(Square square) {
        this.square = square;

        setSize(SIZE, SIZE);

        var x = square.coordinate().fileIndex() * getWidth();
        var y = square.coordinate().rankIndex() * getHeight();
        setPosition(x, y);

        hitbox = new Rectangle(0, 0, getWidth(), getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        var background = square.color().isWhite() ? LIGHT_TEXTURE : DARK_TEXTURE;
        batch.draw(background, getX(), getY(), background.getWidth(), background.getHeight());

        var piece = square.piece();

        if (piece == null) {
            return;
        }

        var texture = TEXTURES.get(piece.color() + piece.firstChar());

        if (texture == null) {
            return;
        }

        batch.setColor(0, 0, 0, 0.15f);
        batch.draw(texture, getX() + 8, getY() + 4, texture.getWidth() + 4, texture.getHeight());

        batch.setColor(Color.WHITE);
        batch.draw(texture, getX() + 4, getY() + 4);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (touchable && hitbox.contains(x, y) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Gdx.app.log("Clicked!", square.toString() + square.coordinate());
            return this;
        }

        return null;
    }

    private static Texture createBackground(String hexCode) {
        var pixmap = new Pixmap(SIZE, SIZE, Pixmap.Format.RGB888);
        pixmap.setColor(Color.valueOf(hexCode));
        pixmap.fill();

        return new Texture(pixmap);
    }
}
