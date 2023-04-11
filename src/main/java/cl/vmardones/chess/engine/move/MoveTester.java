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

    public static MoveStatus testMove(Move move, King king, List<Move> legals, List<Move> opponentLegals) {
        if (move.isNone()) {
            return MoveStatus.NONE;
        }

        if (!legals.contains(move)) {
            return MoveStatus.ILLEGAL;
        }

        var kingAttacks = BoardChecker.isUnderAttack(king.position(), opponentLegals);

        if (!kingAttacks.isEmpty()) {
            return MoveStatus.CHECKS;
        }

        return MoveStatus.NORMAL;
    }

    @ExcludeFromGeneratedReport
    private MoveTester() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
