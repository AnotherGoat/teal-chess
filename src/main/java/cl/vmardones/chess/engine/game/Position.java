/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import java.util.List;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.player.Color;
import cl.vmardones.chess.engine.player.Player;
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
        @Nullable Pawn enPassantPawn,
        int halfmoveClock,
        int fullmoveCounter,
        @Nullable Move lastMove) {

    public Position(
            Board board,
            Color sideToMove,
            CastlingRights castlingRights,
            @Nullable Pawn enPassantPawn,
            int halfmoveClock,
            int fullmoveCounter) {
        this(board, sideToMove, castlingRights, enPassantPawn, halfmoveClock, fullmoveCounter, null);
    }

    Player player(List<Player> players) {
        return players.stream()
                .filter(player -> player.color() == sideToMove)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Unreachable statement"));
    }

    Player opponent(List<Player> players) {
        return players.stream()
                .filter(player -> player.color() != sideToMove)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Unreachable statement"));
    }
}
