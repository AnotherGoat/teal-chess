/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import cl.vmardones.chess.engine.piece.Piece;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import org.eclipse.jdt.annotation.Nullable;

/** A single chess tile, which may or may not contain a piece. */
public final class Tile {

  private static final List<Tile> CACHED_EMPTY_TILES = createAllPossibleEmptyTiles();

  private final Coordinate coordinate;
  private final @Nullable Piece piece;

  /* Tile creation */

  /**
   * Static factory method for creating a new tile.
   *
   * @param coordinate The tile's coordinate.
   * @param piece The piece on the tile.
   * @return A new tile.
   */
  public static Tile create(Coordinate coordinate, @Nullable Piece piece) {
    return piece != null ? new Tile(coordinate, piece) : CACHED_EMPTY_TILES.get(coordinate.index());
  }

  /* Getters */

  public Coordinate coordinate() {
    return coordinate;
  }

  public @Nullable Piece piece() {
    return piece;
  }

  /* equals, hashCode and toString */

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    var other = (Tile) o;
    return coordinate.equals(other.coordinate) && Objects.equals(piece, other.piece);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinate, piece);
  }

  /**
   * String representation of this tile, used when displaying the board.
   *
   * @return The character that represents the piece, or - if the tile is empty.
   */
  @Override
  public String toString() {
    if (piece == null) {
      return "-";
    }

    return piece.singleChar();
  }

  private static List<Tile> createAllPossibleEmptyTiles() {
    return IntStream.range(Board.MIN_TILES, Board.MAX_TILES)
        .mapToObj(Coordinate::of)
        .map(Tile::new)
        .toList();
  }

  private Tile(Coordinate coordinate, @Nullable Piece piece) {
    this.coordinate = coordinate;
    this.piece = piece;
  }

  private Tile(Coordinate coordinate) {
    this(coordinate, null);
  }
}
