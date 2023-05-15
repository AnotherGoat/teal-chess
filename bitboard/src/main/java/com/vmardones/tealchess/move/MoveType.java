/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

public enum MoveType {
    /** A move where a non-pawn piece moves to an empty square. */
    NORMAL,
    /**
     *A move where a non-pawn piece captures another piece.
     * @see <a href="https://www.chessprogramming.org/Captures">Captures</a>
     */
    CAPTURE,
    /**
     * A move where a pawn moves to an empty square. Promotion can happen if the opposite side of the
     * board is reached by this move.
     * @see <a href="https://www.chessprogramming.org/Pawn_Push">Pawn Push</a>
     */
    PAWN_PUSH,
    /**
     * A special move where a pawn can be pushed an extra square during its first move.
     * @see <a href="https://www.chessprogramming.org/Pawn_Push#Double_Push">Double Push</a>
     */
    DOUBLE_PUSH,
    /**
     * A move where a pawn attacks in diagonal to capture another piece.
     */
    PAWN_CAPTURE,
    /**
     * A special move where a pawn takes down another pawn which tried to evade its attack by doing a double push. Promotion can happen if the opposite side of the
     * board is reached by this move.
     * @see <a href="https://www.chessprogramming.org/En_passant">En passant</a>
     */
    EN_PASSANT,
    /**
     * A special move where the king enters the king side castle and moves 2 squares.
     * @see <a href="https://www.chessprogramming.org/Castling">Castling</a>
     */
    KING_CASTLE,
    /**
     * A special move where the king enters the queen side castle and moves 3 squares.
     * @see <a href="https://www.chessprogramming.org/Castling">Castling</a>
     */
    QUEEN_CASTLE
}
