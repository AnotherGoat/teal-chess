/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import java.util.List;
import java.util.stream.Stream;

import cl.vmardones.chess.engine.game.Position;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveMaker;
import cl.vmardones.chess.engine.move.MoveResult;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Color;
import cl.vmardones.chess.engine.player.Player;

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
        var attacks = attackGenerator.calculateAttacks(false);

        var moveGenerator = new MoveGenerator(position);
        var moves = moveGenerator.calculateMoves();

        var pawnMoveGenerator = new PawnMoveGenerator(position);
        var pawnMoves = pawnMoveGenerator.calculatePawnMoves();

        moveTester = new MoveTester(position, opponentAttacks);
        var castleGenerator = new CastleGenerator(position, moveTester);
        var castles = castleGenerator.calculateCastles();

        var pseudoLegals = Stream.concat(Stream.concat(attacks, moves), Stream.concat(pawnMoves, castles))
                .toList();

        var legalityChecker = new LegalityChecker(position, new MoveMaker());
        legals = legalityChecker.checkPseudoLegals(pseudoLegals);

        playerFactory = new PlayerFactory(position, moveTester, legals);
    }

    public Player createPlayer(Color color) {
        return playerFactory.create(color);
    }

    public MoveResult testMove(Move move) {
        return moveTester.testLegalMove(move);
    }

    public List<Move> findLegalMoves(Piece piece) {
        return legals.stream().filter(move -> move.piece().equals(piece)).toList();
    }
}
