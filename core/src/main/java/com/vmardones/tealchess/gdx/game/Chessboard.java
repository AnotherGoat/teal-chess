/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.io.settings.SettingManager;
import com.vmardones.tealchess.move.LegalMove;

final class Chessboard extends Group {

    private static final Event CLEAR_SELECTION = new SimpleEvent(EventType.CLEAR_SELECTION);
    private static final int SIZE = AssetLoader.SQUARE_SIZE * Board.SIDE_LENGTH;
    private Board board;
    private final Map<Coordinate, Square> squares = new HashMap<>();

    Chessboard(SettingManager settings, AssetLoader assets, Board board) {
        this.board = board;

        setSize(SIZE, SIZE);

        var x = (Gdx.graphics.getWidth() - getWidth()) / 2;
        var y = (Gdx.graphics.getHeight() - getHeight()) / 2;
        setPosition(x, y);

        board.mailbox().entrySet().stream()
                .map(entry -> {
                    var coordinate = entry.getKey();
                    var piece = entry.getValue();

                    return new Square(settings, assets, coordinate, board.colorOf(coordinate), piece);
                })
                .forEach(square -> {
                    squares.put(square.coordinate(), square);
                    addActor(square);
                });

        addListener(new ClearListener());
    }

    Square squareAt(Coordinate coordinate) {
        var square = squares.get(coordinate);

        if (square == null) {
            throw new AssertionError();
        }

        return square;
    }

    /* Setters */

    void update(Board newBoard) {
        board = newBoard;

        for (var entry : squares.entrySet()) {
            var newPiece = board.pieceAt(entry.getKey());
            var square = entry.getValue();

            if (!Objects.equals(newPiece, square.piece())) {
                square.piece(newPiece);
            }
        }
    }

    void reset(Board newBoard) {
        board = newBoard;

        for (var entry : squares.entrySet()) {
            var piece = board.pieceAt(entry.getKey());
            var square = entry.getValue();
            square.reset(piece);
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

    void makeDark(boolean value) {
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

    void hideHighlightedMove() {
        squares.values().forEach(square -> square.move(false));
    }

    private class ClearListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            var button = event.getButton();

            if (button == Input.Buttons.RIGHT) {
                fire(CLEAR_SELECTION);
            }
        }

        private ClearListener() {
            setButton(Input.Buttons.RIGHT);
        }
    }
}
