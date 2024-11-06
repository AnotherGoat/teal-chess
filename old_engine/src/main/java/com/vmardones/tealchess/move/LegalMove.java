/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.parser.pgn.San;
import com.vmardones.tealchess.piece.Piece;
import org.jspecify.annotations.Nullable;

/**
 * Represents a legal move done by one side, also known as a ply depending on the context.
 * @see <a href="https://www.chessprogramming.org/Legal_Move">Legal Move</a>
 * @see <a href="https://www.chessprogramming.org/Ply">Ply</a>
 */
public final class LegalMove implements San {

    private final Move move;
    private final MoveResult result;
    private final @Nullable Disambiguation disambiguation;

    /**
     * Convert a pseudo-legal move into a legal one. For this, a move result must be supplied.
     * @param move The pseudo-legal move.
     * @param result What happens to the opponent after the move is done.
     */
    public LegalMove(Move move, MoveResult result) {
        this.move = move;
        this.result = result;
        disambiguation = null;
    }

    /**
     * Convert a pseudo-legal move into a legal one. For this, a move result must be supplied.
     * This alternative also asks for a disambiguation type, which is needed for proper SAN movetext representation.
     * For unambiguous cases, use the shorter version of this method.
     * @param move The pseudo-legal move.
     * @param result What happens to the opponent after the move is done.
     * @param disambiguation Disambiguation type.
     */
    public LegalMove(Move move, MoveResult result, Disambiguation disambiguation) {
        this.move = move;
        this.result = result;
        this.disambiguation = disambiguation;
    }

    /* Getters */

    public Move move() {
        return move;
    }

    public Piece piece() {
        return move.piece();
    }

    public Coordinate source() {
        return move.source();
    }

    public Coordinate destination() {
        return move.destination();
    }

    @Override
    public String san() {
        var base = move.san() + result.san();

        if (disambiguation == null) {
            return base;
        }

        var start = base.substring(0, 1);
        var end = base.substring(1);

        return switch (disambiguation) {
            case FILE -> start + source().file() + end;
            case RANK -> start + source().rank() + end;
            case FULL -> start + source().san() + end;
        };
    }

    public boolean isCastling() {
        return move.type() == MoveType.KING_CASTLE || move.type() == MoveType.QUEEN_CASTLE;
    }

    /* toString */

    @Override
    public String toString() {
        return san();
    }
}
