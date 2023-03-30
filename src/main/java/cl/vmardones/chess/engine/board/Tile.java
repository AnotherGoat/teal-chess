/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import cl.vmardones.chess.engine.piece.Piece;
import java.util.List;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.eclipse.jdt.annotation.Nullable;

/** A single chess tile, which may or may not contain a piece. */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public abstract class Tile {

  private final Coordinate coordinate;

  private static final List<EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

  private static List<EmptyTile> createAllPossibleEmptyTiles() {
    return IntStream.range(Board.MIN_TILES, Board.MAX_TILES)
        .mapToObj(Coordinate::of)
        .map(EmptyTile::new)
        .toList();
  }

  /**
   * Factory method for creating a new tile.
   *
   * @param coordinate The tile's coordinate
   * @param piece The piece on the tile
   * @return A new tile
   */
  public static Tile create(final Coordinate coordinate, final Piece piece) {
    return piece != null
        ? new OccupiedTile(coordinate, piece)
        : EMPTY_TILES_CACHE.get(coordinate.index());
  }

  /**
   * Obtains the piece contained by the tile.
   *
   * @return Piece on the tile
   */
  public abstract @Nullable Piece getPiece();

  private static final class EmptyTile extends Tile {

    private EmptyTile(final Coordinate coordinate) {
      super(coordinate);
    }

    @Override
    public @Nullable Piece getPiece() {
      return null;
    }

    @Override
    public String toString() {
      return "-";
    }
  }

  private static final class OccupiedTile extends Tile {

    private final Piece piece;

    private OccupiedTile(final Coordinate coordinate, final Piece piece) {
      super(coordinate);
      this.piece = piece;
    }

    @Override
    public @Nullable Piece getPiece() {
      return piece;
    }

    @Override
    public String toString() {
      return piece.toSingleChar();
    }
  }
}
