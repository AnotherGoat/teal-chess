/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private static final String LOG_TAG = "Key";
    private final GameScreen screen;
    private final SettingManager settings;
    private final AssetLoader assets;
    private final PieceAnimator animator;

    public KeyListener(GameScreen screen, SettingManager settings, AssetLoader assets, PieceAnimator animator) {
        this.screen = screen;
        this.settings = settings;
        this.assets = assets;
        this.animator = animator;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        return switch (keycode) {
            case Input.Keys.D -> {
                settings.toggleDebugMode();
                Gdx.app.log(LOG_TAG, "Toggling debug mode to " + settings.debugMode());

                if (settings.debugMode()) {
                    Gdx.app.setLogLevel(Application.LOG_DEBUG);
                } else {
                    Gdx.app.setLogLevel(Application.LOG_INFO);
                }
                yield true;
            }
            case Input.Keys.L -> {
                settings.toggleShowLegals();
                Gdx.app.log(LOG_TAG, "Toggling legal move highlighting to " + settings.showLegals());
                yield true;
            }
            case Input.Keys.F -> {
                settings.toggleFlipBoard();
                Gdx.app.log(LOG_TAG, "Toggling flip board to " + settings.flipBoard());

                screen.flipChessboard();

                if (animator.hasAnimations()) {
                    animator.stopAnimations();
                    screen.playNextTurn();
                }
                yield true;
            }
            case Input.Keys.M -> {
                settings.toggleShowLastMove();
                Gdx.app.log(LOG_TAG, "Toggling last move highlighting to " + settings.showLastMove());
                yield true;
            }
            case Input.Keys.C -> {
                settings.toggleShowCoordinates();
                Gdx.app.log(LOG_TAG, "Toggling show coordinates to " + settings.showCoordinates());
                yield true;
            }
            case Input.Keys.K -> {
                settings.toggleShowAllCoordinates();
                Gdx.app.log(LOG_TAG, "Toggling show all coordinates to " + settings.showAllCoordinates());
                yield true;
            }
            case Input.Keys.P -> {
                settings.toggleShowAttackedPieces();
                Gdx.app.log(LOG_TAG, "Toggling show attacked pieces to " + settings.showAttackedPieces());
                yield true;
            }
            case Input.Keys.N -> {
                Gdx.app.log(LOG_TAG, "Starting a new game!");
                screen.startNewGame();
                yield true;
            }
            case Input.Keys.T -> {
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                        || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)
                        || Gdx.input.isKeyPressed(Input.Keys.META_SHIFT_ON)) {
                    settings.previousBoardTheme();
                    Gdx.app.log(LOG_TAG, "Changing to previous board theme: " + settings.boardTheme());
                } else {
                    settings.nextBoardTheme();
                    Gdx.app.log(LOG_TAG, "Changing to next board theme: " + settings.boardTheme());
                }

                assets.reloadBoardTheme();
                yield true;
            }
            case Input.Keys.S -> {
                settings.togglePieceShadows();
                Gdx.app.log(LOG_TAG, "Toggling piece shadows to " + settings.pieceShadows());
                yield true;
            }
            case Input.Keys.A -> {
                settings.toggleAnimatePieces();
                Gdx.app.log(LOG_TAG, "Toggling piece animations to " + settings.animatePieces());

                if (!settings.animatePieces()) {
                    animator.stopAnimations();
                    screen.playNextTurn();
                }

                yield true;
            }
            case Input.Keys.I -> {
                settings.toggleInvisiblePieces();
                Gdx.app.log(LOG_TAG, "Toggling invisible pieces to " + settings.invisiblePieces());
                yield true;
            }
            case Input.Keys.DPAD_UP -> {
                settings.decreaseAnimationDuration();
                Gdx.app.log(LOG_TAG, "Sped up animations to " + settings.animationDuration() + "s");
                yield true;
            }
            case Input.Keys.DPAD_DOWN -> {
                settings.increaseAnimationDuration();
                Gdx.app.log(LOG_TAG, "Slowed down animations to " + settings.animationDuration() + "s");
                yield true;
            }
            case Input.Keys.DPAD_RIGHT -> {
                settings.increaseAiDelay();
                Gdx.app.log(LOG_TAG, "Increased AI delay to " + settings.aiDelay() + "s");
                yield true;
            }
            case Input.Keys.DPAD_LEFT -> {
                settings.decreaseAiDelay();
                Gdx.app.log(LOG_TAG, "Decreased AI delay to " + settings.aiDelay() + "s");
                yield true;
            }
            case Input.Keys.F9 -> {
                var fileName = formattedDateTime() + ".fen";
                var fullPath = PlainTextExporter.save(fileName, screen.exportFen());
                Gdx.app.log(LOG_TAG, "FEN exported as " + fullPath);
                yield true;
            }
            case Input.Keys.F10 -> {
                var fileName = formattedDateTime() + ".pgn";
                var fullPath = PlainTextExporter.save(fileName, screen.exportPgn());
                Gdx.app.log(LOG_TAG, "PGN exported as " + fullPath);
                yield true;
            }
            case Input.Keys.F11 -> {
                var screenshotName = "Board - " + formattedDateTime();
                var fullPath = screen.screenshotBoard(screenshotName);
                Gdx.app.log(LOG_TAG, "Board screenshot saved as " + fullPath);
                yield true;
            }
            case Input.Keys.F12 -> {
                var screenshotName = "Full - " + formattedDateTime();
                var fullPath = ScreenshotTaker.save(screenshotName);
                Gdx.app.log(LOG_TAG, "Screenshot saved as " + fullPath);
                yield true;
            }
            default -> false;
        };
    }

    private String formattedDateTime() {
        return DATE_FORMATTER.format(LocalDateTime.now());
    }
}
