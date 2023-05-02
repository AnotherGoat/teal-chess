/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.io.AssetLoader;

final class ClickableSquare extends Actor {

    private final AssetLoader assetLoader;
    private Square square;
    private boolean highlight;

    ClickableSquare(AssetLoader assetLoader, Square square) {
        this.assetLoader = assetLoader;
        this.square = square;

        setSize(AssetLoader.SQUARE_SIZE, AssetLoader.SQUARE_SIZE);

        var x = square.coordinate().fileIndex() * getWidth();
        var y = square.coordinate().rankIndex() * getHeight();
        setPosition(x, y);

        addListener(new SquareListener());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        var background = square.color().isWhite()
                ? assetLoader.get("light.png", Texture.class)
                : assetLoader.get("dark.png", Texture.class);
        batch.draw(background, getX(), getY(), background.getWidth(), background.getHeight());

        var piece = square.piece();

        if (piece != null) {
            var texture = assetLoader.get(piece.color().fen() + piece.firstChar() + ".png", Texture.class);

            batch.setColor(0, 0, 0, 0.15f);
            batch.draw(texture, getX() + 8, getY() + 4, (float) texture.getWidth() + 4, texture.getHeight());

            batch.setColor(Color.WHITE);
            batch.draw(texture, getX() + 4, getY() + 4);
        }

        if (highlight) {
            var texture = assetLoader.get("highlight.png", Texture.class);

            batch.draw(texture, getX(), getY());
        }
    }

    /* Getters and setters */

    Square square() {
        return square;
    }

    void square(Square value) {
        square = value;

        clearListeners();
        addListener(new SquareListener());
    }

    void flip(boolean flip) {
        var x = square.coordinate().fileIndex() * getWidth();
        var y = square.coordinate().rankIndex() * getHeight();

        if (flip) {
            x = 7 * getWidth() - x;
            y = 7 * getHeight() - y;
        }

        setPosition(x, y);
    }

    void highlight(boolean highlight) {
        this.highlight = highlight;
    }

    Coordinate coordinate() {
        return square.coordinate();
    }

    private class SquareListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            fire(new SquareEvent(square, getX(), getY()));
        }
    }
}
