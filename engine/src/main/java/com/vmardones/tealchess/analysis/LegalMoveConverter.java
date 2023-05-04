/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static java.util.stream.Collectors.groupingBy;

import java.util.*;
import java.util.function.Predicate;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.*;

final class LegalMoveConverter {

    private final Position position;
    private final MoveMaker moveMaker;

    LegalMoveConverter(Position position) {
        this.position = position;
        moveMaker = new MoveMaker();
    }

    List<LegalMove> transformToLegals(List<Move> confirmedLegals) {

        var ambiguities = findAmbiguities(confirmedLegals);
        var unambiguousLegals = new ArrayList<>(confirmedLegals);
        unambiguousLegals.removeAll(ambiguities.allMoves());

        var legalMoves =
                new ArrayList<>(unambiguousLegals.stream().map(this::makeLegal).toList());

        if (ambiguities.hasKnights()) {
            addDisambiguation(ambiguities.knightMoves().values(), legalMoves);
        }
        if (ambiguities.hasBishops()) {
            addDisambiguation(ambiguities.bishopMoves().values(), legalMoves);
        }
        if (ambiguities.hasRooks()) {
            addDisambiguation(ambiguities.rookMoves().values(), legalMoves);
        }
        if (ambiguities.hasQueens()) {
            addDisambiguation(ambiguities.queenMoves().values(), legalMoves);
        }

        return legalMoves;
    }

    private Ambiguities findAmbiguities(List<Move> confirmedLegals) {

        var knightMoves =
                groupAmbiguousMoves(confirmedLegals, move -> move.piece().isKnight());
        var bishopMoves =
                groupAmbiguousMoves(confirmedLegals, move -> move.piece().isBishop());
        var rookMoves =
                groupAmbiguousMoves(confirmedLegals, move -> move.piece().isRook());
        var queenMoves =
                groupAmbiguousMoves(confirmedLegals, move -> move.piece().isQueen());

        return new Ambiguities(knightMoves, bishopMoves, rookMoves, queenMoves);
    }

    private Map<Coordinate, List<Move>> groupAmbiguousMoves(List<Move> confirmedLegals, Predicate<Move> condition) {
        var destinations = confirmedLegals.stream().filter(condition).collect(groupingBy(Move::destination));
        var keysToRemove = new HashSet<Coordinate>();

        for (var destination : destinations.entrySet()) {
            var moves = destination.getValue();

            if (moves.size() == 1) {
                keysToRemove.add(destination.getKey());
            }
        }

        keysToRemove.forEach(destinations::remove);

        return destinations;
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
        var cantMove = confirmedLegals.isEmpty();

        return MoveResult.findResult(attacked, cantMove);
    }

    private void addDisambiguation(Collection<List<Move>> ambiguousCases, List<LegalMove> legalMoves) {
        for (var moves : ambiguousCases) {

            var firstSource = moves.get(0).source();
            var firstFile = firstSource.file();
            var firstRank = firstSource.rank();

            var sameFile = moves.stream().allMatch(move -> move.source().file().equals(firstFile));
            var sameRank = moves.stream().allMatch(move -> move.source().rank() == firstRank);

            if (sameFile && sameRank) {
                throw new AssertionError("Unreachable statement");
            }

            if (!sameFile && sameRank) {
                for (var move : moves) {
                    legalMoves.add(makeLegal(move, Disambiguation.FILE));
                }
            } else if (sameFile) {
                for (var move : moves) {
                    legalMoves.add(makeLegal(move, Disambiguation.RANK));
                }
            } else {
                for (var move : moves) {
                    legalMoves.add(makeLegal(move, Disambiguation.FULL));
                }
            }
        }
    }

    private LegalMove makeLegal(Move move, Disambiguation disambiguation) {
        var afterMove = moveMaker.make(position, move);
        var result = calculateResult(afterMove);
        return move.makeLegal(result, disambiguation);
    }

    private record Ambiguities(
            Map<Coordinate, List<Move>> knightMoves,
            Map<Coordinate, List<Move>> bishopMoves,
            Map<Coordinate, List<Move>> rookMoves,
            Map<Coordinate, List<Move>> queenMoves) {

        private List<Move> allMoves() {
            var ambiguousMoves = new ArrayList<Move>();

            for (var moves : knightMoves.values()) {
                ambiguousMoves.addAll(moves);
            }
            for (var moves : bishopMoves.values()) {
                ambiguousMoves.addAll(moves);
            }
            for (var moves : rookMoves.values()) {
                ambiguousMoves.addAll(moves);
            }
            for (var moves : queenMoves.values()) {
                ambiguousMoves.addAll(moves);
            }

            return ambiguousMoves;
        }

        private boolean hasKnights() {
            return !knightMoves.isEmpty();
        }

        private boolean hasBishops() {
            return !bishopMoves.isEmpty();
        }

        private boolean hasRooks() {
            return !rookMoves.isEmpty();
        }

        private boolean hasQueens() {
            return !queenMoves.isEmpty();
        }
    }
}
