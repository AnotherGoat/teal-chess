/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.vmardones.tealchess.board.Board;

final class BoardGroup extends Group {

    private static final int SIZE = ClickableSquare.SIZE * Board.SIDE_LENGTH;
    private Board board;
    private final List<ClickableSquare> squares = new ArrayList<>();

    BoardGroup(Board board) {
        this.board = board;

        setSize(SIZE, SIZE);

        var x = (Gdx.graphics.getWidth() - getWidth()) / 2;
        var y = (Gdx.graphics.getHeight() - getHeight()) / 2;
        setPosition(x, y);

        board.squares().stream().map(ClickableSquare::new).forEach(square -> {
            squares.add(square);
            addActor(square);
        });
    }

    /* Setters */

    void board(Board value) {
        board = value;
    }
}
