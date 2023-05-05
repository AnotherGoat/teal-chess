/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import java.util.List;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.vmardones.tealchess.ai.RandomMoveChooser;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.io.AssetLoader;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.MoveFinder;

final class GameScreen extends ScreenAdapter {

    private final AssetLoader assetLoader;
    private final GameLogger gameLogger;
    private boolean debugMode;
    private boolean highlightLegals;
    private boolean flipBoard;
    private Game game;
    private final Stage stage;
    private BoardGroup boardGroup;
    private SelectionState selectionState;
    private PromotionGroup promotionGroup;
    private List<LegalMove> promotionMoves;

    GameScreen(AssetLoader assetLoader, boolean debugMode, boolean highlightLegals, boolean flipBoard) {
        this.assetLoader = assetLoader;
        gameLogger = new GameLogger();
        this.debugMode = debugMode;
        this.highlightLegals = highlightLegals;
        this.flipBoard = flipBoard;

        Gdx.app.log("Game", "Game started!\n");
        game = new Game().blackAi(new RandomMoveChooser());
        gameLogger.log(game);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        boardGroup = new BoardGroup(assetLoader, game.board());
        if (flipBoard) {
            boardGroup.flip(flipBoard);
        }

        stage.addActor(boardGroup);

        selectionState = new SourceSelection();
        stage.addListener(new SquareListener());
        stage.addListener(new KeyListener());
        stage.addListener(new PromotionListener());

        if (game.ai() != null) {
            playAiTurn();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act(delta);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void playAiTurn() {
        boardGroup.setTouchable(Touchable.disabled);

        Gdx.app.log("AI", "The AI is choosing a move!");

        var task = new Timer.Task() {
            @Override
            public void run() {
                var aiMove = game.makeAiMove();
                boardGroup.highlightMove(aiMove);
                boardGroup.hideChecked();

                if (game.kingAttacked()) {
                    boardGroup.highlightChecked(game.king().coordinate());
                }

                boardGroup.board(game.board());
                gameLogger.log(game);
                boardGroup.setTouchable(Touchable.enabled);

                if (game.ai() != null) {
                    playAiTurn();
                }
            }
        };

        Timer.schedule(task, 0.75f);
    }

    private class SquareListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (event instanceof SquareEvent squareEvent) {
                selectionState.select(squareEvent);
                return true;
            }

            if (event instanceof ClearSelectionEvent) {
                selectionState.unselect();
                return true;
            }

            return false;
        }
    }

    private interface SelectionState {
        void select(SquareEvent event);

        void unselect();
    }

    private class SourceSelection implements SelectionState {

        private static final String LOG_TAG = "Source";

        @Override
        public void select(SquareEvent event) {
            var piece = event.square().piece();

            if (piece == null) {
                Gdx.app.debug(LOG_TAG, "The source is empty\n");
                return;
            }

            Gdx.app.log(LOG_TAG, "The source contains " + piece);

            if (piece.color() == game.oppponent().color()) {
                Gdx.app.debug(LOG_TAG, "The selected piece belongs to the opponent\n");
                return;
            }

            var legalDestinations = game.findLegalDestinations(piece);

            if (legalDestinations.isEmpty()) {
                Gdx.app.debug(LOG_TAG, "The selected piece has no legal moves\n");
                return;
            }

            boardGroup.highlightSource(event.square().coordinate());

            if (highlightLegals) {
                boardGroup.highlightDestinations(legalDestinations);
            }

            selectionState = new DestinationSelection(event);
        }

        @Override
        public void unselect() {
            // No need to do anything in this state, because nothing has been selected yet
        }

        private SourceSelection() {
            boardGroup.hideSource();

            if (highlightLegals) {
                boardGroup.hideDestinations();
            }
        }
    }

    private class DestinationSelection implements SelectionState {

        private static final String LOG_TAG = "Destination";
        private final SquareEvent sourceEvent;

        @Override
        public void select(SquareEvent event) {
            var square = event.square();
            var sourceCoordinate = sourceEvent.square().coordinate();

            if (sourceCoordinate.equals(square.coordinate())) {
                Gdx.app.debug(LOG_TAG, "A piece can't be moved to the same coordinate\n");
                selectionState = new SourceSelection();
                return;
            }

            if (square.piece() == null) {
                Gdx.app.log(LOG_TAG, "The destination is empty");
            } else {
                Gdx.app.log(LOG_TAG, "The destination contains " + square.piece());
            }

            var moves = MoveFinder.choose(game.player().legals(), sourceCoordinate, square.coordinate());

            if (moves.isEmpty()) {
                Gdx.app.debug(LOG_TAG, "The selected move is illegal\n");
                selectionState = new SourceSelection();
                return;
            }

            if (moves.size() == 1) {
                Gdx.app.log(LOG_TAG, "Legal move found! Updating the chessboard...\n");
                var move = moves.get(0);
                game.makeMove(move);

                selectionState = new SourceSelection();
                boardGroup.highlightMove(move);

                boardGroup.hideChecked();
                if (game.kingAttacked()) {
                    boardGroup.highlightChecked(game.king().coordinate());
                }

                var sprite = sourceEvent.sprite();

                if (sprite != null) {
                    sourceEvent.removeSprite();

                    var x1 = boardGroup.getX() + sourceEvent.x() + 4;
                    var y1 = boardGroup.getY() + sourceEvent.y() + 4;
                    var x2 = boardGroup.getX() + event.x() + 4;
                    var y2 = boardGroup.getY() + event.y() + 4;

                    var image = new Image(sprite);
                    image.setPosition(x1, y1);

                    var slide = Actions.moveTo(x2, y2, 0.3f, Interpolation.smooth);
                    var resumeGame = Actions.run(() -> {
                        boardGroup.board(game.board());
                        gameLogger.log(game);

                        if (game.ai() != null && !game.player().legals().isEmpty()) {
                            playAiTurn();
                        }
                    });
                    var remove = new RemoveActorAction();

                    var fullAction = Actions.sequence(slide, resumeGame, remove);

                    image.addAction(fullAction);
                    stage.addActor(image);
                }

                return;
            }

            // TODO: Handle promotion choices
            Gdx.app.log(LOG_TAG, "Promoting a pawn! Please select the piece you want to promote it to\n");

            boardGroup.dark(true);
            boardGroup.setTouchable(Touchable.disabled);
            promotionMoves = moves;

            var x = boardGroup.getX() + event.x();
            var y = boardGroup.getY() + event.y();
            promotionGroup = new PromotionGroup(assetLoader, game.position().sideToMove(), x, y);
            stage.addActor(promotionGroup);
        }

        @Override
        public void unselect() {
            Gdx.app.debug(LOG_TAG, "Pressed right click, undoing piece selection\n");
            selectionState = new SourceSelection();
        }

        private DestinationSelection(SquareEvent sourceEvent) {
            this.sourceEvent = sourceEvent;
        }
    }

    private class KeyListener extends InputListener {
        private static final String LOG_TAG = "Key";

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (keycode == Input.Keys.D) {
                Gdx.app.log(LOG_TAG, "Toggling debug mode\n");
                debugMode = !debugMode;

                if (debugMode) {
                    Gdx.app.setLogLevel(Application.LOG_DEBUG);
                } else {
                    Gdx.app.setLogLevel(Application.LOG_INFO);
                }

                return true;
            } else if (keycode == Input.Keys.H) {
                Gdx.app.log(LOG_TAG, "Toggling legal move highlighting\n");
                highlightLegals = !highlightLegals;
                // TODO: Handle hiding or showing legals when a piece is already selected
                return true;
            } else if (keycode == Input.Keys.F) {
                Gdx.app.log(LOG_TAG, "Flipping the board\n");
                flipBoard = !flipBoard;
                boardGroup.flip(flipBoard);
                return true;
            }

            return false;
        }
    }

    private class PromotionListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof PromotionEvent promotionEvent)) {
                return false;
            }

            promotionGroup.remove();

            var choice = promotionEvent.promotionChoice();
            var selectedMove = promotionMoves.stream()
                    .filter(legal -> legal.move().promotionChoice() == choice)
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("Unreachable statement"));

            game.makeMove(selectedMove);
            boardGroup.highlightMove(selectedMove);

            boardGroup.hideChecked();
            if (game.kingAttacked()) {
                boardGroup.highlightChecked(game.king().coordinate());
            }

            boardGroup.dark(false);
            boardGroup.setTouchable(Touchable.enabled);

            selectionState = new SourceSelection();
            boardGroup.board(game.board());
            gameLogger.log(game);

            if (game.ai() != null && !game.player().legals().isEmpty()) {
                playAiTurn();
            }

            return true;
        }
    }
}
