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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.io.PieceLoader;
import com.vmardones.tealchess.io.PieceTheme;

final class ClickableSquare extends Actor {

    private static final Color LIGHT_COLOR = Color.valueOf("#FFCE9E");
    private static final Color DARK_COLOR = Color.valueOf("#D18B47");
    private static final int SIDE_SIZE = 80;
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

        var x = square.coordinate().fileIndex() * 80;
        var y = (square.coordinate().rank() - 1) * 80;
        hitbox = new Rectangle(x, y, SIDE_SIZE, SIDE_SIZE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        var piece = square.piece();

        if (piece != null) {
            var x = hitbox.x + 4;
            var y = hitbox.y + 4;
            var texture = TEXTURES.get(piece.color() + piece.firstChar());

            batch.setColor(0, 0, 0, 0.1f);
            batch.draw(texture, x + 4, y, 76, 76);

            batch.setColor(Color.WHITE);
            batch.draw(texture, x, y);
        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (touchable && hitbox.contains(x, y) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Gdx.app.log("Clicked!", square.toString() + square.coordinate());
            return this;
        }

        return null;
    }
}
