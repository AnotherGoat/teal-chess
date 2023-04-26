/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.move.MoveResult;

final class LegalityChecker {

    private final Position position;
    private final MoveMaker moveMaker;

    LegalityChecker(Position position, MoveMaker moveMaker) {
        this.position = position;
        this.moveMaker = moveMaker;
    }

    List<Move> filterPseudoLegals(List<Move> pseudoLegals) {

        var legals = new ArrayList<Move>();

        for (var move : pseudoLegals) {

            var afterMove = moveMaker.make(position, move);

            var attackGenerator = new AttackGenerator(afterMove);
            var opponentAttacks = attackGenerator.calculateAttacks(false).toList();
            var attackTester = new AttackTester(afterMove, opponentAttacks);

            var king = afterMove.board().king(position.sideToMove());

            if (!attackTester.isAttacked(king.coordinate())) {
                legals.add(move);
            }
        }

        return legals;
    }

    List<LegalMove> transformToLegals(List<Move> confirmedLegals) {

        var legals = new ArrayList<LegalMove>();

        for (var move : confirmedLegals) {
            var afterMove = moveMaker.make(position, move);

            var result = calculateResult(afterMove);
            legals.add(move.makeLegal(result));
        }

        return legals;
    }

    // TODO: Remove duplicated code
    private MoveResult calculateResult(Position afterMove) {

        var pseudoLegals = new PseudoLegalGenerator(afterMove).generate().toList();

        var legalityChecker = new LegalityChecker(afterMove, new MoveMaker());
        var confirmedLegals = legalityChecker.filterPseudoLegals(pseudoLegals);

        // TODO: Duplicated code ends here

        var opponentAttacks =
                new AttackGenerator(afterMove).calculateAttacks(true).toList();
        var attackTester = new AttackTester(afterMove, opponentAttacks);

        var attacked = attackTester.isKingAttacked();
        var cantMove = confirmedLegals.isEmpty();

        if (attacked && cantMove) {
            return MoveResult.CHECKMATES;
        }

        if (!attacked && cantMove) {
            return MoveResult.STALEMATES;
        }

        if (attacked) {
            return MoveResult.CHECKS;
        }

        return MoveResult.CONTINUE;
    }
}
