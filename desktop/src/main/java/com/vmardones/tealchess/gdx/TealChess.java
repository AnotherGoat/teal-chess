/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.vmardones.tealchess.game.Game;
import org.lwjgl.opengl.GL20;

final class TealChess extends ApplicationAdapter {

    private Game game;
    private Stage stage;

    @Override
    public void create() {
        game = new Game();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        stage.addActor(new BoardGroup(game.board()));

        stage.addListener(new SquareListener());
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
