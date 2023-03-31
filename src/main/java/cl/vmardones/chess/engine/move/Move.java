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
import java.util.List;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jdt.annotation.Nullable;

// TODO: Replace Move implementations with an enum
/** The action of moving a piece. */
@Slf4j
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

  // TODO: Fix en passant implementation, highlighted moves don't match moves that are executed. En
  // passant pawns are set properly, but it doesn't get added to the list of legal moves
  /**
   * When a move is performed, a new board is created, because the board class is immutable.
   *
   * @return The new board, after the move was performed
   */
  public Board execute() {
    var builder = board.nextTurnBuilder();

    builder.withoutPiece(piece).withoutPiece(otherPiece).piece(piece.move(this));

    if (type == MoveType.PAWN_JUMP) {
      builder.enPassantPawn((Pawn) piece.move(this));
    }

    if (isCastling()) {
      builder.piece(getMovedRook());
    }

    return builder.build();
  }

  private Rook getMovedRook() {
    return new Rook(rookDestination, otherPiece.getAlliance(), false);
  }

  public Coordinate getSource() {
    return piece.getPosition();
  }

  @Override
  public String toString() {
    return switch (type) {
      case CAPTURE -> piece.toSingleChar() + getDestination().toString();
      case PAWN_CAPTURE -> String.format(
          "%sx%s", piece.getPosition().getColumn(), getDestination());
      case KING_CASTLE -> "0-0";
      case QUEEN_CASTLE -> "0-0-0";
      default -> getDestination().toString();
    };
  }

  public boolean isNull() {
    return piece.getPosition() == destination;
  }

  public static final class MoveFactory {

    @Generated
    private MoveFactory() {
      throw new UnsupportedOperationException("You cannot instantiate me!");
    }

    /**
     * Creates a move in the specified direction.
     *
     * @param source Source coordinate
     * @param destination Destination coordinate
     * @return Move that goes from the source to the destination, if possible.
     */
    public static @Nullable Move create(
        List<Move> currentPlayerLegals, Coordinate source, Coordinate destination) {
      if (source.equals(destination)) {
        return null;
      }

      log.debug(
          "Current piece legals: {}",
          currentPlayerLegals.stream().filter(move -> move.getSource().equals(source)).toList());

      return currentPlayerLegals.stream()
          .filter(isMovePossible(source, destination))
          .findFirst()
          .orElse(null);
    }

    private static Predicate<Move> isMovePossible(Coordinate source, Coordinate destination) {
      return move -> move.getSource().equals(source) && move.getDestination().equals(destination);
    }
  }
}
