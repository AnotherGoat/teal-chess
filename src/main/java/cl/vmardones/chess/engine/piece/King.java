/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.vector.Diagonal;
import cl.vmardones.chess.engine.piece.vector.Horizontal;
import cl.vmardones.chess.engine.piece.vector.Vector;
import cl.vmardones.chess.engine.piece.vector.Vertical;
import cl.vmardones.chess.engine.player.Alliance;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * The king piece. The most important piece in the game, must be defended at all costs. It moves
 * like the queen, but only one space at a time. It also cannot move into a position where it could
 * be captured.
 */
@Getter
@AllArgsConstructor
@ToString(includeFieldNames = false)
public final class King implements JumpingPiece {

  private static final List<int[]> MOVE_OFFSETS = calculateMoveOffsets();

  private Coordinate position;
  private Alliance alliance;
  private boolean firstMove;

  public King(final Coordinate position, final Alliance alliance) {
    this(position, alliance, true);
  }

  @Override
  public King move(final Move move) {
    return new King(move.getDestination(), alliance, false);
  }

  @Override
  public List<int[]> getMoveOffsets() {
    return MOVE_OFFSETS;
  }

  private static List<int[]> calculateMoveOffsets() {
    return Stream.concat(
            Arrays.stream(Diagonal.values()),
            Stream.concat(Arrays.stream(Horizontal.values()), Arrays.stream(Vertical.values())))
        .map(Vector::getVector)
        .toList();
  }
}
