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
import org.eclipse.jdt.annotation.Nullable;

/**
 * The entity that controls the pieces in one side of the board. It can be controlled either by a
 * human or an AI.
 */
public abstract sealed class Player permits ComputerPlayer, HumanPlayer {

  protected final Alliance alliance;
  protected final Board board;
  protected final King king;
  protected final List<Piece> pieces;
  protected final List<Move> legals;
  protected final List<Move> opponentLegals;

  private final boolean inCheck;
  private final boolean noEscapeMoves;

  /* Player creation */

  /**
   * Create a new player.
   *
   * @param alliance The player's side of the board.
   * @param board The chess board, used for finding the king and the player's pieces.
   * @param legals The legal moves of the player.
   * @param opponentLegals The legal moves of the player on the opposite side..
   */
  protected Player(Alliance alliance, Board board, List<Move> legals, List<Move> opponentLegals) {
    this.alliance = alliance;
    this.board = board;
    king = findKing(board);
    pieces = findPieces(board);
    this.legals = addCastles(legals);
    this.opponentLegals = opponentLegals;
    inCheck = !Player.calculateAttacksOnTile(king.getPosition(), opponentLegals).isEmpty();
    noEscapeMoves = calculateEscapeMoves();
  }

  /* Getters */

  public Alliance alliance() {
    return alliance;
  }

  public King king() {
    return king;
  }

  public List<Piece> pieces() {
    return pieces;
  }

  public List<Move> legals() {
    return legals;
  }

  /* Checking state */

  /**
   * Checks if the player is in check, which means that the king must be protected.
   *
   * @return True if the player is in check
   */
  public boolean inCheck() {
    return inCheck;
  }

  /**
   * Checks if the player is in checkmate, which means the game is lost. This happens when the king
   * is in check and there are no escape moves.
   *
   * @return True if the player is in checkmate
   */
  public boolean inCheckmate() {
    return inCheck() && noEscapeMoves;
  }

  /**
   * Checks if the player is in stalemate, which means that game ends in a tie. This happens when
   * the king isn't in check and there are no escape moves.
   *
   * @return True if the player is in stalemate
   */
  public boolean inInStalemate() {
    return !inCheck() && noEscapeMoves;
  }

  /* Performing moves */

  public MoveTransition makeMove(Player currentPlayer, Move move) {
    if (move.isNone()) {
      return new MoveTransition(board, move, MoveStatus.NONE);
    }

    if (isIllegal(move)) {
      return new MoveTransition(board, move, MoveStatus.ILLEGAL);
    }

    var kingAttacks =
        Player.calculateAttacksOnTile(currentPlayer.king().getPosition(), opponentLegals);

    if (!kingAttacks.isEmpty()) {
      return new MoveTransition(board, move, MoveStatus.CHECKS);
    }

    return new MoveTransition(move.execute(), move, MoveStatus.DONE);
  }

  /* toString */

  @Override
  public String toString() {
    if (inCheckmate()) {
      return String.format("%s Player, in checkmate!", alliance.name());
    }

    if (inCheck()) {
      return String.format("%s Player, in check!", alliance.name());
    }

    if (inInStalemate()) {
      return String.format("%s Player, in stalemate!", alliance.name());
    }

    return String.format("%s Player", alliance.name());
  }

  private King findKing(Board board) {
    return alliance == Alliance.WHITE ? board.whiteKing() : board.blackKing();
  }

  private List<Piece> findPieces(Board board) {
    return alliance == Alliance.WHITE ? board.whitePieces() : board.blackPieces();
  }

  private List<Move> addCastles(List<Move> legals) {
    return Stream.concat(legals.stream(), calculateCastles().stream()).toList();
  }

  // TODO: This method should probably be moved to board service
  private static List<Move> calculateAttacksOnTile(Coordinate kingPosition, List<Move> moves) {
    return moves.stream().filter(move -> kingPosition.equals(move.destination())).toList();
  }

  private boolean calculateEscapeMoves() {
    return legals.stream()
        .map(move -> makeMove(this, move))
        .noneMatch(transition -> transition.getMoveStatus() == MoveStatus.DONE);
  }

  private boolean isIllegal(Move move) {
    return !legals.contains(move);
  }

  // TODO: Refactor this method, maybe use combinator pattern
  // TODO: The player shouldn't be the one calculating the castles
  protected List<Move> calculateCastles() {

    if (castlingIsImpossible()) {
      return Collections.emptyList();
    }

    return Stream.of(generateCastleMove(true), generateCastleMove(false))
        .filter(Objects::nonNull)
        .toList();
  }

  private boolean castlingIsImpossible() {
    return !king.isFirstMove() || inCheck() || king.getPosition().column() != 'e';
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

  private boolean isTileRook(int offset) {
    var destination = king.getPosition().right(offset);

    return destination != null && board.contains(destination, Rook.class);
  }

  private boolean isUnreachableByEnemy(int offset) {
    var destination = king.getPosition().right(offset);

    return destination != null
        && Player.calculateAttacksOnTile(destination, opponentLegals).isEmpty();
  }
}
