/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import java.util.List;
import java.util.stream.Stream;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveResult;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Alliance;
import cl.vmardones.chess.engine.player.Player;

public final class BoardAnalyzer {

    private final List<Move> legals;
    private final MoveTester moveTester;
    private final PlayerFactory playerFactory;

    public BoardAnalyzer(Board board, Alliance nextMoveMaker) {
        var king = board.king(nextMoveMaker);
        var pieces = board.pieces(nextMoveMaker);

        var opponentKing = board.king(nextMoveMaker.opposite());
        var opponentPieces = board.pieces(nextMoveMaker.opposite());

        var attackGenerator = new AttackGenerator(board, nextMoveMaker, pieces, opponentPieces);
        var opponentAttacks =
                attackGenerator.calculateAttacks(nextMoveMaker.opposite()).toList();
        var attacks = attackGenerator.calculateAttacks(nextMoveMaker);

        var moveGenerator = new MoveGenerator(board, pieces);
        var moves = moveGenerator.calculateMoves();

        var pawnMoveGenerator = new PawnMoveGenerator(board, nextMoveMaker, pieces);
        var pawnMoves = pawnMoveGenerator.calculatePawnMoves();

        moveTester = new MoveTester(king, opponentAttacks);
        var castleGenerator = new CastleGenerator(moveTester, board, king);
        var castles = castleGenerator.calculateCastles();

        legals = Stream.concat(Stream.concat(attacks, moves), Stream.concat(pawnMoves, castles))
                .toList();
        playerFactory =
                new PlayerFactory(moveTester, nextMoveMaker, king, pieces, legals, opponentKing, opponentPieces);
    }

    public Player createPlayer(Alliance alliance) {
        return playerFactory.create(alliance);
    }

    public MoveResult testMove(Move move) {
        return moveTester.testLegalMove(move);
    }

    public List<Move> findLegalMoves(Piece piece) {
        return legals.stream().filter(move -> move.piece().equals(piece)).toList();
    }
}
