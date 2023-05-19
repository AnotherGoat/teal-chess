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
import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.io.assets.AssetLoader;
import com.vmardones.tealchess.io.settings.SettingManager;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.square.Coordinate;
import com.vmardones.tealchess.square.Square;

final class Chessboard extends Group {

    private static final Event CLEAR_SELECTION = new SimpleEvent(EventType.CLEAR_SELECTION);
    private static final int SIZE = AssetLoader.SQUARE_SIZE * Board.SIDE_LENGTH;
    private Board board;
    private final List<ClickableSquare> squares = new ArrayList<>();

    Chessboard(SettingManager settings, AssetLoader assets, Board board) {
        this.board = board;

        setSize(SIZE, SIZE);

        var x = (Gdx.graphics.getWidth() - getWidth()) / 2;
        var y = (Gdx.graphics.getHeight() - getHeight()) / 2;
        setPosition(x, y);

        for (var square : Square.all()) {
            var coordinate = Coordinate.forSquare(square);
            var clickableSquare = new ClickableSquare(
                    settings, assets, coordinate, board.colorOf(coordinate), board.pieceAt(coordinate));
            squares.add(clickableSquare);
            addActor(clickableSquare);
        }

        addListener(new ClearSelectionListener());
    }

    ClickableSquare squareAt(int squareIndex) {
        return squares.get(squareIndex);
    }

    /* Setters */

    void update(Board newBoard) {
        board = newBoard;

        for (var square : squares) {
            var newPiece = board.pieceAt(square.coordinate());

            if (!Objects.equals(newPiece, square.piece())) {
                square.piece(newPiece);
            }
        }
    }

    void reset(Board newBoard) {
        board = newBoard;

        for (var square : squares) {
            var piece = board.pieceAt(square.coordinate());
            square.reset(piece);
        }
    }

    void flip(boolean flip) {
        squares.forEach(square -> square.flip(flip));
    }

    void showSource(Coordinate source) {
        for (var square : squares) {
            if (square.coordinate().equals(source)) {
                square.showSource(true);
                break;
            }
        }
    }

    void hideSource() {
        squares.forEach(square -> square.showSource(false));
    }

    void showDestinations(Set<Coordinate> coordinates) {
        squares.forEach(square -> square.showDestination(coordinates.contains(square.coordinate())));
    }

    void hideDestinations() {
        squares.forEach(square -> square.showDestination(false));
    }

    void showChecked(Coordinate coordinate) {
        squares.forEach(square -> square.showChecked(square.coordinate().equals(coordinate)));
    }

    void hideChecked() {
        squares.forEach(square -> square.showChecked(false));
    }

    void showMove(Move move) {
        var source = move.source();
        var destination = move.destination();

        for (var square : squares) {
            var isPartOfTheMove = square.coordinate().squareIndex() == source
                    || square.coordinate().squareIndex() == destination;
            square.showLastMove(isPartOfTheMove);
        }
    }

    void showAttacks(Color sideToMove, Set<Coordinate> opponentAttacks) {
        for (var square : squares) {
            var piece = square.piece();

            if (piece == null) {
                square.showAttacked(false);
                continue;
            }

            var isAttacked = piece.color() == sideToMove && opponentAttacks.contains(square.coordinate());
            square.showAttacked(isAttacked);
        }
    }

    void hideAttacks() {
        squares.forEach(square -> square.showAttacked(false));
    }

    void makeDark(boolean value) {
        squares.forEach(square -> square.showDark(value));
    }

    private class ClearSelectionListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            var button = event.getButton();

            if (button == Input.Buttons.RIGHT) {
                fire(CLEAR_SELECTION);
            }
        }

        private ClearSelectionListener() {
            setButton(Input.Buttons.RIGHT);
        }
    }
}
