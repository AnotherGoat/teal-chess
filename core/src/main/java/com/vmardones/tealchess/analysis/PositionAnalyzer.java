/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.List;
import java.util.stream.Stream;

import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;
import com.vmardones.tealchess.player.Player;

// TODO: For now, it only generates pseudo-legal moves
/**
 * Analyzes a certain position and generates all the possible legal moves.
 */
public final class PositionAnalyzer {

    private final List<Move> legals;
    private final MoveTester moveTester;
    private final PlayerFactory playerFactory;

    // TODO: Add promotion moves to the pseudo-legals list
    public PositionAnalyzer(Position position) {
        var attackGenerator = new AttackGenerator(position);
        var opponentAttacks = attackGenerator.calculateAttacks(true).toList();

        var moveGenerator = new MoveGenerator(position);
        var moves = moveGenerator.calculateMoves();

        var captureGenerator = new CaptureGenerator(position);
        var captures = captureGenerator.calculateCaptures();

        var pawnMoveGenerator = new PawnMoveGenerator(position);
        var pawnMoves = pawnMoveGenerator.calculatePawnMoves();

        moveTester = new MoveTester(position, opponentAttacks);
        var castleGenerator = new CastleGenerator(position, moveTester);
        var castles = castleGenerator.calculateCastles();

        var pseudoLegals = Stream.concat(Stream.concat(moves, captures), Stream.concat(pawnMoves, castles))
                .toList();

        var legalityChecker = new LegalityChecker(position, new MoveMaker());
        legals = legalityChecker.checkPseudoLegals(pseudoLegals);

        playerFactory = new PlayerFactory(position, moveTester, legals);
    }

    public Player createPlayer(Color color) {
        return playerFactory.create(color);
    }

    /**
     * Given a piece, find the legal moves it has for this position.
     * @param piece The piece to move.
     * @return The legal moves of the piece.
     */
    public List<Move> findLegalMoves(Piece piece) {
        return legals.stream().filter(move -> move.piece().equals(piece)).toList();
    }
}
