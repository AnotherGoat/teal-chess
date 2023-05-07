/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.move.LegalMove;

final class BoardGroup extends Group {

    private static final int SIZE = AssetLoader.SQUARE_SIZE * Board.SIDE_LENGTH;
    private Board board;
    private final Map<Coordinate, ClickableSquare> squares = new HashMap<>();

    BoardGroup(AssetLoader assets, Board board) {
        this.board = board;

        setSize(SIZE, SIZE);

        var x = (Gdx.graphics.getWidth() - getWidth()) / 2;
        var y = (Gdx.graphics.getHeight() - getHeight()) / 2;
        setPosition(x, y);

        board.squares().stream()
                .map(square -> new ClickableSquare(assets, square))
                .forEach(square -> {
                    squares.put(square.coordinate(), square);
                    addActor(square);
                });

        addListener(new ClearListener());
    }

    ClickableSquare squareAt(Coordinate coordinate) {
        var square = squares.get(coordinate);

        if (square == null) {
            throw new AssertionError();
        }

        return square;
    }

    /* Setters */

    void board(Board value) {
        board = value;

        for (var entry : squares.entrySet()) {
            var newSquare = board.squareAt(entry.getKey());
            var clickableSquare = entry.getValue();

            if (!newSquare.equals(clickableSquare.square())) {
                clickableSquare.square(newSquare);
            }
        }
    }

    void flip(boolean flip) {
        squares.values().forEach(square -> square.flip(flip));
    }

    void highlightSource(Coordinate source) {
        for (var square : squares.values()) {
            if (square.coordinate().equals(source)) {
                square.highlight(true);
                break;
            }
        }
    }

    void hideSource() {
        squares.values().forEach(square -> square.highlight(false));
    }

    void highlightDestinations(Set<Coordinate> coordinates) {
        squares.values().forEach(square -> square.destination(coordinates.contains(square.coordinate())));
    }

    void hideDestinations() {
        squares.values().forEach(square -> square.destination(false));
    }

    void highlightChecked(Coordinate coordinate) {
        squares.values().forEach(square -> square.checked(square.coordinate().equals(coordinate)));
    }

    void hideChecked() {
        squares.values().forEach(square -> square.checked(false));
    }

    void dark(boolean value) {
        squares.values().forEach(square -> square.dark(value));
    }

    void highlightMove(LegalMove move) {
        var source = move.source();
        var destination = move.destination();

        for (var square : squares.values()) {
            var isPartOfTheMove =
                    square.coordinate().equals(source) || square.coordinate().equals(destination);
            square.move(isPartOfTheMove);
        }
    }

    private class ClearListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            var button = event.getButton();

            if (button == Input.Buttons.RIGHT) {
                fire(new ClearSelectionEvent());
            }
        }

        private ClearListener() {
            setButton(Input.Buttons.RIGHT);
        }
    }
}
