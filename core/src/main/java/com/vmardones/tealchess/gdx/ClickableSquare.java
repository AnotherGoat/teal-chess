/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.io.AssetLoader;
import com.vmardones.tealchess.piece.Piece;
import org.eclipse.jdt.annotation.Nullable;

final class ClickableSquare extends Actor {

    private final AssetLoader assetLoader;
    private Square square;
    private @Nullable Sprite sprite;
    private boolean highlight;
    private boolean destination;
    private boolean lastMove;
    private boolean checked;
    private boolean dark;

    ClickableSquare(AssetLoader assetLoader, Square square) {
        this.assetLoader = assetLoader;
        this.square = square;

        setSize(AssetLoader.SQUARE_SIZE, AssetLoader.SQUARE_SIZE);

        var x = square.coordinate().fileIndex() * getWidth();
        var y = square.coordinate().rankIndex() * getHeight();
        setPosition(x, y);

        var piece = square.piece();
        if (piece != null) {
            sprite = new Sprite(loadTexture(piece));
            sprite.setPosition(x + 4, y + 4);
        }

        addListener(new SquareListener());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        var background = square.color().isWhite()
                ? assetLoader.get("light.png", Texture.class)
                : assetLoader.get("dark.png", Texture.class);
        batch.draw(background, getX(), getY());

        if (highlight) {
            var tint = assetLoader.get("highlight.png", Texture.class);
            batch.draw(tint, getX(), getY());
        }

        if (lastMove) {
            var tint = assetLoader.get("last_move.png", Texture.class);
            batch.draw(tint, getX(), getY());
        }

        if (checked) {
            var tint = assetLoader.get("check.png", Texture.class);
            batch.draw(tint, getX(), getY());
        }

        if (sprite != null) {
            var texture = sprite.getTexture();

            batch.setColor(0, 0, 0, 0.15f);
            batch.draw(texture, getX() + 8, getY() + 4, (float) texture.getWidth() + 4, texture.getHeight());

            batch.setColor(Color.WHITE);
            sprite.draw(batch);
        }

        if (destination) {
            var texture = square.piece() == null
                    ? assetLoader.get("destination.png", Texture.class)
                    : assetLoader.get("target.png", Texture.class);
            batch.draw(texture, getX(), getY());
        }

        if (dark) {
            var tint = assetLoader.get("dark_tint.png", Texture.class);
            batch.draw(tint, getX(), getY());
        }
    }

    /* Getters and setters */

    Square square() {
        return square;
    }

    @Nullable Sprite sprite() {
        return sprite;
    }

    void square(Square value) {
        square = value;

        var piece = square.piece();

        if (piece == null) {
            sprite = null;
        } else {
            sprite = new Sprite(loadTexture(piece));
            sprite.setPosition(getX() + 4, getY() + 4);
        }
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

    void highlight(boolean value) {
        highlight = value;
    }

    void destination(boolean value) {
        destination = value;
    }

    void move(boolean value) {
        lastMove = value;
    }

    void checked(boolean value) {
        checked = value;
    }

    void dark(boolean value) {
        dark = value;
    }

    Coordinate coordinate() {
        return square.coordinate();
    }

    void removeSprite() {
        sprite = null;
    }

    private Texture loadTexture(Piece piece) {
        return assetLoader.get(piece.color().fen() + piece.firstChar() + ".png", Texture.class);
    }

    private class SquareListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            fire(new SquareEvent(ClickableSquare.this));
        }
    }
}
