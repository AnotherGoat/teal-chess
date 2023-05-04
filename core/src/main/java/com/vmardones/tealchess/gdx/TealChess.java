/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.vmardones.tealchess.io.AssetLoader;
import com.vmardones.tealchess.io.PieceTheme;

final class TealChess extends Game {

    private final AssetLoader assetLoader;
    private final boolean debugMode;
    private final boolean highlightLegals;
    private final boolean flipBoard;

    TealChess(boolean debugMode, boolean highlightLegals, boolean flipBoard) {
        assetLoader = new AssetLoader(PieceTheme.CBURNETT);
        this.debugMode = debugMode;
        this.highlightLegals = highlightLegals;
        this.flipBoard = flipBoard;
    }

    @Override
    public void create() {
        if (debugMode) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        assetLoader.reload();

        setScreen(new GameScreen(assetLoader, debugMode, highlightLegals, flipBoard));
    }
}
