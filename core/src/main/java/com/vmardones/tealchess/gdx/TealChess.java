/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.io.assets.PieceTheme;
import com.vmardones.tealchess.io.settings.SettingsManager;

final class TealChess extends Game {

    private final AssetLoader assets = new AssetLoader(PieceTheme.CBURNETT);
    private final SettingsManager settings = new SettingsManager();
    private final GameLogger logger = new GameLogger(settings);

    @Override
    public void create() {
        settings.load();

        if (settings.debugMode()) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        assets.reload();

        setScreen(new GameScreen(assets, settings, logger));
    }

    @Override
    public void dispose() {
        assets.dispose();
        super.dispose();
    }
}
