/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.move.MoveFinder;
import org.lwjgl.opengl.GL20;

final class TealChess extends ApplicationAdapter {

    private boolean debugMode;
    private Game game;
    private Stage stage;
    private BoardGroup boardGroup;
    private SelectionState selectionState = new SourceSelection();

    TealChess(boolean debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public void create() {
        if (debugMode) {
            // TODO: Remove log4j2 dependency and move all the logging outside the core
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        game = new Game();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        boardGroup = new BoardGroup(game.board());
        stage.addActor(boardGroup);

        stage.addListener(new SquareListener());
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

    private final class SquareListener implements EventListener {
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

    private final class SourceSelection implements SelectionState {
        @Override
        public void select(Square square) {
            var piece = square.piece();

            if (piece == null) {
                Gdx.app.debug("Source", "The source is empty\n");
                return;
            }

            Gdx.app.log("Source", "The source contains " + piece);

            if (piece.color() == game.currentOpponent().color()) {
                Gdx.app.debug("Source", "The selected piece belongs to the opponent\n");
                return;
            }

            if (game.findLegalMoves(piece).isEmpty()) {
                Gdx.app.debug("Source", "The selected piece has no legal moves\n");
                return;
            }

            selectionState = new DestinationSelection(piece.coordinate());
        }

        @Override
        public void unselect() {
            // No need to do anything in this state, because nothing has been selected yet
        }

        private SourceSelection() {}
    }

    private final class DestinationSelection implements SelectionState {

        private final Coordinate sourceCoordinate;

        @Override
        public void select(Square square) {
            if (sourceCoordinate.equals(square.coordinate())) {
                Gdx.app.debug("Destination", "A piece can't be moved to the same coordinate\n");
                selectionState = new SourceSelection();
                return;
            }

            Gdx.app.log("Destination", "The destination contains " + square.piece());

            var move = MoveFinder.choose(game.currentPlayer().legals(), sourceCoordinate, square.coordinate());

            if (move == null) {
                Gdx.app.debug("Destination", "The selected move is illegal\n");
                selectionState = new SourceSelection();
                return;
            }

            game.updatePosition(move);
            selectionState = new SourceSelection();

            boardGroup.board(game.board());
            boardGroup.act(1);
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
}
