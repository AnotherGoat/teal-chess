/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.board.Tile;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveType;
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
  default List<Move> calculateLegals(Board board) {
    return calculatePossibleDestinations(board).stream()
        .map(board::tileAt)
        .filter(this::canAccess)
        .map(tile -> createMove(tile, board))
        .filter(Objects::nonNull)
        .toList();
  }

  List<Coordinate> calculatePossibleDestinations(Board board);

  default boolean isInMoveRange(Board board, Coordinate coordinate) {
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

  default boolean isAllyOf(Piece other) {
    return getAlliance() == other.getAlliance();
  }

  default boolean isEnemyOf(Piece other) {
    return !isAllyOf(other);
  }

  default String toSingleChar() {
    var singleChar = getClass().getSimpleName().substring(0, 1);

    return isBlack() ? singleChar.toLowerCase() : singleChar;
  }

  /**
   * Checks if this piece can get to the given destination. This happens only if the destination is
   * free or has a piece that can be captured.
   *
   * @param destination The target destination
   * @return True if the piece can get to the destination
   */
  default boolean canAccess(Tile destination) {
    var pieceAtDestination = destination.getPiece();
    return pieceAtDestination == null || isEnemyOf(pieceAtDestination);
  }

  Piece move(Move move);

  /**
   * Creates a move, based on the piece and the destination.
   *
   * @param destination The destination tile
   * @param board The current game board
   * @return A move, selected depending on the source and destination
   */
  default @Nullable Move createMove(Tile destination, Board board) {
    if (destination.getPiece() == null) {
      return new Move(MoveType.NORMAL, board, this, destination.getCoordinate());
    }

    var capturablePiece = destination.getPiece();

    if (isEnemyOf(capturablePiece)) {
      return new Move(MoveType.CAPTURE, board, this, destination.getCoordinate(), capturablePiece);
    }

    return null;
  }
}
