/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import java.util.List;

import cl.vmardones.chess.ExcludeFromGeneratedReport;
import cl.vmardones.chess.engine.board.BoardChecker;
import cl.vmardones.chess.engine.piece.King;

public final class MoveTester {

    public static MoveResult testMove(Move move, King king, List<Move> legals, List<Move> opponentLegals) {
        if (move.isNone()) {
            return MoveResult.NONE;
        }

        if (!legals.contains(move)) {
            return MoveResult.ILLEGAL;
        }

        var kingAttacks = BoardChecker.isUnderAttack(king.position(), opponentLegals);

        if (!kingAttacks.isEmpty()) {
            return MoveResult.CHECKS;
        }

        return MoveResult.CONTINUE;
    }

    @ExcludeFromGeneratedReport
    private MoveTester() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
