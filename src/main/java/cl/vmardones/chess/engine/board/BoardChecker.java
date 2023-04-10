/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import cl.vmardones.chess.ExcludeFromGeneratedReport;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.player.Alliance;

/**
 * Responsible for analyzing the board and
 */
public final class BoardChecker {

    public static List<Move> isUnderAttack(Position target, List<Move> moves) {
        return moves.stream().filter(move -> target.equals(move.destination())).toList();
    }

    /**
     * Given a chessboard, calculate the legal moves for every piece of the requested alliance.
     *
     * @param board The board in its current state.
     * @param alliance The alliance to calculate legal moves for.
     * @return All the legal moves.
     */
    public static List<Move> calculateLegals(Board board, Alliance alliance) {

        var opponentBasicLegals =
                calculateBasicLegals(board, alliance.opposite()).toList();
        var castleChecker = new CastleChecker(board, alliance, opponentBasicLegals);

        var basicLegals = calculateBasicLegals(board, alliance);
        var castles = castleChecker.calculateCastles();

        return Stream.concat(basicLegals, castles).toList();
    }

    private static Stream<Move> calculateBasicLegals(Board board, Alliance alliance) {
        return board.pieces(alliance).stream()
                .map(piece -> piece.calculateLegals(board))
                .flatMap(Collection::stream);
    }

    @ExcludeFromGeneratedReport
    private BoardChecker() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
