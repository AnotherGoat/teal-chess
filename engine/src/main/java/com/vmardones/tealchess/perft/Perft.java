/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.perft;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.vmardones.tealchess.generator.MoveGenerator;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.player.PlayerFactory;
import com.vmardones.tealchess.player.PlayerStatus;
import com.vmardones.tealchess.position.Position;

public final class Perft {

    private final MoveGenerator moveGenerator;
    private final MoveMaker moveMaker;
    private final PlayerFactory playerFactory;

    public Perft(MoveGenerator moveGenerator, MoveMaker moveMaker, PlayerFactory playerFactory) {
        this.moveGenerator = moveGenerator;
        this.moveMaker = moveMaker;
        this.playerFactory = playerFactory;
    }

    public long execute(Position position, int depth) {
        if (depth == 0) {
            return 1L;
        }

        var nodes = 0L;
        var moves = moveGenerator.generate(position);

        for (var move : moves) {
            var postMove = moveMaker.make(position, move);
            nodes += execute(postMove, depth - 1);
        }

        return nodes;
    }

    public Map<Move, Long> divide(Position position, int depth) {
        if (depth == 0) {
            return new HashMap<>();
        }

        var nodesPerMove = new HashMap<Move, Long>();
        var moves = moveGenerator.generate(position);

        for (var move : moves) {
            var postMove = moveMaker.make(position, move);
            nodesPerMove.put(move, execute(postMove, depth - 1));
        }

        return nodesPerMove;
    }

    /**
     * Perform a perft divide and generate a short text report, similar to Stockfish's output.
     * @param position The position to analyze.
     * @param depth Max depth of the nodes to traverse.
     * @return A perft divide report.
     */
    public String divideReport(Position position, int depth) {
        var result = new StringBuilder();
        var divideResult = divide(position, depth);

        for (var entry : divideResult.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        var totalNodes = divideResult.values().stream().reduce(0L, Long::sum);
        result.append("\nNodes searched: ").append(totalNodes);

        return result.toString();
    }

    public PerftResults detailedExecute(Position position, int depth) {
        if (depth == 0) {
            return new PerftResults(1, 0, 0, 0, 0, 0, 0);
        }

        var results = new PerftResults();
        var moves = moveGenerator.generate(position);

        for (var move : moves) {
            var type = move.type();

            if (type == MoveType.CAPTURE || type == MoveType.PAWN_CAPTURE) {
                results.captures++;
            }

            if (type == MoveType.EN_PASSANT) {
                results.enPassants++;
            }

            if (type == MoveType.KING_CASTLE || type == MoveType.QUEEN_CASTLE) {
                results.castles++;
            }

            if (move.promotionChoice() != null) {
                results.promotions++;
            }

            var postMove = moveMaker.make(position, move);
            var nextSideToMove = postMove.sideToMove();

            var nextPlayer = playerFactory.create(postMove, nextSideToMove);
            var status = nextPlayer.status();

            if (status == PlayerStatus.CHECKED) {
                results.checks++;
            }

            if (status == PlayerStatus.CHECKMATED) {
                results.checkmates++;
            }

            results.add(detailedExecute(postMove, depth - 1));
        }

        return results;
    }

    public Map<Move, PerftResults> detailedDivide(Position position, int depth) {
        if (depth == 0) {
            return new HashMap<>();
        }

        var resultsPerMove = new HashMap<Move, PerftResults>();
        var moves = moveGenerator.generate(position);

        for (var move : moves) {
            var postMove = moveMaker.make(position, move);
            resultsPerMove.put(move, detailedExecute(postMove, depth - 1));
        }

        return resultsPerMove;
    }

    public static class PerftResults {
        private long nodes;
        private long captures;
        private long enPassants;
        private long castles;
        private long promotions;
        private long checks;
        private long checkmates;

        public PerftResults() {}

        public PerftResults(
                long nodes,
                long captures,
                long enPassants,
                long castles,
                long promotions,
                long checks,
                long checkmates) {
            this.nodes = nodes;
            this.captures = captures;
            this.enPassants = enPassants;
            this.castles = castles;
            this.promotions = promotions;
            this.checks = checks;
            this.checkmates = checkmates;
        }

        /* Getters */

        public long nodes() {
            return nodes;
        }

        public long captures() {
            return captures;
        }

        public long enPassants() {
            return enPassants;
        }

        public long castles() {
            return castles;
        }

        public long promotions() {
            return promotions;
        }

        public long checks() {
            return checks;
        }

        public long checkmates() {
            return checkmates;
        }

        /* equals and hashCode */

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            var other = (PerftResults) o;
            return nodes == other.nodes
                    && captures == other.captures
                    && enPassants == other.enPassants
                    && castles == other.castles
                    && promotions == other.promotions
                    && checks == other.checks
                    && checkmates == other.checkmates;
        }

        @Override
        public int hashCode() {
            return Objects.hash(nodes, captures, enPassants, castles, promotions, checks, checkmates);
        }

        private void add(PerftResults other) {
            nodes += other.nodes;
            captures += other.captures;
            enPassants += other.enPassants;
            castles += other.castles;
            promotions += other.promotions;
            checks += other.checks;
            checkmates += other.checkmates;
        }
    }
}
