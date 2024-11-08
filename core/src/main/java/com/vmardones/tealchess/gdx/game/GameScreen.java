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
import com.vmardones.tealchess.evaluation.MaterialEvaluator;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.io.export.ScreenshotTaker;
import com.vmardones.tealchess.io.settings.SettingManager;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.search.NegamaxMoveChooser;
import org.jspecify.annotations.Nullable;

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

    void playFirstTurn() {
        board.setTouchable(game.hasLegalMoves() ? Touchable.enabled : Touchable.disabled);
        squareSelector.resetState();
        board.showAttacks(game.sideToMove(), game.findOpponentAttacks());

        Gdx.app.log("Game", "Game started!");
        logger.log(game);

        if (game.isAiTurn() && game.hasLegalMoves()) {
            playAiMove();
        }
    }

    void startNewGame() {
        animator.stopAnimations();
        removePromotionSelector();

        game = createNewGame();
        board.reset(game.board());

        playFirstTurn();
    }

    void playNextTurn() {
        board.setTouchable(game.hasLegalMoves() ? Touchable.enabled : Touchable.disabled);
        board.update(game.board());
        logger.log(game);

        if (game.isAiTurn() && game.hasLegalMoves()) {
            playAiMove();
        }
    }

    void flipChessboard() {
        board.flip(settings.flipBoard());
    }

    String exportFen() {
        return game.fen();
    }

    String exportPgn() {
        return game.pgn();
    }

    String screenshotBoard(String name) {
        return ScreenshotTaker.save(
                name, (int) board.getX(), (int) board.getY(), (int) board.getWidth(), (int) board.getHeight());
    }

    private Game createNewGame() {
        var blackAi = new NegamaxMoveChooser(new MaterialEvaluator(), 3);
        return new Game(INITIAL_TAGS).blackAi(blackAi);
    }

    private void playAiMove() {
        board.setTouchable(Touchable.disabled);

        Gdx.app.log("AI", "The AI is choosing a move");

        var startTime = System.nanoTime();
        var aiMove = game.chooseAiMove();
        var endTime = System.nanoTime();

        var elapsedTime = (float) (endTime - startTime) / 1e9f;
        Gdx.app.debug("AI", "Took " + elapsedTime + " seconds");

        var task = new Timer.Task() {
            @Override
            public void run() {
                if (game.isAiTurn()) {
                    game.makeMove(aiMove);
                    board.showMove(aiMove);
                    board.hideChecked();

                    if (game.isKingAttacked()) {
                        board.showChecked(game.kingCoordinate());
                    }

                    board.hideAttacks();
                    board.showAttacks(game.sideToMove(), game.findOpponentAttacks());

                    playSlidingAnimation(aiMove);
                }
            }
        };

        Timer.schedule(task, settings.aiDelay());
    }

    private void playSlidingAnimation(Move move) {
        if (!settings.animatePieces()) {
            playNextTurn();
            return;
        }

        var moveAnimation = animator.animateMove(move);
        if (moveAnimation != null) {
            stage.addActor(moveAnimation);
        }

        var castlingStep = game.castlingStep(move);
        if (castlingStep != null) {
            var castleAnimation = animator.animateCastle(castlingStep);

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
            board.showMove(selectedMove);

            board.hideChecked();
            if (game.isKingAttacked()) {
                board.showChecked(game.kingCoordinate());
            }

            board.hideAttacks();
            board.showAttacks(game.sideToMove(), game.findOpponentAttacks());

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
                case CLEAR_SELECTION -> squareSelector.unselect();
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
