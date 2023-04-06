/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.board.Square;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveFinder;
import cl.vmardones.chess.engine.move.MoveStatus;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Alliance;
import cl.vmardones.chess.io.PieceIconLoader;
import cl.vmardones.chess.io.SvgLoader;
import org.eclipse.jdt.annotation.Nullable;

class SquarePanel extends JPanel {

    private static final Logger LOG = LogManager.getLogger(SquarePanel.class);
    private static final Dimension INITIAL_SIZE = new Dimension(60, 60);
    private static final Color LIGHT_COLOR = Color.decode("#FFCE9E");
    private static final Color DARK_COLOR = Color.decode("#D18B47");

    private static final Integer PIECE_LAYER = 1;
    private static final Integer HIGHLIGHT_LAYER = 2;

    private final transient Table table;
    private JLayeredPane layeredPane;
    private final transient Coordinate coordinate;

    SquarePanel(Table table, Coordinate coordinate) {
        super(new BorderLayout());

        this.table = table;
        this.coordinate = coordinate;

        setOpaque(true);
        setPreferredSize(INITIAL_SIZE);

        layeredPane = createLayeredPane();

        assignSquareColor();
        assignPieceIcon(table.squareAt(coordinate));
        add(layeredPane, BorderLayout.CENTER);

        validate();

        addMouseListener(clickListener());
    }

    private JLayeredPane createLayeredPane() {
        var layeredPane = new JLayeredPane();

        layeredPane.setLayout(new BorderLayout());
        layeredPane.setOpaque(true);

        return layeredPane;
    }

    private MouseListener clickListener() {

        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isLeftMouseButton(e)) {
                    if (table.getSourceSquare() == null) {
                        firstLeftClick();
                    } else {
                        secondLeftClick();
                    }
                } else if (isRightMouseButton(e)) {
                    table.resetSelection();
                    LOG.debug("Pressed right click, unselecting");
                }

                SwingUtilities.invokeLater(table::update);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Do nothing
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Do nothing
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Do nothing
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Do nothing
            }
        };
    }

    private void firstLeftClick() {
        LOG.debug("Selected the square {}", coordinate);
        table.setSourceSquare(table.squareAt(coordinate));

        if (getSelectedPiece() != null) {
            table.setSelectedPiece(getSelectedPiece());
            LOG.debug("The square contains {}", table.getSelectedPiece());
            LOG.debug("Highlighting legal moves");
        } else {
            LOG.debug("The square is unoccupied, unselecting");
            table.resetSelection();
        }
    }

    private @Nullable Piece getSelectedPiece() {
        return table.getSourceSquare().piece();
    }

    private void secondLeftClick() {
        // TODO: Replace all these long method calls with forwarding methods
        LOG.debug("Selected the destination {}", coordinate);
        table.setDestinationSquare(table.squareAt(coordinate));

        var move = MoveFinder.choose(
                table.getGame().getCurrentPlayer().legals(),
                table.getSourceSquare().coordinate(),
                table.getDestinationSquare().coordinate());

        LOG.debug("Is there a move that can get to the destination? {}", move != null);

        if (move != null) {
            var moveTransition = table.makeMove(move);

            if (moveTransition.moveStatus() == MoveStatus.DONE) {
                table.getGame().createNextTurn(move);
                table.addToLog(move);
            }
        }

        table.resetSelection();
    }

    void drawSquare(Board board) {
        layeredPane = createLayeredPane();
        assignPieceIcon(table.squareAt(coordinate));
        highlightLegals(board);
        add(layeredPane, BorderLayout.CENTER);
        validate();
        repaint();
    }

    private void assignSquareColor() {
        setBackground(coordinate.color() == Alliance.WHITE ? LIGHT_COLOR : DARK_COLOR);
    }

    private void assignPieceIcon(Square square) {
        removeAll();

        if (square.piece() != null) {
            var icon = PieceIconLoader.load(square.piece(), INITIAL_SIZE.width, INITIAL_SIZE.height);

            if (icon != null) {
                addImage(icon, PIECE_LAYER);
            }
        }
    }

    private void addImage(BufferedImage bufferedImage, Integer layer) {
        var image = new JLabel(new ImageIcon(bufferedImage));

        layeredPane.add(image, BorderLayout.CENTER, layer);
    }

    private void highlightLegals(Board board) {
        if (table.isHighlightLegals()) {
            selectedPieceLegals(board).stream()
                    .filter(move -> move.destination().equals(coordinate))
                    .forEach(move -> {
                        var greenDot = SvgLoader.load(
                                "art/misc/green_dot.svg", INITIAL_SIZE.width / 2, INITIAL_SIZE.height / 2);

                        if (greenDot != null) {
                            addImage(greenDot, HIGHLIGHT_LAYER);
                        }
                    });
        }
    }

    private List<Move> selectedPieceLegals(Board board) {
        if (table.getSelectedPiece() == null || isOpponentPieceSelected()) {
            return Collections.emptyList();
        }

        return table.getSelectedPiece().calculateLegals(board);
    }

    private boolean isOpponentPieceSelected() {
        return table.getSelectedPiece().alliance()
                != table.getGame().getCurrentPlayer().alliance();
    }
}
