/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

public final class Window {

    private boolean darkTheme;
    private boolean highlightLegals;
    private boolean flipBoard;

    public Window(boolean debugMode, boolean darkTheme, boolean highlightLegals, boolean flipBoard) {
        if (debugMode) {
            // TODO: Remove log4j2 dependency and move all the logging outside the core
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        this.darkTheme = darkTheme;
        this.highlightLegals = highlightLegals;
        this.flipBoard = flipBoard;

        var configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Teal Chess");
        configuration.setInitialBackgroundColor(Color.TEAL);
        configuration.setWindowedMode(1280, 720);
        configuration.setResizable(false);
        configuration.useVsync(true);
        configuration.setForegroundFPS(60);
        configuration.disableAudio(true);

        new Lwjgl3Application(new TealChess(), configuration);
    }
}
