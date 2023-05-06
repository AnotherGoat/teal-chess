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
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.io.AssetLoader;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.MoveFinder;

final class GameScreen extends ScreenAdapter {

    private static final float ANIMATION_SPEED = 0.3f;
    private static final float AI_DELAY = 0.75f;

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

        Gdx.app.log("Game", "Game started!");
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
            playAiMove();
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

    private void playAiMove() {
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

                playSlidingAnimation(aiMove);
            }
        };

        Timer.schedule(task, AI_DELAY);
    }

    private void playSlidingAnimation(LegalMove move) {
        var source = move.source();
        var destination = move.destination();
        var sourceSquare = boardGroup.squareAt(source);
        var destinationSquare = boardGroup.squareAt(destination);

        var sprite = sourceSquare.sprite();

        if (sprite != null) {
            sourceSquare.removeSprite();

            var x1 = boardGroup.getX() + sourceSquare.getX();
            var y1 = boardGroup.getY() + sourceSquare.getY();
            var x2 = boardGroup.getX() + destinationSquare.getX();
            var y2 = boardGroup.getY() + destinationSquare.getY();

            var image = new Image(sprite);
            image.setPosition(x1, y1);

            var slide = Actions.moveTo(x2, y2, ANIMATION_SPEED, Interpolation.smoother);
            var resumeGame = Actions.run(() -> {
                boardGroup.setTouchable(Touchable.enabled);
                boardGroup.board(game.board());
                gameLogger.log(game);

                if (game.ai() != null && !game.player().legals().isEmpty()) {
                    playAiMove();
                }
            });
            var fullAction = Actions.sequence(slide, resumeGame, new RemoveActorAction());

            image.addAction(fullAction);
            stage.addActor(image);

            if (move.isCastling()) {
                var castle = move.move();
                var rook = castle.otherPiece();
                var rookDestination = castle.rookDestination();

                if (rook == null || rookDestination == null) {
                    throw new AssertionError("Unreachable statement");
                }

                var rookSource = rook.coordinate();

                var rookSourceSquare = boardGroup.squareAt(rookSource);
                var rookDestinationSquare = boardGroup.squareAt(rookDestination);

                var rookSprite = rookSourceSquare.sprite();

                if (rookSprite != null) {
                    rookSourceSquare.removeSprite();

                    var rookX1 = boardGroup.getX() + rookSourceSquare.getX();
                    var rookY1 = boardGroup.getY() + rookSourceSquare.getY();
                    var rookX2 = boardGroup.getX() + rookDestinationSquare.getX();
                    var rookY2 = boardGroup.getY() + rookDestinationSquare.getY();

                    var rookImage = new Image(rookSprite);
                    rookImage.setPosition(rookX1, rookY1);

                    var rookSlide = Actions.moveTo(rookX2, rookY2, ANIMATION_SPEED, Interpolation.smoother);
                    var rookAction = Actions.sequence(rookSlide, new RemoveActorAction());

                    rookImage.addAction(rookAction);
                    stage.addActor(rookImage);
                }
            }
        }
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
                Gdx.app.debug(LOG_TAG, "The source is empty");
                return;
            }

            Gdx.app.log(LOG_TAG, "The source contains " + piece);

            if (piece.color() == game.oppponent().color()) {
                Gdx.app.debug(LOG_TAG, "The selected piece belongs to the opponent");
                return;
            }

            var legalDestinations = game.findLegalDestinations(piece);

            if (legalDestinations.isEmpty()) {
                Gdx.app.debug(LOG_TAG, "The selected piece has no legal moves");
                return;
            }

            var coordinate = event.square().coordinate();
            boardGroup.highlightSource(coordinate);

            if (highlightLegals) {
                boardGroup.highlightDestinations(legalDestinations);
            }

            selectionState = new DestinationSelection(coordinate);
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
        private final Coordinate sourceCoordinate;

        @Override
        public void select(SquareEvent event) {
            var square = event.square();

            if (sourceCoordinate.equals(square.coordinate())) {
                Gdx.app.debug(LOG_TAG, "A piece can't be moved to the same coordinate");
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
                Gdx.app.debug(LOG_TAG, "The selected move is illegal");
                selectionState = new SourceSelection();
                return;
            }

            if (moves.size() == 1) {
                Gdx.app.log(LOG_TAG, "Legal move found! Updating the chessboard...");
                var move = moves.get(0);
                game.makeMove(move);

                selectionState = new SourceSelection();
                boardGroup.highlightMove(move);

                boardGroup.hideChecked();
                if (game.kingAttacked()) {
                    boardGroup.highlightChecked(game.king().coordinate());
                }

                playSlidingAnimation(move);
                return;
            }

            Gdx.app.log(LOG_TAG, "Promoting a pawn! Please select the piece you want to promote it to");

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
            Gdx.app.debug(LOG_TAG, "Pressed right click, undoing piece selection");
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
                Gdx.app.log(LOG_TAG, "Toggling debug mode");
                debugMode = !debugMode;

                if (debugMode) {
                    Gdx.app.setLogLevel(Application.LOG_DEBUG);
                } else {
                    Gdx.app.setLogLevel(Application.LOG_INFO);
                }

                return true;
            } else if (keycode == Input.Keys.H) {
                Gdx.app.log(LOG_TAG, "Toggling legal move highlighting");
                highlightLegals = !highlightLegals;
                // TODO: Handle hiding or showing legals when a piece is already selected
                return true;
            } else if (keycode == Input.Keys.F) {
                Gdx.app.log(LOG_TAG, "Flipping the board");
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
            selectionState = new SourceSelection();

            playSlidingAnimation(selectedMove);
            return true;
        }
    }
}
