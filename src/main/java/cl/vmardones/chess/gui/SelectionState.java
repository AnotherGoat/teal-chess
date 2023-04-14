/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.board.Square;
import cl.vmardones.chess.engine.move.MoveFinder;
import cl.vmardones.chess.engine.move.MoveResult;

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

            if (selectedPiece.alliance() == table.game().currentOpponent().alliance()) {
                LOG.warn("The selected piece belongs to the opponent\n");
                return;
            }

            if (table.isHighlightLegals()) {
                table.drawLegals(selectedPiece);
            }

            table.selectionState(new SourceSelectionState(selectedPiece.position()));
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
        private final Position sourcePosition;

        @Override
        public void onLeftClick(Table table, Square pressedSquare) {

            if (sourcePosition.equals(pressedSquare.position())) {
                LOG.warn("A piece can't be moved to the same position\n");
                table.selectionState(new NoSelectionState(table));
                return;
            }

            LOG.debug("The destination contains {}", pressedSquare.piece());

            var move =
                    MoveFinder.choose(table.game().currentPlayer().legals(), sourcePosition, pressedSquare.position());

            LOG.debug("Is the move possible? {}\n", move != null);

            if (move == null) {
                table.selectionState(new NoSelectionState(table));
                return;
            }

            var status = table.testMove(move);

            if (status == MoveResult.CONTINUE) {
                table.game().addTurn(move);
            }

            table.selectionState(new NoSelectionState(table));
        }

        @Override
        public void onRightClick(Table table) {
            LOG.warn("Pressed right click, undoing piece selection\n");
            table.selectionState(new NoSelectionState(table));
        }

        private SourceSelectionState(Position sourcePosition) {
            this.sourcePosition = sourcePosition;
        }
    }
}
