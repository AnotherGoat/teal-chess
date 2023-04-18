/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import java.util.List;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.game.Position;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveResult;
import cl.vmardones.chess.engine.piece.King;

final class MoveTester {

    private final King king;
    private final List<Move> opponentAttacks;

    MoveTester(Position position, List<Move> opponentAttacks) {
        king = position.board().king(position.sideToMove());
        this.opponentAttacks = opponentAttacks;
    }

    boolean isKingAttacked() {
        return isAttacked(king.coordinate());
    }

    boolean isAttacked(Coordinate target) {
        return opponentAttacks.stream().anyMatch(move -> target.equals(move.destination()));
    }

    MoveResult testMove(Move move, List<Move> legals) {
        if (move.isNone()) {
            return MoveResult.NONE;
        }

        if (!legals.contains(move)) {
            return MoveResult.ILLEGAL;
        }

        if (isKingAttacked()) {
            return MoveResult.CHECKS;
        }

        return MoveResult.CONTINUE;
    }
}
