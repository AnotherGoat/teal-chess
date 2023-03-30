/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import cl.vmardones.chess.engine.piece.Piece;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Getter;
import org.eclipse.jdt.annotation.Nullable;

/** A single chess tile, which may or may not contain a piece. */
@Getter
public class Tile {

  private final Coordinate coordinate;
  private final @Nullable Piece piece;

  private static final List<Tile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

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

  /**
   * Factory method for creating a new tile.
   *
   * @param coordinate The tile's coordinate
   * @param piece The piece on the tile
   * @return A new tile
   */
  public static Tile create(Coordinate coordinate, @Nullable Piece piece) {
    return piece != null ? new Tile(coordinate, piece) : EMPTY_TILES_CACHE.get(coordinate.index());
  }

  /**
   * Obtains the piece contained by the tile.
   *
   * @return Piece on the tile
   */
  public @Nullable Piece getPiece() {
    return piece;
  }

  @Override
  public String toString() {
    if (piece == null) {
      return "-";
    }

    return piece.toSingleChar();
  }
}
