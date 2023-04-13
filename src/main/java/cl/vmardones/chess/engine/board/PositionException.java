/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

final class PositionException extends RuntimeException {

    PositionException(int index) {
        super("Index is outside chessboard: " + index);
    }
}
