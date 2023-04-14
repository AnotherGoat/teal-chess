/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.board.Square;
import cl.vmardones.chess.engine.player.Color;
import cl.vmardones.chess.io.PieceIconLoader;
import cl.vmardones.chess.io.SvgLoader;

class SquarePanel extends JPanel {

    private static final Dimension INITIAL_SIZE = new Dimension(63, 63);
    private static final java.awt.Color LIGHT_COLOR = java.awt.Color.decode("#FFCE9E");
    private static final java.awt.Color DARK_COLOR = java.awt.Color.decode("#D18B47");

    private final transient Table table;
    private transient Square square;
    private final JLabel pieceIconLabel;
    private final JLabel highlightIconLabel;

    SquarePanel(Table table, Square square) {
        super(new BorderLayout());

        this.table = table;
        this.square = square;

        setOpaque(true);
        setPreferredSize(INITIAL_SIZE);

        var layeredPane = new JLayeredPane();

        pieceIconLabel = new IconLabel(null);
        layeredPane.add(pieceIconLabel, JLayeredPane.DEFAULT_LAYER);

        highlightIconLabel = new IconLabel(null);
        layeredPane.add(highlightIconLabel, JLayeredPane.PALETTE_LAYER);

        assignSquareColor();
        assignPieceIcon();
        add(layeredPane, BorderLayout.CENTER);

        validate();

        addComponentListener(new ResizeListener());
        addMouseListener(new ClickListener());
    }

    void drawSquare(Board board) {
        var previousSquare = square;
        square = board.squareAt(square.position());

        if (!square.equals(previousSquare)) {
            assignPieceIcon(getWidth(), getHeight());
        }

        validate();
        repaint();
    }

    /* Getters */

    Position position() {
        return square.position();
    }

    /* Highlighting squares */

    void hideGreenDot() {
        highlightIconLabel.setIcon(null);
    }

    void showGreenDot() {
        var greenDot = SvgLoader.load("art/misc/green_dot.svg", getWidth() / 2, getHeight() / 2);

        if (greenDot != null) {
            highlightIconLabel.setIcon(greenDot);
        }
    }

    private void assignSquareColor() {
        setBackground(square.color() == Color.WHITE ? LIGHT_COLOR : DARK_COLOR);
    }

    private void assignPieceIcon() {
        assignPieceIcon(INITIAL_SIZE.width, INITIAL_SIZE.height);
    }

    private void assignPieceIcon(int width, int height) {
        pieceIconLabel.setIcon(null);

        var piece = square.piece();

        if (piece != null) {
            var icon = PieceIconLoader.load(piece, width, height);

            if (icon != null) {
                pieceIconLabel.setIcon(icon);
            }
        }
    }

    private final class ResizeListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            if (pieceIconLabel.getIcon() != null) {
                pieceIconLabel.setBounds(0, 0, getWidth(), getHeight());
                assignPieceIcon(getWidth(), getHeight());
            }

            if (highlightIconLabel.getIcon() != null) {
                highlightIconLabel.setBounds(0, 0, getWidth(), getHeight());
                showGreenDot();
            }

            setPreferredSize(getSize());
            revalidate();
        }
    }

    private final class ClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (isLeftMouseButton(e)) {
                table.selectionState().onLeftClick(table, square);
            } else if (isRightMouseButton(e)) {
                table.selectionState().onRightClick(table);
            }

            SwingUtilities.invokeLater(table::update);
        }
    }
}
