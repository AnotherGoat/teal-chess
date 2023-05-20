/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.io.export.PlainTextExporter;
import com.vmardones.tealchess.io.export.ScreenshotTaker;
import com.vmardones.tealchess.io.settings.SettingManager;

public class KeyListener extends InputListener {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String LOG_TAG = "Input";
    private final GameScreen screen;
    private final SettingManager settings;
    private final AssetLoader assets;
    private final PieceAnimator animator;
    private final Map<Integer, Runnable> mappings;

    public KeyListener(GameScreen screen, SettingManager settings, AssetLoader assets, PieceAnimator animator) {
        this.screen = screen;
        this.settings = settings;
        this.assets = assets;
        this.animator = animator;

        mappings = new HashMap<>();
        fillMappings();
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        var action = mappings.get(keycode);

        if (action == null) {
            return false;
        }

        action.run();
        return true;
    }

    /* Detecting modifier keys */

    private static boolean isShiftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }

    private static boolean isControlPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
    }

    /* Input mappings */

    private void map(int keycode, Runnable action) {
        mappings.put(keycode, action);
    }

    private void fillMappings() {
        // First row (QWERTYUIOP)
        map(Input.Keys.T, this::changeTheme);
        map(Input.Keys.I, this::toggleInvisiblePieces);
        map(Input.Keys.P, this::toggleShowAttackedPieces);

        // Second row (ASDFGHJKL)
        map(Input.Keys.A, this::changeAnimations);
        map(Input.Keys.S, this::togglePieceShadows);
        map(Input.Keys.D, this::toggleDebugMode);
        map(Input.Keys.F, this::flipBoard);
        map(Input.Keys.L, this::toggleShowLegals);

        // Third row (ZXCVBNM)
        map(Input.Keys.C, this::toggleShowCoordinates);
        map(Input.Keys.N, this::startNewGame);
        map(Input.Keys.M, this::toggleShowLastMove);

        // Function keys
        map(Input.Keys.F9, this::exportFen);
        map(Input.Keys.F10, this::exportPgn);
        map(Input.Keys.F11, this::takeBoardScreenshot);
        map(Input.Keys.F12, this::takeFullScreenshot);

        // Other keys
        map(Input.Keys.DPAD_RIGHT, this::increaseAiDelay);
        map(Input.Keys.DPAD_LEFT, this::decreaseAiDelay);
    }

    private void changeTheme() {
        if (isShiftPressed()) {
            settings.previousBoardTheme();
            Gdx.app.log(LOG_TAG, "Changing to previous board theme: " + settings.boardTheme());
        } else {
            settings.nextBoardTheme();
            Gdx.app.log(LOG_TAG, "Changing to next board theme: " + settings.boardTheme());
        }

        assets.reloadBoardTheme();
    }

    private void toggleInvisiblePieces() {
        settings.toggleInvisiblePieces();
        Gdx.app.log(LOG_TAG, "Toggling invisible pieces to " + settings.invisiblePieces());
    }

    private void toggleShowAttackedPieces() {
        settings.toggleShowAttackedPieces();
        Gdx.app.log(LOG_TAG, "Toggling show attacked pieces to " + settings.showAttackedPieces());
    }

    private void changeAnimations() {
        if (isControlPressed()) {
            settings.decreaseAnimationDuration();
            Gdx.app.log(LOG_TAG, "Sped up animations to " + settings.animationDuration() + "s");
            return;
        }

        if (isShiftPressed()) {
            settings.increaseAnimationDuration();
            Gdx.app.log(LOG_TAG, "Slowed down animations to " + settings.animationDuration() + "s");
            return;
        }

        settings.toggleAnimatePieces();
        Gdx.app.log(LOG_TAG, "Toggling piece animations to " + settings.animatePieces());

        if (!settings.animatePieces()) {
            animator.stopAnimations();
            screen.playNextTurn();
        }
    }

    private void togglePieceShadows() {
        settings.togglePieceShadows();
        Gdx.app.log(LOG_TAG, "Toggling piece shadows to " + settings.pieceShadows());
    }

    private void toggleDebugMode() {
        settings.toggleDebugMode();
        Gdx.app.log(LOG_TAG, "Toggling debug mode to " + settings.debugMode());

        if (settings.debugMode()) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        } else {
            Gdx.app.setLogLevel(Application.LOG_INFO);
        }
    }

    private void flipBoard() {
        settings.toggleFlipBoard();
        Gdx.app.log(LOG_TAG, "Toggling flip board to " + settings.flipBoard());

        screen.flipChessboard();

        if (animator.hasAnimations()) {
            animator.stopAnimations();
            screen.playNextTurn();
        }
    }

    private void toggleShowLegals() {
        settings.toggleShowLegals();
        Gdx.app.log(LOG_TAG, "Toggling legal move highlighting to " + settings.showLegals());
    }

    private void toggleShowCoordinates() {
        if (isShiftPressed()) {
            settings.toggleShowAllCoordinates();
            Gdx.app.log(LOG_TAG, "Toggling show all coordinates to " + settings.showAllCoordinates());
            return;
        }

        settings.toggleShowCoordinates();
        Gdx.app.log(LOG_TAG, "Toggling show coordinates to " + settings.showCoordinates());
    }

    private void startNewGame() {
        Gdx.app.log(LOG_TAG, "Starting a new game!");
        screen.startNewGame();
    }

    private void toggleShowLastMove() {
        settings.toggleShowLastMove();
        Gdx.app.log(LOG_TAG, "Toggling last move highlighting to " + settings.showLastMove());
    }

    private void exportFen() {
        var fileName = formattedDateTime() + ".fen";
        var fullPath = PlainTextExporter.save(fileName, screen.exportFen());
        Gdx.app.log(LOG_TAG, "FEN exported as " + fullPath);
    }

    private void exportPgn() {
        var fileName = formattedDateTime() + ".pgn";
        var fullPath = PlainTextExporter.save(fileName, screen.exportPgn());
        Gdx.app.log(LOG_TAG, "PGN exported as " + fullPath);
    }

    private void takeBoardScreenshot() {
        var screenshotName = "Board - " + formattedDateTime();
        var fullPath = screen.screenshotBoard(screenshotName);
        Gdx.app.log(LOG_TAG, "Board screenshot saved as " + fullPath);
    }

    private void takeFullScreenshot() {
        var screenshotName = "Full - " + formattedDateTime();
        var fullPath = ScreenshotTaker.save(screenshotName);
        Gdx.app.log(LOG_TAG, "Screenshot saved as " + fullPath);
    }

    private void increaseAiDelay() {
        settings.increaseAiDelay();
        Gdx.app.log(LOG_TAG, "Increased AI delay to " + settings.aiDelay() + "s");
    }

    private void decreaseAiDelay() {
        settings.decreaseAiDelay();
        Gdx.app.log(LOG_TAG, "Decreased AI delay to " + settings.aiDelay() + "s");
    }

    private String formattedDateTime() {
        return DATE_FORMATTER.format(LocalDateTime.now());
    }
}
