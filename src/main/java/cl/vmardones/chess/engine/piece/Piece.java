/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.board.Tile;
import cl.vmardones.chess.engine.move.CaptureMove;
import cl.vmardones.chess.engine.move.MajorMove;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.player.Alliance;
import java.util.List;
import java.util.Objects;
import org.eclipse.jdt.annotation.Nullable;

/** A chess piece. */
public sealed interface Piece permits JumpingPiece, SlidingPiece {

  Coordinate getPosition();

  Alliance getAlliance();

  boolean isFirstMove();

  /**
   * Calculates all the moves that a piece can do.
   *
   * @param board Current state of the game board
   * @return List of possible moves
   */
  default List<Move> calculateLegals(final Board board) {
    return calculatePossibleDestinations(board).stream()
        .map(board::getTile)
        .filter(this::canAccess)
        .map(tile -> createMove(tile, board))
        .filter(Objects::nonNull)
        .toList();
  }

  List<Coordinate> calculatePossibleDestinations(final Board board);

  default boolean isInMoveRange(final Board board, final Coordinate coordinate) {
    return calculateLegals(board).stream()
        .map(Move::getDestination)
        .anyMatch(destination -> destination == coordinate);
  }

  default boolean isWhite() {
    return getAlliance() == Alliance.WHITE;
  }

  default boolean isBlack() {
    return !isWhite();
  }

  default boolean isAllyOf(final Piece other) {
    return getAlliance() == other.getAlliance();
  }

  default boolean isEnemyOf(final Piece other) {
    return !isAllyOf(other);
  }

  default String toSingleChar() {
    final var singleChar = getClass().getSimpleName().substring(0, 1);

    return isBlack() ? singleChar.toLowerCase() : singleChar;
  }

  /**
   * Checks if this piece can get to the given destination. This happens only if the destination is
   * free or has a piece that can be captured.
   *
   * @param destination The target destination
   * @return True if the piece can get to the destination
   */
  default boolean canAccess(final Tile destination) {
    final var pieceAtDestination = destination.getPiece();
    return pieceAtDestination == null || isEnemyOf(pieceAtDestination);
  }

  Piece move(final Move move);

  /**
   * Creates a move, based on the piece and the destination.
   *
   * @param destination The destination tile
   * @param board The current game board
   * @return A move, selected depending on the source and destination
   */
  default @Nullable Move createMove(final Tile destination, final Board board) {
    if (destination.getPiece() == null) {
      return new MajorMove(board, this, destination.getCoordinate());
    }

    final var capturablePiece = destination.getPiece();

    if (capturablePiece != null && isEnemyOf(capturablePiece)) {
      return new CaptureMove(board, this, destination.getCoordinate(), capturablePiece);
    }

    return null;
  }
}
