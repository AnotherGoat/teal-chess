/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.search;

import java.util.Random;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.evaluation.BoardEvaluator;
import com.vmardones.tealchess.game.GameMemento;
import com.vmardones.tealchess.generator.AttackGenerator;
import com.vmardones.tealchess.generator.LegalGenerator;
import com.vmardones.tealchess.generator.MoveGenerator;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.player.PlayerFactory;

public final class MinimaxMoveChooser implements MoveChooser {

    private final Random random = new Random();
    private final MoveGenerator moveGenerator = new LegalGenerator();
    private final MoveMaker moveMaker = new MoveMaker();
    private final PlayerFactory playerFactory = new PlayerFactory(new AttackGenerator(), moveGenerator);
    private final BoardEvaluator evaluator;
    private final int depth;

    public MinimaxMoveChooser(BoardEvaluator evaluator, int depth) {
        this.evaluator = evaluator;
        this.depth = depth;
    }

    @Override
    public Move chooseMove(GameMemento state) {

        var legals = state.player().legals();
        var bestMove = legals.get(0);

        var highest = Integer.MIN_VALUE;
        var lowest = Integer.MAX_VALUE;

        for (var move : legals) {
            // TODO: Encapsulate this into a method
            var position = moveMaker.make(state.position(), move);
            var whitePlayer = playerFactory.create(position, Color.WHITE);
            var blackPlayer = playerFactory.create(position, Color.BLACK);

            var nextState = new GameMemento(position, whitePlayer, blackPlayer, move);

            if (position.sideToMove().isWhite()) {
                var score = min(nextState, depth - 1);

                if (score > highest) {
                    highest = score;
                    bestMove = move;
                } else if (score == highest && random.nextBoolean()) {
                    bestMove = move;
                }
            } else {
                var score = max(nextState, depth - 1);

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

    private int min(GameMemento state, int depth) {
        var legals = state.player().legals();

        if (depth == 0 || legals.isEmpty()) {
            return evaluator.evaluate(state);
        }

        var lowest = Integer.MAX_VALUE;

        for (var move : legals) {
            var position = moveMaker.make(state.position(), move);
            var whitePlayer = playerFactory.create(position, Color.WHITE);
            var blackPlayer = playerFactory.create(position, Color.BLACK);

            var nextState = new GameMemento(position, whitePlayer, blackPlayer, move);

            var score = max(nextState, depth - 1);

            if (score < lowest) {
                lowest = score;
            }
        }

        return lowest;
    }

    private int max(GameMemento state, int depth) {
        var legals = state.player().legals();

        if (depth == 0 || legals.isEmpty()) {
            return evaluator.evaluate(state);
        }

        var highest = Integer.MIN_VALUE;

        for (var move : legals) {
            var position = moveMaker.make(state.position(), move);
            var whitePlayer = playerFactory.create(position, Color.WHITE);
            var blackPlayer = playerFactory.create(position, Color.BLACK);

            var nextState = new GameMemento(position, whitePlayer, blackPlayer, move);

            var score = min(nextState, depth - 1);

            if (score > highest) {
                highest = score;
            }
        }

        return highest;
    }
}
