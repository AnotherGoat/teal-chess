/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.piece.PromotionChoice;
import com.vmardones.tealchess.player.Color;

final class PromotionGroup extends Group {

    private static final int WIDTH = AssetLoader.SQUARE_SIZE * PromotionChoice.values().length;

    PromotionGroup(AssetLoader assets, Color color, float x, float y) {
        setSize(WIDTH, AssetLoader.SQUARE_SIZE);
        setPosition(x, y);

        for (var choice : PromotionChoice.values()) {
            addActor(new PromotionOption(assets, choice, color));
        }
    }
}
