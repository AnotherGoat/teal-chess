/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.vmardones.tealchess.Initializer;

// TODO: Add an option to choose between ASCII and Unicode chess pieces
public final class SettingsManager {

    private static final String SETTINGS_FILE = "TealChess";
    private Preferences preferences;

    public SettingsManager() {}

    @Initializer
    public void load() {
        preferences = Gdx.app.getPreferences(SETTINGS_FILE);
    }

    /* Getters and setters */
    public boolean debugMode() {
        return get(BooleanSetting.DEBUG_MODE);
    }

    public void toggleDebugMode() {
        toggle(BooleanSetting.DEBUG_MODE);
    }

    public boolean highlightLegals() {
        return get(BooleanSetting.HIGHTLIGHT_LEGALS);
    }

    public void toggleHighlightLegals() {
        toggle(BooleanSetting.HIGHTLIGHT_LEGALS);
    }

    public boolean flipBoard() {
        return get(BooleanSetting.FLIP_BOARD);
    }

    public void toggleFlipBoard() {
        toggle(BooleanSetting.FLIP_BOARD);
    }

    private boolean get(BooleanSetting setting) {
        return preferences.getBoolean(setting.key(), setting.defaultValue());
    }

    private void toggle(BooleanSetting setting) {
        set(setting, !get(setting));
    }

    private void set(BooleanSetting setting, boolean value) {
        preferences.putBoolean(setting.key(), value);
        preferences.flush();
    }
}
