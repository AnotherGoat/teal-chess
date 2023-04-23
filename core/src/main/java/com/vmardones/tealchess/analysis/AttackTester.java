/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.List;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Attack;
import com.vmardones.tealchess.piece.King;

final class AttackTester {

    private final King king;
    private final List<Attack> opponentAttacks;

    AttackTester(Position position, List<Attack> opponentAttacks) {
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
}
