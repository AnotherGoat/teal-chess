/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.vector.Diagonal;
import cl.vmardones.chess.engine.piece.vector.Vector;
import cl.vmardones.chess.engine.player.Alliance;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** The bishop piece. It can move diagonally. */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString(includeFieldNames = false)
public final class Bishop implements SlidingPiece {

  private static final List<int[]> MOVE_VECTORS = calculateMoveVectors();

  private Coordinate position;
  private Alliance alliance;
  private boolean firstMove;

  public Bishop(Coordinate position, Alliance alliance) {
    this(position, alliance, true);
  }

  @Override
  public Bishop move(Move move) {
    return new Bishop(move.destination(), alliance, false);
  }

  @Override
  public List<int[]> getMoveVectors() {
    return MOVE_VECTORS;
  }

  private static List<int[]> calculateMoveVectors() {
    return Arrays.stream(Diagonal.values()).map(Vector::getVector).toList();
  }
}
