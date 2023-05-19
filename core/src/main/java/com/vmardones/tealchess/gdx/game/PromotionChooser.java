/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.piece.PromotionChoice;

final class PromotionChooser extends Group {

    private static final int WIDTH = AssetLoader.SQUARE_SIZE * PromotionChoice.values().length;

    PromotionChooser(AssetLoader assets, Color color, float x, float y, List<Move> promotionMoves) {
        setSize(WIDTH, AssetLoader.SQUARE_SIZE);
        setPosition(x, y);

        for (var choice : PromotionChoice.values()) {
            var selectedMove = promotionMoves.stream()
                    .filter(legal -> legal.promotionChoice() == choice)
                    .findFirst()
                    .orElseThrow(AssertionError::new);

            addActor(new PromotionOption(assets, choice, color, selectedMove));
        }
    }
}
