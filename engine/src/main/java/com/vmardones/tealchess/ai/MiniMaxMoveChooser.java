/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.ai;

import java.util.List;

import com.vmardones.tealchess.analysis.PositionAnalyzer;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.MoveMaker;

public final class MiniMaxMoveChooser implements MoveChooser {

    private final MoveMaker moveMaker = new MoveMaker();
    private final BoardEvaluator evaluator;
    private final int depth;

    public MiniMaxMoveChooser(BoardEvaluator evaluator, int depth) {
        this.evaluator = evaluator;
        this.depth = depth;
    }

    @Override
    public LegalMove chooseMove(Position position, List<LegalMove> legals) {

        var startTime = System.currentTimeMillis();

        LegalMove bestMove = null;

        var highestValue = Integer.MIN_VALUE;
        var lowestValue = Integer.MAX_VALUE;
        int currentValue;

        for (var move : legals) {
            var nextPosition = moveMaker.make(position, move);
            var analyzer = new PositionAnalyzer(nextPosition);
            var sideToMove = nextPosition.sideToMove();

            // TODO: Move this method to position analyzer
            var player = sideToMove.isWhite() ? analyzer.whitePlayer() : analyzer.blackPlayer();

            if (sideToMove.isWhite()) {
                currentValue = min(nextPosition, player.legals(), depth - 1);

                if (currentValue >= highestValue) {
                    highestValue = currentValue;
                    bestMove = move;
                }
            } else {
                currentValue = max(nextPosition, player.legals(), depth - 1);

                if (currentValue <= lowestValue) {
                    lowestValue = currentValue;
                    bestMove = move;
                }
            }
        }

        var executionTime = System.currentTimeMillis() - startTime;
        System.out.println(executionTime);

        return bestMove;
    }

    private int min(Position position, List<LegalMove> legals, int depth) {
        if (depth == 0 || legals.isEmpty()) {
            return evaluator.evaluate(position);
        }

        var lowestValue = Integer.MAX_VALUE;

        for (var move : legals) {
            var nextPosition = moveMaker.make(position, move);
            var analyzer = new PositionAnalyzer(nextPosition);
            var player = nextPosition.sideToMove().isWhite() ? analyzer.whitePlayer() : analyzer.blackPlayer();

            var currentValue = max(nextPosition, player.legals(), depth - 1);

            if (currentValue <= lowestValue) {
                lowestValue = currentValue;
            }
        }

        return lowestValue;
    }

    private int max(Position position, List<LegalMove> legals, int depth) {
        if (depth == 0 || legals.isEmpty()) {
            return evaluator.evaluate(position);
        }

        var highestValue = Integer.MIN_VALUE;

        for (var move : legals) {
            var nextPosition = moveMaker.make(position, move);
            var analyzer = new PositionAnalyzer(nextPosition);
            var player = nextPosition.sideToMove().isWhite() ? analyzer.whitePlayer() : analyzer.blackPlayer();

            var currentValue = min(nextPosition, player.legals(), depth - 1);

            if (currentValue >= highestValue) {
                highestValue = currentValue;
            }
        }

        return highestValue;
    }
}
