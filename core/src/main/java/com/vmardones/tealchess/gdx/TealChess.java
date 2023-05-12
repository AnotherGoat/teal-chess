/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.vmardones.tealchess.gdx.game.GameScreen;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.io.settings.SettingManager;

final class TealChess extends Game {

    private final SettingManager settings = new SettingManager();
    private final AssetLoader assets = new AssetLoader(settings);

    @Override
    public void create() {
        settings.load();

        if (settings.debugMode()) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        assets.load();

        setScreen(new GameScreen(settings, assets));
    }

    @Override
    public void dispose() {
        assets.dispose();
        super.dispose();
    }
}
