/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;

final class Square extends Actor {

    private final AssetLoader assets;
    private final Coordinate coordinate;
    private final Color color;
    private @Nullable Piece piece;
    private @Nullable Sprite sprite;
    private boolean highlight;
    private boolean destination;
    private boolean lastMove;
    private boolean checked;
    private boolean dark;

    Square(AssetLoader assets, Coordinate coordinate, Color color, @Nullable Piece piece) {
        this.assets = assets;
        this.coordinate = coordinate;
        this.color = color;
        this.piece = piece;

        setSize(AssetLoader.SQUARE_SIZE, AssetLoader.SQUARE_SIZE);

        var x = coordinate.fileIndex() * getWidth();
        var y = coordinate.rankIndex() * getHeight();
        setPosition(x, y);

        if (piece != null) {
            sprite = new Sprite(loadTexture(piece));
            sprite.setPosition(x, y);
        }

        addListener(new SquareListener());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        var background =
                color.isWhite() ? assets.get("light.png", Texture.class) : assets.get("dark.png", Texture.class);
        batch.draw(background, getX(), getY());

        if (highlight) {
            var tint = assets.get("highlight.png", Texture.class);
            batch.draw(tint, getX(), getY());
        }

        if (lastMove) {
            var tint = assets.get("last_move.png", Texture.class);
            batch.draw(tint, getX(), getY());
        }

        if (checked) {
            var tint = assets.get("check.png", Texture.class);
            batch.draw(tint, getX(), getY());
        }

        if (sprite != null) {
            var texture = sprite.getTexture();

            batch.setColor(0, 0, 0, 0.15f);
            batch.draw(texture, getX() + 2, getY(), (float) texture.getWidth() + 4, texture.getHeight());

            batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
            sprite.draw(batch);
        }

        if (destination) {
            var texture = piece == null
                    ? assets.get("destination.png", Texture.class)
                    : assets.get("target.png", Texture.class);
            batch.draw(texture, getX(), getY());
        }

        if (dark) {
            var tint = assets.get("dark_tint.png", Texture.class);
            batch.draw(tint, getX(), getY());
        }
    }

    /* Getters and setters */

    Coordinate coordinate() {
        return coordinate;
    }

    @Nullable Piece piece() {
        return piece;
    }

    void reset(@Nullable Piece newPiece) {
        piece(newPiece);
        highlight(false);
        destination(false);
        checked(false);
        dark(false);
        move(false);
    }

    void piece(@Nullable Piece value) {
        piece = value;

        if (piece == null) {
            sprite = null;
        } else {
            sprite = new Sprite(loadTexture(piece));
            sprite.setPosition(getX(), getY());
        }
    }

    @Nullable Sprite sprite() {
        return sprite;
    }

    void flip(boolean flip) {
        var x = coordinate.fileIndex() * getWidth();
        var y = coordinate.rankIndex() * getHeight();

        if (flip) {
            x = 7 * getWidth() - x;
            y = 7 * getHeight() - y;
        }

        setPosition(x, y);

        if (sprite != null) {
            sprite.setPosition(x, y);
        }
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

    void removeSprite() {
        sprite = null;
    }

    private Texture loadTexture(Piece piece) {
        return assets.get(piece.color().fen() + piece.firstChar() + ".png", Texture.class);
    }

    private class SquareListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            fire(new SquareEvent(Square.this));
        }
    }
}
