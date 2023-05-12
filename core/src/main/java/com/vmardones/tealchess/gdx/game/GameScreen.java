/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Timer;
import com.vmardones.tealchess.ai.RandomMoveChooser;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.io.settings.SettingManager;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.MoveFinder;
import com.vmardones.tealchess.move.MoveMaker;
import org.eclipse.jdt.annotation.Nullable;

public final class GameScreen extends ScreenAdapter {

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
    private final Chessboard board;
    private final PieceAnimator animator;
    private final SquareSelector squareSelector;
    private @Nullable PromotionChooser promotionChooser;

    public GameScreen(SettingManager settings, AssetLoader assets) {
        this.settings = settings;
        this.assets = assets;
        logger = new GameLogger(settings);

        game = createNewGame();

        board = new Chessboard(settings, assets, game.board());

        if (settings.flipBoard()) {
            board.flip(true);
        }

        stage.addActor(board);

        animator = new PieceAnimator(settings, board);
        stage.addActor(animator);

        squareSelector = new SquareSelector(board);
        stage.addActor(squareSelector);

        stage.addListener(new KeyListener(this, settings, assets, animator));
        stage.addListener(new SquareListener());
        stage.addListener(new AskPromotionListener());
        stage.addListener(new PromotionListener());
        stage.addListener(new SimpleEventListener());
        stage.addListener(new MoveEventListener());

        Gdx.input.setInputProcessor(stage);
        playFirstTurn();
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

    void startNewGame() {
        animator.stopAnimations();
        removePromotionSelector();

        game = createNewGame();
        board.reset(game.board());

        playFirstTurn();
    }

    void playNextTurn() {
        board.setTouchable(Touchable.enabled);
        board.update(game.board());
        logger.log(game);

        if (game.isAiTurn() && game.hasLegalMoves()) {
            playAiMove();
        }
    }

    void flipChessboard() {
        board.flip(settings.flipBoard());
    }

    private Game createNewGame() {
        return new Game(new MoveMaker(), new MoveFinder(), INITIAL_TAGS).blackAi(new RandomMoveChooser());
    }

    void playFirstTurn() {
        squareSelector.resetState();
        board.setTouchable(Touchable.enabled);

        Gdx.app.log("Game", "Game started!");
        logger.log(game);

        if (game.isAiTurn() && game.hasLegalMoves()) {
            playAiMove();
        }
    }

    private void playAiMove() {
        board.setTouchable(Touchable.disabled);

        Gdx.app.log("AI", "The AI is choosing a move!");
        var aiMove = game.chooseAiMove();

        var task = new Timer.Task() {
            @Override
            public void run() {
                if (game.isAiTurn()) {
                    game.makeMove(aiMove);
                    board.highlightMove(aiMove);
                    board.hideChecked();

                    if (game.isKingAttacked()) {
                        board.highlightChecked(game.kingCoordinate());
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
            playNextTurn();
            return;
        }

        var moveAnimation = animator.animateMove(move);
        if (moveAnimation != null) {
            stage.addActor(moveAnimation);
        }

        if (game.isCastling(move)) {
            var castleAnimation = animator.animateCastle(move.move());

            if (castleAnimation != null) {
                stage.addActor(castleAnimation);
            }
        }
    }

    private void removePromotionSelector() {
        if (promotionChooser != null) {
            promotionChooser.remove();
            promotionChooser = null;
        }
    }

    private class SquareListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (event instanceof SquareEvent squareEvent) {
                squareSelector.select(game, squareEvent);
                return true;
            }

            return false;
        }
    }

    private class AskPromotionListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof AskPromotionEvent askPromotionEvent)) {
                return false;
            }

            board.makeDark(true);
            board.setTouchable(Touchable.disabled);

            var square = askPromotionEvent.square();

            var x = board.getX() + square.getX();
            var y = board.getY() + square.getY();
            var promotionMoves = askPromotionEvent.promotionMoves();

            promotionChooser = new PromotionChooser(assets, game.sideToMove(), x, y, promotionMoves);
            stage.addActor(promotionChooser);

            return true;
        }
    }

    private class PromotionListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof PromotionEvent promotionEvent)) {
                return false;
            }

            removePromotionSelector();

            var selectedMove = promotionEvent.move();
            game.makeMove(selectedMove);
            board.highlightMove(selectedMove);

            board.hideChecked();
            if (game.isKingAttacked()) {
                board.highlightChecked(game.kingCoordinate());
            }

            board.makeDark(false);
            squareSelector.resetState();

            playSlidingAnimation(selectedMove);
            return true;
        }
    }

    private class SimpleEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof SimpleEvent simpleEvent)) {
                return false;
            }

            switch (simpleEvent.type()) {
                case NEXT_TURN -> playNextTurn();
                case CLEAR_SELECTION -> squareSelector.resetState();
            }

            return true;
        }
    }

    private class MoveEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof MoveEvent moveEvent)) {
                return false;
            }

            playSlidingAnimation(moveEvent.move());
            return true;
        }
    }
}
