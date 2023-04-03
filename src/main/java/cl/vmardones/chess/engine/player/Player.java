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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.ToString;
import org.eclipse.jdt.annotation.Nullable;

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
  private @Nullable Boolean noEscapeMoves;

  protected Player(Board board, King king, List<Move> legals, List<Move> opponentLegals) {
    this.board = board;
    this.king = king;
    this.opponentLegals = opponentLegals;

    this.legals = Stream.concat(legals.stream(), calculateCastles().stream()).toList();
    inCheck = !Player.calculateAttacksOnTile(king.getPosition(), opponentLegals).isEmpty();
  }

  protected static List<Move> calculateAttacksOnTile(Coordinate kingPosition, List<Move> moves) {
    return moves.stream().filter(move -> kingPosition.equals(move.getDestination())).toList();
  }

  /**
   * Used to check if a specific move can be performed.
   *
   * @param move The move to check
   * @return True if the move is legal
   */
  public boolean isLegal(Move move) {
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

  public MoveTransition makeMove(Player currentPlayer, Move move) {
    if (move.isNull()) {
      return new MoveTransition(board, move, MoveStatus.NULL);
    }

    if (!isLegal(move)) {
      return new MoveTransition(board, move, MoveStatus.ILLEGAL);
    }

    List<Move> kingAttacks =
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
  protected List<Move> calculateCastles() {

    if (castlingIsImpossible()) {
      return Collections.emptyList();
    }

    return Stream.of(generateCastleMove(true), generateCastleMove(false))
        .filter(Objects::nonNull)
        .toList();
  }

  private boolean castlingIsImpossible() {
    return !king.isFirstMove() || isInCheck() || king.getPosition().column() != 'e';
  }

  private @Nullable Move generateCastleMove(boolean kingSide) {

    if (kingSide && !isKingSideCastlePossible() || !kingSide && !isQueenSideCastlePossible()) {
      return null;
    }

    // TODO: Only use king's column
    var kingPosition = king.getPosition();

    var rookOffset = kingSide ? 3 : -4;
    var rook = (Rook) board.tileAt(kingPosition.right(rookOffset)).piece();

    if (rook == null || !rook.isFirstMove()) {
      return null;
    }

    var direction = kingSide ? 1 : -1;
    var kingDestination = kingPosition.right(2 * direction);
    var rookDestination = kingPosition.right(direction);

    return new Move(
        kingSide ? MoveType.KING_CASTLE : MoveType.QUEEN_CASTLE,
        board,
        king,
        kingDestination,
        rook,
        rookDestination);
  }

  private boolean isKingSideCastlePossible() {
    return isTileFree(1)
        && isTileFree(2)
        && isTileRook(3)
        && isUnreachableByEnemy(1)
        && isUnreachableByEnemy(2);
  }

  private boolean isQueenSideCastlePossible() {
    return isTileFree(-1)
        && isTileFree(-2)
        && isTileFree(-3)
        && isTileRook(-4)
        && isUnreachableByEnemy(-1)
        && isUnreachableByEnemy(-2)
        && isUnreachableByEnemy(-3);
  }

  private boolean isTileFree(int offset) {
    var destination = king.getPosition().right(offset);

    return destination != null && board.isEmpty(destination);
  }

  private boolean isUnreachableByEnemy(int offset) {
    var destination = king.getPosition().right(offset);

    return destination != null
        && Player.calculateAttacksOnTile(destination, opponentLegals).isEmpty();
  }

  private boolean isTileRook(int offset) {
    var destination = king.getPosition().right(offset);

    return destination != null && board.contains(destination, Rook.class);
  }
}
