/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import java.util.List;
import java.util.Objects;

/**
 * A piece that can move to a specific set of positions. It usually doesn't matter if there are
 * other pieces in the way.
 */
sealed interface JumpingPiece extends Piece permits King, Knight, Pawn {

  List<int[]> getMoveOffsets();

  @Override
  default List<Coordinate> calculatePossibleDestinations(final Board board) {
    return getMoveOffsets().stream()
        .map(offset -> getPosition().to(offset[0], offset[1]))
        .filter(Objects::nonNull)
        .toList();
  }
}
