/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.Objects;
import java.util.stream.Stream;

import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveMaker;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Responsible for testing pseudo-legal moves and checking whether the player's king is checked after them or not.
 * @see <a href="https://www.chessprogramming.org/Square_Attacked_By#LegalityTest">Legality Test</a>
 */
final class LegalityTester {

    private final Position position;
    private final MoveMaker moveMaker;

    LegalityTester(Position position) {
        this.position = position;
        moveMaker = new MoveMaker();
    }

    Stream<Move> testPseudoLegals(Stream<Move> pseudoLegals) {
        return pseudoLegals.map(this::testPseudoLegal).filter(Objects::nonNull);
    }

    private @Nullable Move testPseudoLegal(Move move) {
        var afterMove = moveMaker.make(position, move);

        var attackGenerator = new AttackGenerator(afterMove);
        var opponentAttacks = attackGenerator.calculateAttacks(false).toList();
        var attackTester = new AttackTester(afterMove, opponentAttacks);

        var king = afterMove.board().king(position.sideToMove());

        if (attackTester.isAttacked(king.coordinate())) {
            return null;
        }

        return move;
    }
}
