/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.vector.LShaped;
import cl.vmardones.chess.engine.piece.vector.Vector;
import cl.vmardones.chess.engine.player.Alliance;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/** The knight piece. It moves in an L shape. */
@AllArgsConstructor
@Getter
@ToString(includeFieldNames = false)
public final class Knight implements JumpingPiece {

  private static final List<int[]> MOVE_OFFSETS = calculateMoveOffsets();

  private Coordinate position;
  private Alliance alliance;
  private boolean firstMove;

  public Knight(final Coordinate position, final Alliance alliance) {
    this(position, alliance, true);
  }

  @Override
  public String toSingleChar() {
    return isBlack() ? "n" : "N";
  }

  @Override
  public Knight move(final Move move) {
    return new Knight(move.getDestination(), alliance, false);
  }

  @Override
  public List<int[]> getMoveOffsets() {
    return MOVE_OFFSETS;
  }

  private static List<int[]> calculateMoveOffsets() {
    return Arrays.stream(LShaped.values()).map(Vector::getVector).toList();
  }
}
