/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.List;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Attack;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveResult;
import com.vmardones.tealchess.piece.King;

final class MoveTester {

    private final King king;
    private final List<Attack> opponentAttacks;

    MoveTester(Position position, List<Attack> opponentAttacks) {
        king = position.board().king(position.sideToMove());
        this.opponentAttacks = opponentAttacks;
    }

    boolean isKingAttacked() {
        return isAttacked(king.coordinate());
    }

    boolean isAttacked(Coordinate target) {
        return opponentAttacks.stream()
                .anyMatch(attack -> target.equals(attack.target().coordinate()));
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
