/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.player;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.move.*;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.piece.Rook;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.ToString;

/**
 * The entity that controls the pieces in one side of the board. It can be controlled either by a
 * human or an AI.
 */
@ToString
public abstract class Player {

  @ToString.Exclude protected final Board board;

  @Getter protected final King king;

  @Getter @ToString.Exclude protected final List<Move> legals;
  @ToString.Exclude protected final List<Move> opponentLegals;

  private final boolean inCheck;
  private Boolean noEscapeMoves;

  protected Player(
      final Board board,
      final King king,
      final List<Move> legals,
      final List<Move> opponentLegals) {
    this.board = board;
    this.king = king;
    this.opponentLegals = opponentLegals;

    this.legals =
        Stream.concat(legals.stream(), calculateCastles(opponentLegals).stream()).toList();
    inCheck = !Player.calculateAttacksOnTile(king.getPosition(), opponentLegals).isEmpty();
  }

  protected static List<Move> calculateAttacksOnTile(
      final Coordinate kingPosition, final List<Move> moves) {
    return moves.stream().filter(move -> kingPosition == move.getDestination()).toList();
  }

  /**
   * Used to check if a specific move can be performed.
   *
   * @param move The move to check
   * @return True if the move is legal
   */
  public boolean isLegal(final Move move) {
    return legals.contains(move);
  }

  /**
   * Checks if the player is in check, which means that the king must be protected.
   *
   * @return True if the player is in check
   */
  public boolean isInCheck() {
    return inCheck;
  }

  /**
   * Checks if the player is in checkmate, which means the game is lost. This happens when the king
   * is in check and there are no escape moves.
   *
   * @return True if the player is in checkmate
   */
  public boolean isInCheckmate() {
    return isInCheck() && hasNoEscapeMoves();
  }

  private boolean hasNoEscapeMoves() {
    if (noEscapeMoves == null) {
      noEscapeMoves =
          legals.stream()
              .map(move -> makeMove(this, move))
              .noneMatch(transition -> transition.getMoveStatus().isDone());
    }

    return noEscapeMoves;
  }

  /**
   * Checks if the player is in stalemate, which means that game ends in a tie. This happens when
   * the king isn't in check and there are no escape moves.
   *
   * @return True if the player is in stalemate
   */
  public boolean inInStalemate() {
    return !isInCheck() && hasNoEscapeMoves();
  }

  public boolean isCastled() {
    return false;
  }

  public MoveTransition makeMove(final Player currentPlayer, final Move move) {
    if (move.isNull()) {
      return new MoveTransition(board, move, MoveStatus.NULL);
    }

    if (!isLegal(move)) {
      return new MoveTransition(board, move, MoveStatus.ILLEGAL);
    }

    final List<Move> kingAttacks =
        Player.calculateAttacksOnTile(currentPlayer.getKing().getPosition(), opponentLegals);

    if (!kingAttacks.isEmpty()) {
      return new MoveTransition(board, move, MoveStatus.LEAVES_OPPONENT_IN_CHECK);
    }

    return new MoveTransition(move.execute(), move, MoveStatus.DONE);
  }

  /**
   * Obtains the player's current pieces on the board.
   *
   * @return The player's active pieces
   */
  public abstract List<Piece> getActivePieces();

  /**
   * Obtains the player's side.
   *
   * @return The player's alliance
   */
  public abstract Alliance getAlliance();

  // TODO: Refactor this method, maybe use combinator pattern
  protected List<Move> calculateCastles(final List<Move> opponentLegals) {

    if (!king.isFirstMove() || isInCheck() || king.getPosition().getColumn() != 'e') {
      return Collections.emptyList();
    }

    final List<Move> castles = new ArrayList<>();
    final var kingPosition = king.getPosition();

    if (isKingSideCastlePossible(kingPosition, opponentLegals)) {
      final var rook = (Rook) board.getTile(kingPosition.right(3)).getPiece();
      final var kingDestination = kingPosition.right(2);
      final var rookDestination = kingPosition.right(1);

      if (rook.isFirstMove()) {
        castles.add(new KingSideCastleMove(board, king, kingDestination, rook, rookDestination));
      }
    }

    if (isQueenSideCastlePossible(kingPosition, opponentLegals)) {
      final var rook = (Rook) board.getTile(kingPosition.right(3)).getPiece();
      final var kingDestination = kingPosition.left(2);
      final var rookDestination = kingPosition.left(1);

      if (rook.isFirstMove()) {
        castles.add(new QueenSideCastleMove(board, king, kingDestination, rook, rookDestination));
      }
    }

    return Collections.unmodifiableList(castles);
  }

  private boolean isKingSideCastlePossible(
      final Coordinate kingPosition, final List<Move> opponentLegals) {
    return isTileFree(kingPosition, 1)
        && isTileFree(kingPosition, 2)
        && isTileRook(kingPosition, 3)
        && isUnreachableByEnemy(kingPosition, 1, opponentLegals)
        && isUnreachableByEnemy(kingPosition, 2, opponentLegals);
  }

  private boolean isQueenSideCastlePossible(
      final Coordinate kingPosition, final List<Move> opponentLegals) {
    return isTileFree(kingPosition, -1)
        && isTileFree(kingPosition, -2)
        && isTileFree(kingPosition, -3)
        && isTileRook(kingPosition, -4)
        && isUnreachableByEnemy(kingPosition, -1, opponentLegals)
        && isUnreachableByEnemy(kingPosition, -2, opponentLegals)
        && isUnreachableByEnemy(kingPosition, -3, opponentLegals);
  }

  private boolean isTileFree(final Coordinate kingPosition, final int offset) {
    final var destination = kingPosition.right(offset);

    return destination != null && board.containsNothing(destination);
  }

  private boolean isUnreachableByEnemy(
      final Coordinate kingPosition, final int offset, final List<Move> opponentLegals) {
    final var destination = kingPosition.right(offset);

    return destination != null
        && Player.calculateAttacksOnTile(destination, opponentLegals).isEmpty();
  }

  private boolean isTileRook(final Coordinate kingPosition, final int offset) {
    final var destination = kingPosition.right(offset);

    return destination != null && board.contains(destination, Rook.class);
  }
}
