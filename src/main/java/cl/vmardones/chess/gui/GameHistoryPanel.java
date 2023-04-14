/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.player.Color;
import org.eclipse.jdt.annotation.Nullable;

class GameHistoryPanel extends JPanel {

    private static final Dimension INITIAL_SIZE = new Dimension(150, 400);
    public static final int ROW_HEIGHT = 25;
    private final DataModel model;
    private final JScrollPane scrollPane;
    private @Nullable Move lastMove;

    GameHistoryPanel() {
        super(new BorderLayout());
        model = new DataModel();

        var table = new JTable(model);
        table.setRowHeight(ROW_HEIGHT);
        table.setDefaultRenderer(Object.class, new CenteredRenderer());

        scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(INITIAL_SIZE);

        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    void draw(@Nullable Move newLastMove) {

        if (newLastMove != null && !Objects.equals(lastMove, newLastMove)) {
            lastMove = newLastMove;

            var moveText = lastMove.toString();

            if (lastMove.piece().color() == Color.WHITE) {
                model.setValueAt(moveText, model.getLastRowIndex() + 1, 0);
            } else {
                model.setValueAt(moveText, model.getLastRowIndex(), 1);
            }

            var vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        }
    }

    void reset() {
        model.clear();
    }

    private static final class DataModel extends DefaultTableModel {

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            if (row > getLastRowIndex()) {
                addRow(new Vector<>(List.of(aValue, "")));
            } else {
                super.setValueAt(aValue, row, column);
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        private DataModel() {
            super(new Vector<>(List.of("White", "Black")), 0);
        }

        private void clear() {
            dataVector = new Vector<>();
        }

        private int getLastRowIndex() {
            return getRowCount() - 1;
        }
    }

    private static final class CenteredRenderer extends DefaultTableCellRenderer {
        private CenteredRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }
}
