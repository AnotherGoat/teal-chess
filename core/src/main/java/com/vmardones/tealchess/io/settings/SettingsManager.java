/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.settings;

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

    public boolean showLegals() {
        return get(BooleanSetting.SHOW_LEGALS);
    }

    public void toggleShowLegals() {
        toggle(BooleanSetting.SHOW_LEGALS);
    }

    public boolean flipBoard() {
        return get(BooleanSetting.FLIP_BOARD);
    }

    public void toggleFlipBoard() {
        toggle(BooleanSetting.FLIP_BOARD);
    }

    public float animationDuration() {
        return get(FloatSetting.ANIMATION_DURATION);
    }

    public void increaseAnimationDuration() {
        increase(FloatSetting.ANIMATION_DURATION);
    }

    public void decreaseAnimationDuration() {
        decrease(FloatSetting.ANIMATION_DURATION);
    }

    public float aiDelay() {
        return get(FloatSetting.AI_DELAY);
    }

    public void increaseAiDelay() {
        increase(FloatSetting.AI_DELAY);
    }

    public void decreaseAiDelay() {
        decrease(FloatSetting.AI_DELAY);
    }

    public String pgn() {
        return get(StringSetting.PGN);
    }

    public void pgn(String value) {
        set(StringSetting.PGN, value);
    }

    public String fen() {
        return get(StringSetting.FEN);
    }

    public void fen(String value) {
        set(StringSetting.FEN, value);
    }

    private boolean get(BooleanSetting setting) {
        return preferences.getBoolean(setting.key(), setting.defaultValue());
    }

    private String get(StringSetting setting) {
        return preferences.getString(setting.key(), setting.defaultValue());
    }

    private float get(FloatSetting setting) {
        return preferences.getFloat(setting.key(), setting.defaultValue());
    }

    private void set(BooleanSetting setting, boolean value) {
        preferences.putBoolean(setting.key(), value);
        preferences.flush();
    }

    private void set(StringSetting setting, String value) {
        preferences.putString(setting.key(), value);
        preferences.flush();
    }

    private void set(FloatSetting setting, float value) {
        preferences.putFloat(setting.key(), value);
        preferences.flush();
    }

    private void toggle(BooleanSetting setting) {
        set(setting, !get(setting));
    }

    private void increase(FloatSetting setting) {
        var oldValue = get(setting);
        var newValue = setting.clamp(oldValue + setting.step());

        set(setting, newValue);
    }

    private void decrease(FloatSetting setting) {
        var oldValue = get(setting);
        var newValue = setting.clamp(oldValue - setting.step());

        set(setting, newValue);
    }
}
