/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.vmardones.tealchess.io.PieceLoader;
import com.vmardones.tealchess.io.PieceTheme;

final class TealChess extends ApplicationAdapter {

    private SpriteBatch batch;
    private List<Texture> textures;

    @Override
    public void create() {
        batch = new SpriteBatch();

        textures = new ArrayList<>();
        textures.add(new Texture(PieceLoader.load(PieceTheme.CBURNETT, "wP")));
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.TEAL);
        batch.begin();
        batch.draw(textures.get(0), 0, 0);
        batch.draw(textures.get(0), 100, 100);
        batch.draw(textures.get(0), 200, 200);
        batch.draw(textures.get(0), 300, 300);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        textures.forEach(Texture::dispose);
    }
}
