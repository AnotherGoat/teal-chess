/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.square.Coordinate;

final class SquareSelector extends Actor {

    private final Chessboard board;
    private SelectionState selectionState;

    SquareSelector(Chessboard board) {
        this.board = board;
        resetState();
    }

    void resetState() {
        selectionState = new SourceSelection();
    }

    void select(Game game, SquareEvent event) {
        selectionState.select(game, event);
    }

    void unselect() {
        Gdx.app.debug("Square", "Pressed right click, undoing piece selection");
        resetState();
    }

    interface SelectionState {
        void select(Game game, SquareEvent event);
    }

    private class SourceSelection implements SelectionState {

        private static final String LOG_TAG = "Source";

        @Override
        public void select(Game game, SquareEvent event) {
            var square = event.square();
            var piece = square.piece();

            if (piece == null) {
                Gdx.app.debug(LOG_TAG, "The source is empty");
                return;
            }

            Gdx.app.log(LOG_TAG, "The source contains " + piece);

            if (game.isOpponentPiece(piece)) {
                Gdx.app.debug(LOG_TAG, "The selected piece belongs to the opponent");
                return;
            }

            var legalDestinations = game.findLegalDestinations(Coordinate.forSquare(piece.square()));

            if (legalDestinations.isEmpty()) {
                Gdx.app.debug(LOG_TAG, "The selected piece has no legal moves");
                return;
            }

            var source = square.coordinate();
            board.showSource(source);
            board.showDestinations(legalDestinations);

            selectionState = new DestinationSelection(source);
        }

        private SourceSelection() {
            board.hideSource();
            board.hideDestinations();
        }
    }

    private class DestinationSelection implements SelectionState {

        private static final String LOG_TAG = "Destination";
        private final Coordinate source;

        @Override
        public void select(Game game, SquareEvent event) {
            var square = event.square();
            var destination = square.coordinate();

            if (source.equals(destination)) {
                Gdx.app.debug(LOG_TAG, "A piece can't be moved to the same coordinate");
                resetState();
                return;
            }

            var piece = square.piece();

            if (piece == null) {
                Gdx.app.log(LOG_TAG, "The destination is empty");
            } else {
                Gdx.app.log(LOG_TAG, "The destination contains " + piece);
            }

            var moves = game.findLegalMoves(source, destination);

            if (moves.isEmpty()) {
                Gdx.app.debug(LOG_TAG, "The selected move is illegal");
                resetState();
                return;
            }

            if (moves.size() == 1) {
                Gdx.app.log(LOG_TAG, "Legal move found! Updating the chessboard...");
                var move = moves.get(0);
                game.makeMove(move);

                resetState();
                board.showMove(move);

                board.hideChecked();
                if (game.isKingAttacked()) {
                    board.showChecked(game.kingCoordinate());
                }

                board.hideAttacks();
                board.showAttacks(game.sideToMove(), game.findOpponentAttacks());

                fire(new MoveEvent(move));
                return;
            }

            Gdx.app.log(LOG_TAG, "Promoting a pawn! Please select the piece you want to promote it to");

            fire(new AskPromotionEvent(square, moves));
        }

        private DestinationSelection(Coordinate source) {
            this.source = source;
        }
    }
}
