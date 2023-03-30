/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.board.Tile;
import cl.vmardones.chess.engine.move.*;
import cl.vmardones.chess.engine.piece.vector.Diagonal;
import cl.vmardones.chess.engine.piece.vector.Jump;
import cl.vmardones.chess.engine.piece.vector.Vector;
import cl.vmardones.chess.engine.piece.vector.Vertical;
import cl.vmardones.chess.engine.player.Alliance;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The pawn piece. It only moves forward (depending on the side) and can eat other pieces
 * diagonally. A very weak piece, but it can be promoted when getting to the last rank at the
 * opposite side.
 */
@Getter
@AllArgsConstructor
@ToString(includeFieldNames = false)
@Slf4j
public final class Pawn implements JumpingPiece {

  private Coordinate position;
  private Alliance alliance;
  private boolean firstMove;

  public Pawn(final Coordinate position, final Alliance alliance) {
    this(position, alliance, true);
  }

  @Override
  public @Nullable Move createMove(final Tile destination, final Board board) {

    if (isCaptureMove(destination)) {
      if (!isEnPassantPossible(board, destination)) {
        return createCaptureMove(board, destination);
      }

      log.debug("En passant is possible!");
      return createEnPassantMove(board, destination);
    }

    if (isJumpPossible(board, destination)) {
      return createJumpMove(board, destination);
    }

    return createForwardMove(board, destination);
  }

  private @Nullable Move createEnPassantMove(final Board board, final Tile destination) {
    final var enPassantMove =
        new EnPassantMove(board, this, destination.getCoordinate(), board.getEnPassantPawn());

    log.debug("Created en passant move: {}", enPassantMove);
    return enPassantMove;
  }

  private boolean isEnPassantPossible(final Board board, final Tile destination) {

    if (board.getEnPassantPawn() == null) {
      return false;
    }

    final var side = destination.getCoordinate().to(0, alliance.getOppositeDirection());

    if (side == null) {
      return false;
    }

    final var pieceAtSide = board.getTile(side).getPiece();

    return pieceAtSide != null
        && pieceAtSide.equals(board.getEnPassantPawn())
        && destination.getPiece() == null;
  }

  private boolean isCaptureMove(final Tile destination) {
    return !getPosition().sameColumnAs(destination.getCoordinate());
  }

  private @Nullable Move createCaptureMove(final Board board, final Tile destination) {
    final var capturablePiece = destination.getPiece();

    if (capturablePiece != null && isEnemyOf(capturablePiece)) {
      return new PawnCaptureMove(board, this, destination.getCoordinate(), capturablePiece);
    }

    return null;
  }

  private @Nullable Move createJumpMove(final Board board, final Tile destination) {
    return new PawnJump(board, this, destination.getCoordinate());
  }

  private boolean isJumpPossible(final Board board, final Tile destination) {

    final var forward = position.up(alliance.getDirection());

    if (forward == null) {
      return false;
    }

    return isFirstMove() && canAccess(board.getTile(forward)) && canAccess(destination);
  }

  private @Nullable Move createForwardMove(final Board board, final Tile destination) {
    if (destination.getPiece() != null) {
      return null;
    }

    // TODO: Deal with promotions
    return new PawnMove(board, this, destination.getCoordinate());
  }

  @Override
  public Pawn move(final Move move) {
    return new Pawn(move.getDestination(), alliance, false);
  }

  @Override
  public List<int[]> getMoveOffsets() {
    return switch (getAlliance()) {
      case BLACK -> calculateBlackOffsets();
      case WHITE -> calculateWhiteOffsets();
    };
  }

  private List<int[]> calculateWhiteOffsets() {
    final List<Vector> moves =
        new ArrayList<>(List.of(Vertical.UP, Diagonal.UP_LEFT, Diagonal.UP_RIGHT));

    if (isFirstMove()) {
      moves.add(Jump.UP);
    }

    return moves.stream().map(Vector::getVector).toList();
  }

  private List<int[]> calculateBlackOffsets() {
    final List<Vector> moves =
        new ArrayList<>(List.of(Vertical.DOWN, Diagonal.DOWN_LEFT, Diagonal.DOWN_RIGHT));

    if (isFirstMove()) {
      moves.add(Jump.DOWN);
    }

    return moves.stream().map(Vector::getVector).toList();
  }
}
