/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.piece.Rook;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.eclipse.jdt.annotation.Nullable;

// TODO: Replace Move implementations with an enum
/** The action of moving a piece. */
@EqualsAndHashCode
public class Move {

  @Getter private final MoveType type;
  private final Board board;

  @Getter private final Piece piece;

  @Getter private final Coordinate destination;

  @Getter @Nullable private final Piece otherPiece;
  @Nullable private final Coordinate rookDestination;

  public Move(MoveType type, Board board, Piece piece, Coordinate destination) {
    this(type, board, piece, destination, null);
  }

  public Move(
      MoveType type, Board board, Piece piece, Coordinate destination, @Nullable Piece otherPiece) {
    this(type, board, piece, destination, otherPiece, null);
  }

  public Move(
      MoveType type,
      Board board,
      Piece piece,
      Coordinate destination,
      @Nullable Piece otherPiece,
      @Nullable Coordinate rookDestination) {
    this.type = type;
    this.board = board;
    this.piece = piece;
    this.destination = destination;
    this.otherPiece = otherPiece;
    this.rookDestination = rookDestination;
  }

  public boolean isCapturing() {
    return otherPiece != null && rookDestination == null;
  }

  public boolean isCastling() {
    return otherPiece != null && rookDestination != null;
  }

  public boolean isNull() {
    return piece.getPosition() == destination;
  }

  // TODO: Fix en passant implementation, highlighted moves don't match moves that are executed. En
  // passant pawns are set properly, but it doesn't get added to the list of legal moves
  /**
   * When a move is performed, a new board is created, because the board class is immutable.
   *
   * @return The new board, after the move was performed
   */
  public Board execute() {
    var builder = board.nextTurnBuilder();

    builder.without(piece).without(otherPiece).with(piece.move(this));

    if (type == MoveType.PAWN_JUMP) {
      builder.enPassantPawn((Pawn) piece.move(this));
    }

    if (isCastling()) {
      builder.with(new Rook(rookDestination, otherPiece.getAlliance(), false));
    }

    return builder.build();
  }

  public Coordinate getSource() {
    return piece.getPosition();
  }

  @Override
  public String toString() {
    return switch (type) {
      case CAPTURE -> piece.toSingleChar() + getDestination().toString();
      case PAWN_CAPTURE -> String.format("%sx%s", piece.getPosition().column(), getDestination());
      case KING_CASTLE -> "0-0";
      case QUEEN_CASTLE -> "0-0-0";
      default -> getDestination().toString();
    };
  }
}
