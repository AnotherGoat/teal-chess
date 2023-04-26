/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.*;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.move.MoveFinder;
import com.vmardones.tealchess.player.Color;
import org.lwjgl.opengl.GL20;

final class TealChess extends ApplicationAdapter {

    private boolean debugMode;
    private boolean highlightLegals;
    private boolean flipBoard;
    private Game game;
    private Stage stage;
    private BoardGroup boardGroup;
    private SelectionState selectionState;

    TealChess(boolean debugMode, boolean highlightLegals, boolean flipBoard) {
        this.debugMode = debugMode;
        this.highlightLegals = highlightLegals;
        this.flipBoard = flipBoard;
    }

    @Override
    public void create() {
        if (debugMode) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        Gdx.app.log("Game", "Game started!\n");
        game = new Game();
        logCurrentPosition();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        boardGroup = new BoardGroup(game.board());
        if (flipBoard) {
            boardGroup.flip(flipBoard);
        }

        stage.addActor(boardGroup);

        selectionState = new SourceSelection();
        stage.addListener(new SquareListener());

        stage.addListener(new KeyListener());
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    // TODO: Move this method somewhere else
    private void logCurrentPosition() {
        var board = game.board();
        var position = game.position();
        var sideToMove = position.sideToMove();
        var player = game.player();
        var opponent = game.oppponent();
        var history = game.history();

        Gdx.app.debug("Game", "Current chessboard:\n" + board.unicode());

        Gdx.app.debug("Game", "White king: " + board.king(Color.WHITE));
        Gdx.app.debug("Game", "White pieces: " + board.pieces(Color.WHITE));
        Gdx.app.debug("Game", "Black king: " + board.king(Color.BLACK));
        Gdx.app.debug("Game", "Black pieces: " + board.pieces(Color.BLACK));
        Gdx.app.debug("Game", "Castling rights: " + position.castlingRights().fen());
        Gdx.app.debug("Game", "En passant pawn: " + position.enPassantTarget());

        Gdx.app.debug("Game", "Players: " + player + " vs. " + opponent);
        Gdx.app.log("Game", sideToMove + "'s turn!");
        Gdx.app.debug("Game", "Legal moves: " + player.legals());

        Gdx.app.debug("Game", "Move history: " + history.moves());

        switch (game.player().status()) {
            case NORMAL -> Gdx.app.log("Game", "The game continues like normal...\n");
            case CHECKED -> Gdx.app.log("Game", "Check! " + sideToMove + " king is in danger!\n");
            case CHECKMATED -> Gdx.app.log("Game", "Checkmate! " + sideToMove.opposite() + " player won!\n");
            case STALEMATED -> Gdx.app.log("Game", "Stalemate! The game ends in a draw!\n");
        }
    }

    private class SquareListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (event instanceof SquareEvent squareEvent) {
                selectionState.select(squareEvent.square());
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
        void select(Square square);

        void unselect();
    }

    private class SourceSelection implements SelectionState {
        @Override
        public void select(Square square) {
            var piece = square.piece();

            if (piece == null) {
                Gdx.app.debug("Source", "The source is empty\n");
                return;
            }

            Gdx.app.log("Source", "The source contains " + piece);

            if (piece.color() == game.oppponent().color()) {
                Gdx.app.debug("Source", "The selected piece belongs to the opponent\n");
                return;
            }

            var legalDestinations = game.findLegalDestinations(piece);

            if (legalDestinations.isEmpty()) {
                Gdx.app.debug("Source", "The selected piece has no legal moves\n");
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

        private final Coordinate sourceCoordinate;

        @Override
        public void select(Square square) {
            if (sourceCoordinate.equals(square.coordinate())) {
                Gdx.app.debug("Destination", "A piece can't be moved to the same coordinate\n");
                selectionState = new SourceSelection();
                return;
            }

            if (square.piece() == null) {
                Gdx.app.log("Destination", "The destination is empty");
            } else {
                Gdx.app.log("Destination", "The destination contains " + square.piece());
            }

            var moves = MoveFinder.choose(game.player().legals(), sourceCoordinate, square.coordinate());

            if (moves.isEmpty()) {
                Gdx.app.debug("Destination", "The selected move is illegal\n");
                selectionState = new SourceSelection();
                return;
            }

            if (moves.size() == 1) {
                Gdx.app.log("Destination", "Legal move found! Updating the chessboard...\n");
                game.updatePosition(moves.get(0));
                selectionState = new SourceSelection();

                boardGroup.board(game.board());
                logCurrentPosition();
                return;
            }

            // TODO: Handle promotion choices
            Gdx.app.log("Destination", "Promoting a pawn! Please select the piece you want to promote it to\n");

            var choice = MathUtils.random(3);

            game.updatePosition(moves.get(choice));
            selectionState = new SourceSelection();

            boardGroup.board(game.board());
            logCurrentPosition();
        }

        @Override
        public void unselect() {
            Gdx.app.debug("Destination", "Pressed right click, undoing piece selection\n");
            selectionState = new SourceSelection();
        }

        private DestinationSelection(Coordinate sourceCoordinate) {
            this.sourceCoordinate = sourceCoordinate;
        }
    }

    private class KeyListener extends InputListener {
        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (keycode == Input.Keys.D) {
                Gdx.app.log("Key", "Toggling debug mode\n");
                debugMode = !debugMode;

                if (debugMode) {
                    Gdx.app.setLogLevel(Application.LOG_DEBUG);
                } else {
                    Gdx.app.setLogLevel(Application.LOG_INFO);
                }

                return true;
            } else if (keycode == Input.Keys.H) {
                Gdx.app.log("Key", "Toggling legal move highlighting\n");
                highlightLegals = !highlightLegals;
                // TODO: Handle hiding or showing legals when a piece is already selected
                return true;
            } else if (keycode == Input.Keys.F) {
                Gdx.app.log("Key", "Flipping the board\n");
                flipBoard = !flipBoard;
                boardGroup.flip(flipBoard);
                return true;
            }

            return false;
        }
    }
}
