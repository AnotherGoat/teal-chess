/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import java.util.List;

import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveResult;
import cl.vmardones.chess.engine.piece.King;

final class MoveTester {

    private final King king;
    private final List<Move> legals;
    private final List<Move> opponentLegals;

    MoveTester(King king, List<Move> legals, List<Move> opponentLegals) {
        this.king = king;
        this.legals = legals;
        this.opponentLegals = opponentLegals;
    }

    List<Move> attacksOnKing() {
        return attacksOn(king.position());
    }

    List<Move> attacksOn(Position target) {
        return opponentLegals.stream().filter(move -> target.equals(move.destination())).toList();
    }

    MoveResult testMove(Move move) {
        if (move.isNone()) {
            return MoveResult.NONE;
        }

        if (!legals.contains(move)) {
            return MoveResult.ILLEGAL;
        }

        if (!attacksOnKing().isEmpty()) {
            return MoveResult.CHECKS;
        }

        return MoveResult.CONTINUE;
    }
}
