/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;

import cl.vmardones.chess.engine.player.Alliance;

/**
 * The king piece. The most important piece in the game, must be defended at all costs. It moves
 * like the queen, but only one space at a time. It also cannot move into a position where it could
 * be captured.
 */
public final class King extends JumpingPiece {

    private static final List<int[]> MOVES = List.of(
            new int[] {-2, 0},
            new int[] {-1, 1},
            new int[] {0, 1},
            new int[] {1, 1},
            new int[] {-1, 0},
            new int[] {1, 0},
            new int[] {-1, -1},
            new int[] {0, -1},
            new int[] {1, -1},
            new int[] {2, 0});

    public King(String position, Alliance alliance) {
        this(position, alliance, true, false, false);
    }

    @Override
    public King moveTo(String destination) {
        return moveTo(destination, false, false);
    }

    /**
     * Move this king to another square. No checks of any kind are done to check whether the move is
     * legal or not. This overload allows moving the king and allowing the new one to perform a castle move.
     *
     * @param destination The destination to move the piece to.
     * @param kingCastle Whether king side castle is allowed for this king's next move or not.
     * @param queenCastle Whether queen side castle is allowed for this king's next move or not.
     * @return The piece after the move is completed.
     */
    public King moveTo(String destination, boolean kingCastle, boolean queenCastle) {
        return new King(destination, alliance, false, kingCastle, queenCastle);
    }

    private King(String position, Alliance alliance, boolean firstMove, boolean kingCastle, boolean queenCastle) {
        super(position, alliance, firstMove, calculateMoves(kingCastle, queenCastle));
    }

    private static List<int[]> calculateMoves(boolean kingCastle, boolean queenCastle) {
        var leftLimit = queenCastle ? 0 : 1;
        var rightLimit = kingCastle ? 9 : 10;

        return MOVES.subList(leftLimit, rightLimit);
    }
}
