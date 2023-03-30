/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;

/**
 * A piece that can move to a specific set of positions. It usually doesn't matter if there are
 * other pieces in the way.
 */
sealed interface JumpingPiece extends Piece permits King, Knight, Pawn {

  Collection<int[]> getMoveOffsets();

  @Override
  default Collection<Coordinate> calculatePossibleDestinations(@NonNull final Board board) {
    return getMoveOffsets().stream()
        .map(offset -> getPosition().to(offset[0], offset[1]))
        .flatMap(Optional::stream)
        .collect(ImmutableList.toImmutableList());
  }
}
