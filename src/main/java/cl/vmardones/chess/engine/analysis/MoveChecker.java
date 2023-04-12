/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Alliance;

/**
 * Responsible for analyzing the board and calculating legal moves.
 */
final class MoveChecker {

    //private final MoveFactory moveFactory;

    /**
     * Given a chessboard, calculate the legal moves for every piece of the requested alliance.
     *
     * @param board The board in its current state.
     * @param alliance The alliance to calculate legal moves for.
     * @return All the legal moves.
     */
    public static List<Move> calculateLegals(Board board, Alliance alliance) {

        var opponentLegals =
                calculateBasicLegals(board, alliance.opposite()).toList();
        var castleChecker = new CastleChecker(board, alliance, opponentLegals);

        var basicLegals = calculateBasicLegals(board, alliance);
        var castles = castleChecker.calculateCastles();

        return Stream.concat(basicLegals, castles).toList();
    }

    private static Stream<Move> calculateBasicLegals(Board board, Alliance alliance, List<Move> opponentLegals) {
        return board.pieces(alliance).stream()
                .map(piece -> calculatePieceLegals(piece, board, opponentLegals))
                .flatMap(Collection::stream);
    }

    /**
     * Calculates all the legal moves that a specific piece can do.
     *
     * @param piece The piece to check moves for.
     * @param board The current chessboard.
     * @return List of possible legal moves.
     */
    public static List<Move> calculatePieceLegals(Piece piece, Board board, List<Move> opponentLegals) {
        return piece.calculatePossibleDestinations(board).stream()
                .map(board::squareAt)
                .filter(piece::canAccess)
                .map(square -> new MoveFactory(board, opponentLegals).createMove(piece, square))
                .filter(Objects::nonNull)
                .toList();
    }
}
