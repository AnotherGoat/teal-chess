/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.board.Tile;
import java.util.*;
import java.util.stream.IntStream;

sealed interface SlidingPiece extends Piece permits Bishop, Queen, Rook {

  List<int[]> getMoveVectors();

  @Override
  default List<Coordinate> calculatePossibleDestinations(final Board board) {
    return getMoveVectors().stream()
        .map(vector -> calculateOffsets(vector, board))
        .flatMap(Collection::stream)
        .toList();
  }

  private List<Coordinate> calculateOffsets(final int[] vector, final Board board) {
    final var tiles =
        IntStream.range(1, Board.SIDE_LENGTH + 1)
            .mapToObj(i -> getPosition().to(vector[0] * i, vector[1] * i))
            .filter(Objects::nonNull)
            .map(board::getTile)
            .toList()
            .listIterator();

    return filterAccessible(tiles).stream().map(Tile::getCoordinate).toList();
  }

  // TODO: Replace this method with something more stream-friendly
  private List<Tile> filterAccessible(final Iterator<Tile> tiles) {

    final List<Tile> accessibleTiles = new ArrayList<>();

    while (tiles.hasNext()) {
      final var destination = tiles.next();
      final var pieceAtDestination = destination.getPiece();

      if (pieceAtDestination == null) {
        accessibleTiles.add(destination);
      } else {
        if (isEnemyOf(pieceAtDestination)) {
          accessibleTiles.add(destination);
        }

        break;
      }
    }

    return accessibleTiles;
  }
}
