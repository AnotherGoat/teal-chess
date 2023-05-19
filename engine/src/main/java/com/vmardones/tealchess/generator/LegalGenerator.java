/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.position.Position;

public final class LegalGenerator implements MoveGenerator {

    private final MoveGenerator pseudoLegalGenerator;
    private final MoveMaker moveMaker;
    private final AttackGenerator attackGenerator;

    @Override
    public List<Move> generate(Position position) {
        var moves = new ArrayList<Move>();

        var pseudoLegals = pseudoLegalGenerator.generate(position);

        for (var pseudoLegal : pseudoLegals) {
            var postMove = moveMaker.make(position, pseudoLegal);

            var opponent = postMove.sideToMove();
            var attacks = attackGenerator.generate(postMove, opponent);

            var player = opponent.opposite();
            var board = postMove.board();
            var playerKing = board.kings(player);

            if ((playerKing & attacks) == 0) {
                moves.add(pseudoLegal);
            }
        }

        return moves;
    }

    public LegalGenerator() {
        pseudoLegalGenerator = new PseudoLegalGenerator();
        moveMaker = new MoveMaker();
        attackGenerator = new AttackGenerator();
    }
}
