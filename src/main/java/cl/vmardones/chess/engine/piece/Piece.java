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
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;

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
  default Collection<Move> calculateLegals(@NonNull final Board board) {
    return calculatePossibleDestinations(board).stream()
        .map(board::getTile)
        .filter(this::canAccess)
        .map(tile -> createMove(tile, board))
        .flatMap(Optional::stream)
        .collect(ImmutableList.toImmutableList());
  }

  Collection<Coordinate> calculatePossibleDestinations(@NonNull final Board board);

  default boolean isInMoveRange(@NonNull final Board board, @NonNull final Coordinate coordinate) {
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

  default boolean isAllyOf(@NonNull final Piece other) {
    return getAlliance() == other.getAlliance();
  }

  default boolean isEnemyOf(@NonNull final Piece other) {
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
  default boolean canAccess(@NonNull final Tile destination) {
    final var pieceAtDestination = destination.getPiece();
    return pieceAtDestination.isEmpty() || isEnemyOf(pieceAtDestination.get());
  }

  Piece move(final Move move);

  /**
   * Creates a move, based on the piece and the destination.
   *
   * @param destination The destination tile
   * @param board The current game board
   * @return A move, selected depending on the source and destination
   */
  default Optional<Move> createMove(@NonNull final Tile destination, @NonNull final Board board) {
    if (destination.getPiece().isEmpty()) {
      return Optional.of(new MajorMove(board, this, destination.getCoordinate()));
    }

    final var capturablePiece = destination.getPiece();

    if (capturablePiece.isPresent() && isEnemyOf(capturablePiece.get())) {
      return Optional.of(
          new CaptureMove(board, this, destination.getCoordinate(), capturablePiece.get()));
    }

    return Optional.empty();
  }
}
