/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.*;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;

class BoardPanel extends JPanel {

    public static final Dimension INITIAL_SIZE = new Dimension(500, 500);

    private final transient Table table;
    private final List<SquarePanel> squares;

    BoardPanel(Table table) {
        super(new GridLayout(8, 8));
        this.table = table;

        squares = new ArrayList<>();

        IntStream.range(Board.MIN_SQUARES, Board.MAX_SQUARES)
                .mapToObj(Coordinate::of)
                .map(coordinate -> new SquarePanel(table, coordinate))
                .forEach(squarePanel -> {
                    squares.add(squarePanel);
                    add(squarePanel);
                });

        setPreferredSize(INITIAL_SIZE);
        validate();
    }

    void drawBoard(Board board) {
        removeAll();

        table.getBoardDirection().traverse(squares).forEach(squarePanel -> {
            squarePanel.drawSquare(board);
            add(squarePanel);
        });

        validate();
        repaint();
    }
}
