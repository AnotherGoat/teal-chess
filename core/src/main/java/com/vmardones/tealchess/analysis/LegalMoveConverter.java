/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.List;
import java.util.stream.Stream;

import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.move.MoveResult;

final class LegalMoveConverter {

    private final Position position;
    private final MoveMaker moveMaker;

    LegalMoveConverter(Position position) {
        this.position = position;
        moveMaker = new MoveMaker();
    }

    List<LegalMove> transformToLegals(Stream<Move> confirmedLegals) {
        return confirmedLegals.map(this::makeLegal).toList();
    }

    private LegalMove makeLegal(Move move) {
        var afterMove = moveMaker.make(position, move);
        var result = calculateResult(afterMove);
        return move.makeLegal(result);
    }

    private MoveResult calculateResult(Position afterMove) {

        var pseudoLegals = new PseudoLegalGenerator(afterMove).generate();

        var legalityTester = new LegalityTester(afterMove);
        var confirmedLegals = legalityTester.testPseudoLegals(pseudoLegals);

        var opponentAttacks =
                new AttackGenerator(afterMove).calculateAttacks(true).toList();
        var attackTester = new AttackTester(afterMove, opponentAttacks);

        var attacked = attackTester.isKingAttacked();
        var cantMove = confirmedLegals.findFirst().isPresent();

        return MoveResult.findResult(attacked, cantMove);
    }
}
