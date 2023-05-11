/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.*;
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
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.io.settings.SettingManager;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.MoveFinder;
import com.vmardones.tealchess.move.MoveMaker;
import org.eclipse.jdt.annotation.Nullable;

final class GameScreen extends ScreenAdapter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final Map<String, String> INITIAL_TAGS = new LinkedHashMap<>();

    static {
        INITIAL_TAGS.put("Event", "Casual game");
        INITIAL_TAGS.put("Site", "Teal Chess");
        INITIAL_TAGS.put("Date", LocalDate.now().format(DATE_FORMATTER));
        INITIAL_TAGS.put("Round", "?");
        INITIAL_TAGS.put("White", "Anonymous");
        INITIAL_TAGS.put("Black", "Anonymous");
        INITIAL_TAGS.put("Result", "\\*");
    }

    private final AssetLoader assets;
    private final SettingManager settings;
    private final GameLogger logger;
    private Game game;
    private final Stage stage = new Stage();
    private final BoardGroup boardGroup;
    private @Nullable Image moveAnimation;
    private @Nullable Image castleAnimation;
    private SelectionState selectionState;
    private @Nullable PromotionGroup promotionGroup;
    private final List<LegalMove> promotionMoves = new ArrayList<>();

    GameScreen(SettingManager settings, AssetLoader assets, GameLogger logger) {
        this.settings = settings;
        this.assets = assets;
        this.logger = logger;

        Gdx.app.log("Game", "Game started!");
        game = new Game(new MoveMaker(), new MoveFinder(), INITIAL_TAGS).blackAi(new RandomMoveChooser());

        logger.log(game);

        Gdx.input.setInputProcessor(stage);

        boardGroup = new BoardGroup(settings, assets, game.board());

        if (settings.flipBoard()) {
            boardGroup.flip(true);
        }

        stage.addActor(boardGroup);

        selectionState = new SourceSelection();
        stage.addListener(new SquareListener());
        stage.addListener(new KeyListener());
        stage.addListener(new PromotionListener());

        if (game.isAiTurn()) {
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

    // TODO: Check if animations are enabled before using this method
    private void stopAnimations() {
        if (moveAnimation != null) {
            moveAnimation.remove();
            moveAnimation = null;
        }

        if (castleAnimation != null) {
            castleAnimation.remove();
            castleAnimation = null;
        }
    }

    private void removePromotionSelector() {
        if (promotionGroup != null) {
            promotionGroup.remove();
            promotionGroup = null;
        }
    }

    private void playAiMove() {
        boardGroup.setTouchable(Touchable.disabled);

        Gdx.app.log("AI", "The AI is choosing a move!");

        var task = new Timer.Task() {
            @Override
            public void run() {
                if (game.isAiTurn()) {
                    var aiMove = game.makeAiMove();
                    boardGroup.highlightMove(aiMove);
                    boardGroup.hideChecked();

                    if (game.isKingAttacked()) {
                        boardGroup.highlightChecked(game.kingCoordinate());
                    }

                    playSlidingAnimation(aiMove);
                }
            }
        };

        // TODO: Calculate next AI move at the same time, not after the delay is finished
        Timer.schedule(task, settings.aiDelay());
    }

    private void playSlidingAnimation(LegalMove move) {
        if (!settings.animatePieces()) {
            boardGroup.setTouchable(Touchable.enabled);
            boardGroup.board(game.board());
            logger.log(game);

            if (game.isAiTurn() && game.hasLegalMoves()) {
                playAiMove();
            }

            return;
        }

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

            moveAnimation = new Image(sprite);
            moveAnimation.setPosition(x1, y1);

            var slide = Actions.moveTo(x2, y2, settings.animationDuration(), Interpolation.smoother);
            var resumeGame = Actions.run(() -> {
                moveAnimation = null;

                boardGroup.setTouchable(Touchable.enabled);
                boardGroup.board(game.board());
                logger.log(game);

                if (game.isAiTurn() && game.hasLegalMoves()) {
                    playAiMove();
                }
            });
            var fullAction = Actions.sequence(slide, resumeGame, new RemoveActorAction());

            moveAnimation.addAction(fullAction);
            stage.addActor(moveAnimation);

            if (game.isCastling(move)) {
                var castle = move.move();
                var rook = castle.otherPiece();
                var rookDestination = castle.rookDestination();

                if (rook == null || rookDestination == null) {
                    throw new AssertionError();
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

                    castleAnimation = new Image(rookSprite);
                    castleAnimation.setPosition(rookX1, rookY1);

                    var rookSlide =
                            Actions.moveTo(rookX2, rookY2, settings.animationDuration(), Interpolation.smoother);
                    var rookAction = Actions.sequence(
                            rookSlide, Actions.run(() -> castleAnimation = null), new RemoveActorAction());

                    castleAnimation.addAction(rookAction);
                    stage.addActor(castleAnimation);
                }
            }
        }
    }

    private void toggleDebugMode() {
        settings.toggleDebugMode();

        if (settings.debugMode()) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        } else {
            Gdx.app.setLogLevel(Application.LOG_INFO);
        }
    }

    private void flipTheBoard() {
        settings.toggleFlipBoard();

        stopAnimations();
        boardGroup.board(game.board());
        boardGroup.flip(settings.flipBoard());

        logger.log(game);

        boardGroup.setTouchable(Touchable.enabled);

        if (game.isAiTurn() && game.hasLegalMoves()) {
            playAiMove();
        }
    }

    private void startNewGame() {
        stopAnimations();
        removePromotionSelector();
        promotionMoves.clear();

        game = new Game(new MoveMaker(), new MoveFinder(), INITIAL_TAGS).blackAi(new RandomMoveChooser());
        logger.log(game);

        boardGroup.reset(game.board());

        selectionState = new SourceSelection();
        boardGroup.setTouchable(Touchable.enabled);

        if (game.isAiTurn()) {
            playAiMove();
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
            var piece = event.piece();

            if (piece == null) {
                Gdx.app.debug(LOG_TAG, "The source is empty");
                return;
            }

            Gdx.app.log(LOG_TAG, "The source contains " + piece);

            if (game.isOpponentPiece(piece)) {
                Gdx.app.debug(LOG_TAG, "The selected piece belongs to the opponent");
                return;
            }

            var legalDestinations = game.findLegalDestinations(piece);

            if (legalDestinations.isEmpty()) {
                Gdx.app.debug(LOG_TAG, "The selected piece has no legal moves");
                return;
            }

            var source = event.coordinate();
            boardGroup.highlightSource(source);
            boardGroup.highlightDestinations(legalDestinations);

            selectionState = new DestinationSelection(source);
        }

        @Override
        public void unselect() {
            // No need to do anything in this state, because nothing has been selected yet
        }

        private SourceSelection() {
            boardGroup.hideSource();
            boardGroup.hideDestinations();
        }
    }

    private class DestinationSelection implements SelectionState {

        private static final String LOG_TAG = "Destination";
        private final Coordinate source;

        @Override
        public void select(SquareEvent event) {
            var destination = event.coordinate();

            if (source.equals(destination)) {
                Gdx.app.debug(LOG_TAG, "A piece can't be moved to the same coordinate");
                selectionState = new SourceSelection();
                return;
            }

            var piece = event.piece();

            if (piece == null) {
                Gdx.app.log(LOG_TAG, "The destination is empty");
            } else {
                Gdx.app.log(LOG_TAG, "The destination contains " + piece);
            }

            var moves = game.findLegalMoves(source, destination);

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
                if (game.isKingAttacked()) {
                    boardGroup.highlightChecked(game.kingCoordinate());
                }

                playSlidingAnimation(move);
                return;
            }

            Gdx.app.log(LOG_TAG, "Promoting a pawn! Please select the piece you want to promote it to");

            boardGroup.makeDark(true);
            boardGroup.setTouchable(Touchable.disabled);
            promotionMoves.addAll(moves);

            var x = boardGroup.getX() + event.x();
            var y = boardGroup.getY() + event.y();
            promotionGroup = new PromotionGroup(assets, game.sideToMove(), x, y);
            stage.addActor(promotionGroup);
        }

        @Override
        public void unselect() {
            Gdx.app.debug(LOG_TAG, "Pressed right click, undoing piece selection");
            selectionState = new SourceSelection();
        }

        private DestinationSelection(Coordinate source) {
            this.source = source;
        }
    }

    private class KeyListener extends InputListener {
        private static final String LOG_TAG = "Key";

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            return switch (keycode) {
                case Input.Keys.D -> {
                    Gdx.app.log(LOG_TAG, "Toggling debug mode");
                    toggleDebugMode();
                    yield true;
                }
                case Input.Keys.L -> {
                    Gdx.app.log(LOG_TAG, "Toggling legal move highlighting");
                    settings.toggleShowLegals();
                    yield true;
                }
                case Input.Keys.F -> {
                    Gdx.app.log(LOG_TAG, "Flipping the board");
                    flipTheBoard();
                    yield true;
                }
                case Input.Keys.M -> {
                    Gdx.app.log(LOG_TAG, "Toggling last move highlighting");
                    settings.toggleShowLastMove();
                    yield true;
                }
                case Input.Keys.C -> {
                    Gdx.app.log(LOG_TAG, "Toggling show coordinates");
                    settings.toggleShowCoordinates();
                    yield true;
                }
                case Input.Keys.P -> {
                    Gdx.app.log(LOG_TAG, "Toggling show attacked pieces");
                    // TODO: Show attacked pieces
                    settings.toggleShowAttackedPieces();
                    yield true;
                }
                case Input.Keys.N -> {
                    Gdx.app.log(LOG_TAG, "Starting a new game!");
                    startNewGame();
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
                    Gdx.app.log(LOG_TAG, "Toggling piece shadows");
                    settings.togglePieceShadows();
                    yield true;
                }
                case Input.Keys.A -> {
                    Gdx.app.log(LOG_TAG, "Toggling piece animations");
                    settings.toggleAnimatePieces();

                    if (!settings.animatePieces()) {
                        stopAnimations();

                        boardGroup.board(game.board());
                        boardGroup.flip(settings.flipBoard());

                        logger.log(game);

                        boardGroup.setTouchable(Touchable.enabled);

                        if (game.isAiTurn() && game.hasLegalMoves()) {
                            playAiMove();
                        }
                    }

                    yield true;
                }
                case Input.Keys.I -> {
                    Gdx.app.log(LOG_TAG, "Toggling invisible pieces");
                    settings.toggleInvisiblePieces();
                    yield true;
                }
                case Input.Keys.DPAD_UP -> {
                    settings.increaseAnimationDuration();
                    Gdx.app.log(LOG_TAG, "Slowed down animations to " + settings.animationDuration() + "s");
                    yield true;
                }
                case Input.Keys.DPAD_DOWN -> {
                    settings.decreaseAnimationDuration();
                    Gdx.app.log(LOG_TAG, "Sped up animations to " + settings.animationDuration() + "s");
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
                default -> false;
            };
        }
    }

    private class PromotionListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof PromotionEvent promotionEvent)) {
                return false;
            }

            removePromotionSelector();

            var choice = promotionEvent.promotionChoice();
            var selectedMove = promotionMoves.stream()
                    .filter(legal -> legal.move().promotionChoice() == choice)
                    .findFirst()
                    .orElseThrow(AssertionError::new);

            promotionMoves.clear();

            game.makeMove(selectedMove);
            boardGroup.highlightMove(selectedMove);

            boardGroup.hideChecked();
            if (game.isKingAttacked()) {
                boardGroup.highlightChecked(game.kingCoordinate());
            }

            boardGroup.makeDark(false);
            selectionState = new SourceSelection();

            playSlidingAnimation(selectedMove);
            return true;
        }
    }
}
