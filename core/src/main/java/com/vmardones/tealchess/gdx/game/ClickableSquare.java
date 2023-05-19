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
import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.io.settings.SettingManager;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.square.Coordinate;
import org.eclipse.jdt.annotation.Nullable;

final class ClickableSquare extends Actor {

    private final SettingManager settings;
    private final AssetLoader assets;
    private final Coordinate coordinate;
    private final Color color;
    private @Nullable Piece piece;
    private @Nullable Sprite sprite;

    /* Highlighting settings */
    private boolean showSource;
    private boolean showDestination;
    private boolean showLastMove;
    private boolean showAttacked;
    private boolean showChecked;
    private boolean showDark;
    private boolean showFile;
    private boolean showRank;

    ClickableSquare(
            SettingManager settings, AssetLoader assets, Coordinate coordinate, Color color, @Nullable Piece piece) {
        this.settings = settings;
        this.assets = assets;
        this.coordinate = coordinate;
        this.color = color;
        this.piece = piece;

        setSize(AssetLoader.SQUARE_SIZE, AssetLoader.SQUARE_SIZE);

        var x = coordinate.fileIndex() * getWidth();
        var y = coordinate.rankIndex() * getHeight();
        setPosition(x, y);

        showFile = coordinate.rank() == 1;
        showRank = coordinate.file().equals("h");

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

            if (settings.showAllCoordinates() || showFile) {
                font.draw(batch, coordinate.file(), getX() + padding, getY() + height + padding);
            }

            if (settings.showAllCoordinates() || showRank) {
                font.draw(
                        batch,
                        String.valueOf(coordinate.rank()),
                        getX() + getWidth() - height - 2,
                        getY() + getHeight() - padding);
            }
        }

        if (showSource) {
            var tint = assets.texture("source");
            batch.draw(tint, getX(), getY());
        }

        if (settings.showLastMove() && showLastMove) {
            var tint = assets.texture("last_move");
            batch.draw(tint, getX(), getY());
        }

        if (showChecked) {
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

        if (settings.showLegals() && showDestination) {
            var texture = piece == null ? assets.texture("destination") : assets.texture("target");
            batch.draw(texture, getX(), getY());
        }

        if (settings.showAttackedPieces() && !showChecked && showAttacked) {
            var texture = assets.texture("attack");
            batch.draw(texture, getX(), getY());
        }

        if (showDark) {
            var tint = assets.texture("dark_tint");
            batch.draw(tint, getX(), getY());
        }
    }

    /* Getters and setters */

    // TODO: Add back coordinate, as an abstraction that the frontend uses
    Coordinate coordinate() {
        return coordinate;
    }

    @Nullable Piece piece() {
        return piece;
    }

    void reset(@Nullable Piece newPiece) {
        piece(newPiece);
        showSource(false);
        showDestination(false);
        showChecked(false);
        showDark(false);
        showLastMove(false);
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
            showFile = coordinate.rank() == 8;
            showRank = coordinate.file().equals("a");
        } else {
            setPosition(x, y);
            showFile = coordinate.rank() == 1;
            showRank = coordinate.file().equals("h");
        }

        if (sprite != null) {
            sprite.setPosition(getX(), getY());
        }
    }

    void showSource(boolean value) {
        showSource = value;
    }

    void showDestination(boolean value) {
        showDestination = value;
    }

    void showLastMove(boolean value) {
        showLastMove = value;
    }

    void showAttacked(boolean value) {
        showAttacked = value;
    }

    void showChecked(boolean value) {
        showChecked = value;
    }

    void showDark(boolean value) {
        showDark = value;
    }

    void removeSprite() {
        sprite = null;
    }

    private Texture loadTexture(Piece piece) {
        return assets.texture(piece.color().fen() + piece.san());
    }

    private class SquareListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            fire(new SquareEvent(ClickableSquare.this));
        }
    }
}
