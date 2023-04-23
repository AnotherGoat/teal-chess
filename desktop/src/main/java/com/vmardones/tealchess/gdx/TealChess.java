/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.vmardones.tealchess.game.Game;

final class TealChess extends ApplicationAdapter {

    private Game game;
    private Stage stage;

    @Override
    public void create() {
        game = new Game();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        var board = game.board();
        board.squares().forEach(square -> stage.addActor(new ClickableSquare(square)));
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.TEAL);

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
