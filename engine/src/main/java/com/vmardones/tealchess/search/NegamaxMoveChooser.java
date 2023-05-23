/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.search;

import java.util.List;
import java.util.Random;

import com.vmardones.tealchess.evaluation.BoardEvaluator;
import com.vmardones.tealchess.generator.AttackGenerator;
import com.vmardones.tealchess.generator.LegalGenerator;
import com.vmardones.tealchess.generator.MoveGenerator;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.player.PlayerFactory;
import com.vmardones.tealchess.position.Position;

public final class NegamaxMoveChooser implements MoveChooser {

    private final Random random = new Random();
    private final MoveGenerator moveGenerator = new LegalGenerator();
    private final MoveMaker moveMaker = new MoveMaker();
    private final PlayerFactory playerFactory = new PlayerFactory(new AttackGenerator(), moveGenerator);
    private final BoardEvaluator evaluator;
    private final int depth;

    public NegamaxMoveChooser(BoardEvaluator evaluator, int depth) {
        this.evaluator = evaluator;
        this.depth = depth;
    }

    @Override
    public Move chooseMove(Position position, List<Move> legals) {

        var bestMove = legals.get(0);

        var highest = Integer.MIN_VALUE;
        var lowest = Integer.MAX_VALUE;

        for (var move : legals) {
            var nextPosition = moveMaker.make(position, move);
            var nextSideToMove = nextPosition.sideToMove();
            var player = playerFactory.create(nextPosition, nextSideToMove);

            if (nextSideToMove.isWhite()) {
                var score = -negamax(nextPosition, player.legals(), depth - 1);

                if (score > highest) {
                    highest = score;
                    bestMove = move;
                } else if (score == highest && random.nextBoolean()) {
                    bestMove = move;
                }
            } else {
                var score = negamax(nextPosition, player.legals(), depth - 1);

                if (score < lowest) {
                    lowest = score;
                    bestMove = move;
                } else if (score == highest && random.nextBoolean()) {
                    bestMove = move;
                }
            }
        }

        return bestMove;
    }

    private int negamax(Position position, List<Move> legals, int depth) {
        if (depth == 0 || legals.isEmpty()) {
            return evaluator.evaluate(position);
        }

        var highest = Integer.MIN_VALUE;

        for (var move : legals) {
            var nextPosition = moveMaker.make(position, move);
            var nextSideToMove = nextPosition.sideToMove();
            var player = playerFactory.create(nextPosition, nextSideToMove);

            var score = -negamax(nextPosition, player.legals(), depth - 1);

            if (score > highest) {
                highest = score;
            }
        }

        return highest;
    }
}
