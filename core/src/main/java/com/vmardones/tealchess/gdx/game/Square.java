/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.io.settings.SettingManager;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;

final class Square extends Actor {

    private final SettingManager settings;
    private final AssetLoader assets;
    private final Coordinate coordinate;
    private final Color color;
    private @Nullable Piece piece;
    private @Nullable Sprite sprite;
    private boolean source;
    private boolean destination;
    private boolean lastMove;
    private boolean attacked;
    private boolean checked;
    private boolean dark;
    private boolean file;
    private boolean rank;

    Square(SettingManager settings, AssetLoader assets, Coordinate coordinate, Color color, @Nullable Piece piece) {
        this.settings = settings;
        this.assets = assets;
        this.coordinate = coordinate;
        this.color = color;
        this.piece = piece;

        setSize(AssetLoader.SQUARE_SIZE, AssetLoader.SQUARE_SIZE);

        var x = coordinate.fileIndex() * getWidth();
        var y = coordinate.rankIndex() * getHeight();
        setPosition(x, y);

        file = coordinate.rank() == 1;
        rank = coordinate.file().equals("h");

        if (piece != null) {
            sprite = new Sprite(loadTexture(piece));
            sprite.setPosition(x, y);
        }

        addListener(new SquareListener());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        var background = color.isWhite() ? assets.texture("light") : assets.texture("dark");
        batch.draw(background, getX(), getY());

        if (settings.showCoordinates()) {
            var font = color.isWhite() ? assets.font("dark_font") : assets.font("light_font");

            var height = font.getCapHeight();
            var padding = 6;

            if (settings.showAllCoordinates() || file) {
                font.draw(batch, coordinate.file(), getX() + padding, getY() + height + padding);
            }

            if (settings.showAllCoordinates() || rank) {
                font.draw(
                        batch,
                        String.valueOf(coordinate.rank()),
                        getX() + getWidth() - height - 2,
                        getY() + getHeight() - padding);
            }
        }

        if (source) {
            var tint = assets.texture("source");
            batch.draw(tint, getX(), getY());
        }

        if (settings.showLastMove() && lastMove) {
            var tint = assets.texture("last_move");
            batch.draw(tint, getX(), getY());
        }

        if (checked) {
            var tint = assets.texture("check");
            batch.draw(tint, getX(), getY());
        }

        if (!settings.invisiblePieces() && sprite != null) {
            var texture = sprite.getTexture();

            if (settings.pieceShadows()) {
                batch.setColor(0, 0, 0, 0.15f);
                batch.draw(texture, getX() + 2, getY(), (float) texture.getWidth() + 4, texture.getHeight());
                batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
            }

            sprite.draw(batch);
        }

        if (settings.showLegals() && destination) {
            var texture = piece == null ? assets.texture("destination") : assets.texture("target");
            batch.draw(texture, getX(), getY());
        }

        if (settings.showAttackedPieces() && !checked && attacked) {
            var texture = assets.texture("attack");
            batch.draw(texture, getX(), getY());
        }

        if (dark) {
            var tint = assets.texture("dark_tint");
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
        source(false);
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
            setPosition(7 * getWidth() - x, 7 * getHeight() - y);
            file = coordinate.rank() == 8;
            rank = coordinate.file().equals("a");
        } else {
            setPosition(x, y);
            file = coordinate.rank() == 1;
            rank = coordinate.file().equals("h");
        }

        if (sprite != null) {
            sprite.setPosition(getX(), getY());
        }
    }

    void source(boolean value) {
        source = value;
    }

    void destination(boolean value) {
        destination = value;
    }

    void move(boolean value) {
        lastMove = value;
    }

    void attacked(boolean value) {
        attacked = value;
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
        return assets.texture(piece.color().fen() + piece.firstChar());
    }

    private class SquareListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            fire(new SquareEvent(Square.this));
        }
    }
}
