/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.settings;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.vmardones.tealchess.Initializer;
import com.vmardones.tealchess.io.assets.BoardTheme;
import com.vmardones.tealchess.io.assets.PieceTheme;

// TODO: Add an option to choose between ASCII and Unicode chess pieces
public final class SettingManager {

    private static final String SETTINGS_FILE = "TealChess";
    private Preferences preferences;

    public SettingManager() {}

    @Initializer
    public void load() {
        preferences = Gdx.app.getPreferences(SETTINGS_FILE);
    }

    /* Boolean settings */

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

    public boolean showLastMove() {
        return get(BooleanSetting.SHOW_LAST_MOVE);
    }

    public void toggleShowLastMove() {
        toggle(BooleanSetting.SHOW_LAST_MOVE);
    }

    public boolean showCoordinates() {
        return get(BooleanSetting.SHOW_COORDINATES);
    }

    public void toggleShowCoordinates() {
        toggle(BooleanSetting.SHOW_COORDINATES);
    }

    public boolean showAllCoordinates() {
        return get(BooleanSetting.SHOW_ALL_COORDINATES);
    }

    public void toggleShowAllCoordinates() {
        toggle(BooleanSetting.SHOW_ALL_COORDINATES);
    }

    public boolean showAttackedPieces() {
        return get(BooleanSetting.SHOW_ATTACKED_PIECES);
    }

    public void toggleShowAttackedPieces() {
        toggle(BooleanSetting.SHOW_ATTACKED_PIECES);
    }

    public boolean pieceShadows() {
        return get(BooleanSetting.PIECE_SHADOWS);
    }

    public void togglePieceShadows() {
        toggle(BooleanSetting.PIECE_SHADOWS);
    }

    public boolean animatePieces() {
        return get(BooleanSetting.ANIMATE_PIECES);
    }

    public void toggleAnimatePieces() {
        toggle(BooleanSetting.ANIMATE_PIECES);
    }

    public boolean invisiblePieces() {
        return get(BooleanSetting.INVISIBLE_PIECES);
    }

    public void toggleInvisiblePieces() {
        toggle(BooleanSetting.INVISIBLE_PIECES);
    }

    /* Float settings */

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

    /* String settings */

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

    /* Enum settings */

    public BoardTheme boardTheme() {
        return get(StringSetting.BOARD_THEME, BoardTheme.class);
    }

    public void boardTheme(BoardTheme value) {
        set(StringSetting.BOARD_THEME, value);
    }

    public void previousBoardTheme() {
        boardTheme(boardTheme().previous());
    }

    public void nextBoardTheme() {
        boardTheme(boardTheme().next());
    }

    public PieceTheme pieceTheme() {
        return get(StringSetting.PIECE_THEME, PieceTheme.class);
    }

    public void pieceTheme(PieceTheme value) {
        set(StringSetting.PIECE_THEME, value);
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

    private <E extends Enum<E>> E get(StringSetting setting, Class<E> clazz) {
        var value = preferences.getString(setting.key(), setting.defaultValue());
        return Enum.valueOf(clazz, value.toUpperCase(Locale.ROOT));
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

    private <E extends Enum<E>> void set(StringSetting setting, E value) {
        set(setting, value.name().toLowerCase(Locale.ROOT));
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
