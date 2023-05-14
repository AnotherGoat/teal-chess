/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A chess position. The position of the game at a specific point in time.
 * @see <a href="https://www.chessprogramming.org/Chess_Position">Chess Position</a>
 * @see <a href="https://www.chessprogramming.org/Side_to_move">Side to move</a>
 * @see <a href="https://www.chessprogramming.org/Halfmove_Clock">Halfmove Clock</a>
 */
public record Position(
        CastlingRights castlingRights,
        @Nullable Coordinate enPassantTarget) {


    private static Position generateInitialPosition() {
        return FenParser.parse("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }
}
