/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import java.util.ArrayList;
import java.util.List;

import cl.vmardones.chess.engine.game.Position;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveMaker;

final class LegalityChecker {

    private final Position position;
    private final MoveMaker moveMaker;

    LegalityChecker(Position position, MoveMaker moveMaker) {
        this.position = position;
        this.moveMaker = moveMaker;
    }

    // TODO: legal moves and pseudo legal moves should be different classes
    List<Move> checkPseudoLegals(List<Move> pseudoLegals) {

        var legals = new ArrayList<Move>();

        for (var move : pseudoLegals) {
            var result = moveMaker.make(position, move);

            var attackGenerator = new AttackGenerator(result);
            var opponentAttacks = attackGenerator.calculateAttacks(false).toList();
            var moveTester = new MoveTester(result, opponentAttacks);

            var king = result.board().king(position.sideToMove());

            if (!moveTester.isAttacked(king.coordinate())) {
                legals.add(move);
            }
        }

        return legals;
    }

    private void isInCheckOrCheckmate(Position possiblePosition) {}
}
