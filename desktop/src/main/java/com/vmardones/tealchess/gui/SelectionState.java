/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.move.MoveFinder;

interface SelectionState {
    void onLeftClick(Table table, Square pressedSquare);

    void onRightClick(Table table);

    final class NoSelectionState implements SelectionState {
        private static final Logger LOG = LogManager.getLogger(NoSelectionState.class);

        @Override
        public void onLeftClick(Table table, Square pressedSquare) {
            var selectedPiece = pressedSquare.piece();

            if (selectedPiece == null) {
                LOG.warn("The source is empty\n");
                return;
            }

            LOG.debug("The source contains {}", selectedPiece);

            if (selectedPiece.color() == table.game().currentOpponent().color()) {
                LOG.warn("The selected piece belongs to the opponent\n");
                return;
            }

            if (table.game().findLegalMoves(selectedPiece).isEmpty()) {
                LOG.warn("The selected piece has no legal moves\n");
                return;
            }

            if (table.isHighlightLegals()) {
                table.drawLegals(selectedPiece);
            }

            table.selectionState(new SourceSelectionState(selectedPiece.coordinate()));
        }

        @Override
        public void onRightClick(Table table) {
            // Do nothing (for now)
        }

        NoSelectionState() {}

        private NoSelectionState(Table table) {
            if (table.isHighlightLegals()) {
                table.hideHighlights();
            }
        }
    }

    final class SourceSelectionState implements SelectionState {
        private static final Logger LOG = LogManager.getLogger(SourceSelectionState.class);
        private final Coordinate sourceCoordinate;

        @Override
        public void onLeftClick(Table table, Square pressedSquare) {

            if (sourceCoordinate.equals(pressedSquare.coordinate())) {
                LOG.warn("A piece can't be moved to the same coordinate\n");
                table.selectionState(new NoSelectionState(table));
                return;
            }

            LOG.debug("The destination contains {}", pressedSquare.piece());

            var move = MoveFinder.choose(
                    table.game().currentPlayer().legals(), sourceCoordinate, pressedSquare.coordinate());

            if (move == null) {
                LOG.warn("The selected move is illegal\n");
                table.selectionState(new NoSelectionState(table));
                return;
            }

            table.game().updatePosition(move);
            table.selectionState(new NoSelectionState(table));
        }

        @Override
        public void onRightClick(Table table) {
            LOG.warn("Pressed right click, undoing piece selection\n");
            table.selectionState(new NoSelectionState(table));
        }

        private SourceSelectionState(Coordinate sourceCoordinate) {
            this.sourceCoordinate = sourceCoordinate;
        }
    }
}
