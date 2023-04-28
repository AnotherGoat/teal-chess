/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.player.Color;
import com.vmardones.tealchess.player.Player;

/**
 * Analyzes a certain position and generates all the possible legal moves for the side to move.
 */
public final class PositionAnalyzer {

    private final Player whitePlayer;
    private final Player blackPlayer;

    /**
     * The position analyzer for the initial position in a game of chess.
     * Because the initial position is present in every standard game, this instance will always be available.
     */
    public static final PositionAnalyzer INITIAL_ANALYZER = new PositionAnalyzer(Position.INITIAL_POSITION);

    /**
     * Builds a new, immutable position analyzer.
     * @param position The position to analyze. Generally, this will be used after a move is made.
     */
    public PositionAnalyzer(Position position) {

        var pseudoLegals = new PseudoLegalGenerator(position).generate();

        var legalityTester = new LegalityTester(position);
        var confirmedLegals = legalityTester.testPseudoLegals(pseudoLegals);
        var legals = legalityTester.transformToLegals(confirmedLegals);

        var playerFactory = new PlayerFactory(position, legals);

        whitePlayer = playerFactory.create(Color.WHITE);
        blackPlayer = playerFactory.create(Color.BLACK);
    }

    /* Getters */

    /**
     * Get a player in this new position, which includes all their legal moves if it's their turn to move.
     * @return The white player for this new position.
     */
    public Player whitePlayer() {
        return whitePlayer;
    }

    /**
     * Get a player in this new position, which includes all their legal moves if it's their turn to move.
     * @return The black player for this new position.
     */
    public Player blackPlayer() {
        return blackPlayer;
    }
}
