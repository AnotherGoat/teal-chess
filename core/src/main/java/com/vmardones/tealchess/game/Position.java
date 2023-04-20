/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.parser.FenParser;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A chess position. The state of the game at a specific point in time.
 * @see <a href="https://www.chessprogramming.org/Chess_Position">Chess Position</a>
 * @see <a href="https://www.chessprogramming.org/Side_to_move">Side to move</a>
 * @see <a href="https://www.chessprogramming.org/Halfmove_Clock">Halfmove Clock</a>
 */
public record Position(
        Board board,
        Color sideToMove,
        CastlingRights castlingRights,
        @Nullable Pawn enPassantTarget,
        int halfmoveClock,
        int fullmoveCounter,
        // TODO: Move lastMove outside of position, keep it in the memento instead
        @Nullable Move lastMove) {

    /**
     * The initial position of the chess pieces in the board, which consists of a rank filled with 8 pawns on each side with a
     * formation of 8 major pieces behind.
     * Always used when starting a new game.
     * @see <a href="https://www.chessprogramming.org/Initial_Position">Initial Position</a>
     */
    public static final Position INITIAL_POSITION = generateInitialPosition();

    public Position(
            Board board,
            Color sideToMove,
            CastlingRights castlingRights,
            @Nullable Pawn enPassantTarget,
            int halfmoveClock,
            int fullmoveCounter) {
        this(board, sideToMove, castlingRights, enPassantTarget, halfmoveClock, fullmoveCounter, null);
    }

    private static Position generateInitialPosition() {
        return FenParser.parse("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }
}
