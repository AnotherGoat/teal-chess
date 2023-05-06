/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public final class Window {

    public Window() {
        var configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Teal Chess");
        configuration.setWindowedMode(1280, 720);
        configuration.setResizable(false);
        configuration.useVsync(true);
        configuration.setForegroundFPS(60);
        configuration.disableAudio(true);

        new Lwjgl3Application(new TealChess(), configuration);
    }
}
