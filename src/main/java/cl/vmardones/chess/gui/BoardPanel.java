/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;

class BoardPanel extends JPanel {

    public static final Dimension INITIAL_SIZE = new Dimension(500, 500);

    private transient Board board;
    private final transient Table table;
    private final List<SquarePanel> squares;

    BoardPanel(Table table, Board board) {
        super(new GridLayout(Board.SIDE_LENGTH, Board.SIDE_LENGTH));
        this.table = table;
        this.board = board;

        squares = new ArrayList<>();

        board.squares().stream().map(square -> new SquarePanel(table, square)).forEach(squarePanel -> {
            squares.add(squarePanel);
            add(squarePanel);
        });

        setPreferredSize(INITIAL_SIZE);
        validate();
    }

    void setBoard(Board board) {
        this.board = board;
    }

    void draw() {
        removeAll();

        table.boardDirection().traverse(squares).forEach(squarePanel -> {
            squarePanel.drawSquare(board);
            add(squarePanel);
        });

        validate();
        repaint();
    }

    void highlightSquares(List<Coordinate> coordinates) {
        squares.forEach(square -> {
            if (coordinates.contains(square.coordinate())) {
                square.showGreenDot();
            } else {
                square.hideGreenDot();
            }
        });
    }

    void hideHighlights() {
        squares.forEach(SquarePanel::hideGreenDot);
    }
}
