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
    private final List<Move> opponentAttacks;

    MoveTester(King king, List<Move> opponentAttacks) {
        this.king = king;
        this.opponentAttacks = opponentAttacks;
    }

    List<Move> attacksOnKing() {
        return attacksOn(king.position());
    }

    List<Move> attacksOn(Position target) {
        return opponentAttacks.stream()
                .filter(move -> target.equals(move.destination()))
                .toList();
    }

    // TODO: Use the move parameter to check what would happen if the move is done
    // For example, the player could SELF_CHECK
    MoveResult testLegalMove(Move move) {
        if (!attacksOnKing().isEmpty()) {
            return MoveResult.CHECKS;
        }

        return MoveResult.CONTINUE;
    }

    MoveResult testMove(Move move, List<Move> legals) {
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
