/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static java.util.stream.Collectors.toUnmodifiableSet;

import java.util.List;
import java.util.Set;

import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;
import com.vmardones.tealchess.player.Player;

/**
 * Analyzes a certain position and generates all the possible legal moves.
 */
public final class PositionAnalyzer {

    private final List<LegalMove> legals;
    private final Player whitePlayer;
    private final Player blackPlayer;

    public PositionAnalyzer(Position position) {

        var pseudoLegals = new PseudoLegalGenerator(position).generate().toList();

        var legalityChecker = new LegalityChecker(position, new MoveMaker());
        var confirmedLegals = legalityChecker.filterPseudoLegals(pseudoLegals);
        legals = legalityChecker.transformToLegals(confirmedLegals);

        var playerFactory = new PlayerFactory(position, legals);

        whitePlayer = playerFactory.create(Color.WHITE);
        blackPlayer = playerFactory.create(Color.BLACK);
    }

    /* Getters */

    public Player newWhitePlayer() {
        return whitePlayer;
    }

    public Player newBlackPlayer() {
        return blackPlayer;
    }

    /**
     * Given a piece, find the legal moves it has for this position.
     * @param piece The piece to move.
     * @return The legal moves of the piece.
     */
    public Set<LegalMove> findLegalMoves(Piece piece) {
        return legals.stream()
                .filter(legal -> legal.move().piece().equals(piece))
                .collect(toUnmodifiableSet());
    }
}
