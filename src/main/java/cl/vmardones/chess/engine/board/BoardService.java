/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import java.util.*;
import java.util.stream.IntStream;

import cl.vmardones.chess.ExcludeFromGeneratedReport;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.*;
import cl.vmardones.chess.engine.player.Alliance;

/** Provides utility functions to create and do calculations over the chessboard. */
public final class BoardService {

    private static final Board STANDARD_BOARD = generateStandardBoard();

    /* Board creation */

    /**
     * Creates a standard chessboard, which consists of a rank filled with 8 pawns on each side with a
     * formation of 8 major pieces behind.
     *
     * @return The standard chessboard.
     */
    public static Board createStandardBoard() {
        return STANDARD_BOARD;
    }

    /**
     * Given a chessboard, calculate the legal moves for every piece.
     *
     * @param board The board in its current state.
     * @param pieces The pieces to search legal moves for.
     * @return All the legal moves.
     */
    public static List<Move> calculateLegals(Board board, List<Piece> pieces) {
        return pieces.stream()
                .map(piece -> piece.calculateLegals(board))
                .flatMap(Collection::stream)
                .toList();
    }

    @ExcludeFromGeneratedReport
    private BoardService() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }

    private static Board generateStandardBoard() {
        var whiteKing = new King("e1", Alliance.WHITE);
        var blackKing = new King("e8", Alliance.BLACK);

        var builder = Board.builder(whiteKing, blackKing);

        builder.with(new Rook("a8", Alliance.BLACK))
                .with(new Knight("b8", Alliance.BLACK))
                .with(new Bishop("c8", Alliance.BLACK))
                .with(new Queen("d8", Alliance.BLACK))
                .with(new Bishop("f8", Alliance.BLACK))
                .with(new Knight("g8", Alliance.BLACK))
                .with(new Rook("h8", Alliance.BLACK));

        IntStream.range(8, 16)
                .mapToObj(AlgebraicConverter::toAlgebraic)
                .map(position -> new Pawn(position, Alliance.BLACK))
                .forEach(builder::with);

        IntStream.range(48, 56)
                .mapToObj(AlgebraicConverter::toAlgebraic)
                .map(position -> new Pawn(position, Alliance.WHITE))
                .forEach(builder::with);

        builder.with(new Rook("a1", Alliance.WHITE))
                .with(new Knight("b1", Alliance.WHITE))
                .with(new Bishop("c1", Alliance.WHITE))
                .with(new Queen("d1", Alliance.WHITE))
                .with(new Bishop("f1", Alliance.WHITE))
                .with(new Knight("g1", Alliance.WHITE))
                .with(new Rook("h1", Alliance.WHITE));

        return builder.build();
    }
}
