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

class BoardPanel extends JPanel {

    public static final Dimension INITIAL_SIZE = new Dimension(500, 500);

    private Board board;
    private final transient Table table;
    private final List<SquarePanel> squares;

    BoardPanel(Table table, Board board) {
        super(new GridLayout(8, 8));
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

        table.getBoardDirection().traverse(squares).forEach(squarePanel -> {
            squarePanel.drawSquare(board);
            add(squarePanel);
        });

        validate();
        repaint();
    }
}
