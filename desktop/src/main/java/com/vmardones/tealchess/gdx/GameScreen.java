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
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Timer;
import com.vmardones.tealchess.ai.RandomMoveChooser;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.io.AssetLoader;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.MoveFinder;
import org.lwjgl.opengl.GL11;

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
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);
        stage.draw();
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
                game.makeAiMove();
                boardGroup.board(game.board());
                gameLogger.log(game);
                boardGroup.setTouchable(Touchable.enabled);
            }
        };

        Timer.schedule(task, 0.5f);
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

            if (highlightLegals) {
                boardGroup.highlightSquares(legalDestinations);
            }

            selectionState = new DestinationSelection(piece.coordinate());
        }

        @Override
        public void unselect() {
            // No need to do anything in this state, because nothing has been selected yet
        }

        private SourceSelection() {
            if (highlightLegals) {
                boardGroup.hideHighlights();
            }
        }
    }

    private class DestinationSelection implements SelectionState {

        private static final String LOG_TAG = "Destination";
        private final Coordinate sourceCoordinate;

        @Override
        public void select(SquareEvent event) {
            var square = event.square();

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
                game.makeMove(moves.get(0));

                selectionState = new SourceSelection();
                boardGroup.board(game.board());
                gameLogger.log(game);

                if (game.ai() != null && !game.player().legals().isEmpty()) {
                    playAiTurn();
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

        private DestinationSelection(Coordinate sourceCoordinate) {
            this.sourceCoordinate = sourceCoordinate;
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
