/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.*;

import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.*;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.PieceType;

final class LegalMoveConverter {

    private final Position position;
    private final List<Piece> pieces;
    private final MoveMaker moveMaker = new MoveMaker();

    LegalMoveConverter(Position position) {
        this.position = position;
        pieces = position.board().pieces(position.sideToMove());
    }

    List<LegalMove> transformToLegals(List<Move> confirmedLegals) {
        var legalMoves = new ArrayList<LegalMove>();

        for (var move : confirmedLegals) {
            legalMoves.add(transformToLegal(move, confirmedLegals));
        }

        return legalMoves;
    }

    private LegalMove transformToLegal(Move move, List<Move> confirmedLegals) {
        var piece = move.piece();

        if (piece.isPawn() || piece.isKing()) {
            return makeLegal(move);
        }

        var type = piece.type();

        if (countPieces(type) == 1) {
            return makeLegal(move);
        }

        var similarMoves = findSimilarMoves(confirmedLegals, move);

        if (similarMoves.isEmpty()) {
            return makeLegal(move);
        }

        var disambiguation = findDisambiguation(move, similarMoves);

        return makeLegal(move, disambiguation);
    }

    private LegalMove makeLegal(Move move) {
        var afterMove = moveMaker.make(position, move);
        var result = calculateResult(afterMove);
        return move.makeLegal(result);
    }

    private LegalMove makeLegal(Move move, Disambiguation disambiguation) {
        var afterMove = moveMaker.make(position, move);
        var result = calculateResult(afterMove);
        return move.makeLegal(result, disambiguation);
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

    private long countPieces(PieceType type) {
        return pieces.stream()
                .filter(otherPiece -> otherPiece.type().equals(type))
                .count();
    }

    private List<Move> findSimilarMoves(List<Move> moves, Move moveToCheck) {
        var pieceType = moveToCheck.piece().type();
        var destination = moveToCheck.destination();
        var file = moveToCheck.source().file();
        var rank = moveToCheck.source().rank();

        return moves.stream()
                .filter(move -> move.piece().type().equals(pieceType)
                        && move.destination().equals(destination))
                .filter(move ->
                        move.source().file().equals(file) || move.source().rank() == rank)
                .filter(move -> !move.equals(moveToCheck))
                .toList();
    }

    private Disambiguation findDisambiguation(Move moveToCheck, List<Move> otherMoves) {
        var fileToCheck = moveToCheck.source().file();
        var fileDisambiguates =
                otherMoves.stream().map(move -> move.source().file()).noneMatch(file -> file.equals(fileToCheck));

        var rankToCheck = moveToCheck.source().rank();
        var rankDisambiguates =
                otherMoves.stream().map(move -> move.source().rank()).noneMatch(rank -> rank.equals(rankToCheck));

        if (fileDisambiguates && rankDisambiguates) {
            throw new AssertionError();
        }

        if (fileDisambiguates) {
            return Disambiguation.FILE;
        }

        if (rankDisambiguates) {
            return Disambiguation.RANK;
        }

        return Disambiguation.FULL;
    }
}
