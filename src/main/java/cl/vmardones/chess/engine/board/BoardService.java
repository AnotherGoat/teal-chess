/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.*;
import cl.vmardones.chess.engine.player.Alliance;
import java.util.*;
import java.util.stream.IntStream;

/** Provides utility functions to create and do calculations over the chess board. */
public final class BoardService {

  private static final Board CACHED_STANDARD_BOARD = generateStandardBoard();

  /* Board creation */

  /**
   * Creates a standard chessboard, which consists of a rank filled with 8 pawns on each side with a
   * formation of 8 major pieces behind.
   *
   * @return The standard chessboard.
   */
  public Board createStandardBoard() {
    return CACHED_STANDARD_BOARD;
  }

  /**
   * Given a chess board, calculate the legal moves for every piece.
   *
   * @param board The board in its current state.
   * @param pieces The pieces to search legal moves for.
   * @return All the legal moves.
   */
  public List<Move> calculateLegals(Board board, List<Piece> pieces) {
    return pieces.stream()
        .map(piece -> piece.calculateLegals(board))
        .flatMap(Collection::stream)
        .toList();
  }

  private static Board generateStandardBoard() {
    var whiteKing = new King(Coordinate.of("e1"), Alliance.WHITE);
    var blackKing = new King(Coordinate.of("e8"), Alliance.BLACK);

    var builder = Board.builder(whiteKing, blackKing);

    builder
        .with(new Rook(Coordinate.of("a8"), Alliance.BLACK))
        .with(new Knight(Coordinate.of("b8"), Alliance.BLACK))
        .with(new Bishop(Coordinate.of("c8"), Alliance.BLACK))
        .with(new Queen(Coordinate.of("d8"), Alliance.BLACK))
        .with(new Bishop(Coordinate.of("f8"), Alliance.BLACK))
        .with(new Knight(Coordinate.of("g8"), Alliance.BLACK))
        .with(new Rook(Coordinate.of("h8"), Alliance.BLACK));

    IntStream.range(8, 16)
        .mapToObj(Coordinate::of)
        .map(coordinate -> new Pawn(coordinate, Alliance.BLACK))
        .forEach(builder::with);

    IntStream.range(48, 56)
        .mapToObj(Coordinate::of)
        .map(coordinate -> new Pawn(coordinate, Alliance.WHITE))
        .forEach(builder::with);

    builder
        .with(new Rook(Coordinate.of("a1"), Alliance.WHITE))
        .with(new Knight(Coordinate.of("b1"), Alliance.WHITE))
        .with(new Bishop(Coordinate.of("c1"), Alliance.WHITE))
        .with(new Queen(Coordinate.of("d1"), Alliance.WHITE))
        .with(new Bishop(Coordinate.of("f1"), Alliance.WHITE))
        .with(new Knight(Coordinate.of("g1"), Alliance.WHITE))
        .with(new Rook(Coordinate.of("h1"), Alliance.WHITE));

    return builder.build();
  }
}
