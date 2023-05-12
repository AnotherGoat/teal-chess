/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.piece.PromotionChoice;
import com.vmardones.tealchess.player.Color;

final class PromotionOption extends Actor {

    private final AssetLoader assets;
    private final PromotionChoice choice;
    private final Color color;
    private final LegalMove move;

    PromotionOption(AssetLoader assets, PromotionChoice choice, Color color, LegalMove move) {
        this.assets = assets;
        this.choice = choice;
        this.color = color;
        this.move = move;

        setSize(AssetLoader.SQUARE_SIZE, AssetLoader.SQUARE_SIZE);

        var x = choice.ordinal() * AssetLoader.SQUARE_SIZE;
        var y = 0;
        setPosition(x, y);

        addListener(new OptionListener());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        var background = assets.texture("promotion");
        batch.draw(background, getX(), getY(), background.getWidth(), background.getHeight());

        var piece = assets.texture(color.fen() + choice.san());
        batch.draw(piece, getX(), getY());
    }

    private class OptionListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            fire(new PromotionEvent(move));
        }
    }
}
