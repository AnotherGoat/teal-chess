/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Alliance;
import java.util.*;
import java.util.stream.IntStream;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The chess game board, made of 8x8 tiles. */
public final class Board {

  /** The chess board is a square. This is the number of tiles per side. */
  public static final int SIDE_LENGTH = 8;
  /**
   * The minimum number of tiles in the game board. This is meant to be used along with MAX_TILES to
   * easily fill the game board.
   */
  public static final int MIN_TILES = 0;
  /**
   * The maximum number of tiles in the game board. This is meant to be used along with MIN_TILES to
   * easily fill the game board.
   */
  public static final int MAX_TILES = SIDE_LENGTH * SIDE_LENGTH;

  private static final Logger LOG = LoggerFactory.getLogger(Board.class);

  private final List<Tile> tiles;
  private final King whiteKing;
  private final List<Piece> whitePieces;
  private final King blackKing;
  private final List<Piece> blackPieces;
  private final @Nullable Pawn enPassantPawn;

  /* Building the board */

  /**
   * The board is a complex object, so this builder is the standard method to create it. Both white
   * and black kings must be supplied for the board to be legal. This builder is intended for
   * creating a new board from scratch.
   *
   * @param whiteKing The white player's king.
   * @param blackKing The black player's king.
   * @return The board builder.
   */
  public static BoardBuilder builder(King whiteKing, King blackKing) {
    return new BoardBuilder(whiteKing, blackKing);
  }

  /**
   * A special builder intended to be used when players make a move. This can only be used after the
   * board has been initialized at least once. It keeps the current state of the board and lets you
   * specify only the differences from the previous turn.
   *
   * @return The next turn builder.
   */
  public BoardBuilder nextTurnBuilder() {
    return new BoardBuilder(this);
  }

  /* Checking the board */

  /**
   * Get the tile located at a specific coordinate.
   *
   * @param coordinate The coordinate to search.
   * @return The tile found.
   */
  public Tile tileAt(Coordinate coordinate) {
    return tiles.get(coordinate.index());
  }

  /**
   * Check whether the specified tile contains a type of piece or not.
   *
   * @param coordinate The coordinate to search.
   * @param pieceType The type of piece to search.
   * @return True if the tile has a piece of the specified type. Always returns false if the tile is
   *     empty.
   */
  public boolean contains(Coordinate coordinate, Class<? extends Piece> pieceType) {
    var piece = tileAt(coordinate).piece();

    return pieceType.isInstance(piece);
  }

  /**
   * Check if a specific tile is empty.
   *
   * @param coordinate The coordinate of the tile to check.
   * @return True if the tile doesn't have a piece.
   */
  public boolean isEmpty(Coordinate coordinate) {
    return tileAt(coordinate).piece() == null;
  }

  /* Getters */

  public King whiteKing() {
    return whiteKing;
  }

  public List<Piece> whitePieces() {
    return whitePieces;
  }

  public King blackKing() {
    return blackKing;
  }

  public List<Piece> blackPieces() {
    return blackPieces;
  }

  public @Nullable Pawn enPassantPawn() {
    return enPassantPawn;
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

    var other = (Board) o;
    return tiles.equals(other.tiles)
        && whiteKing.equals(other.whiteKing)
        && whitePieces.equals(other.whitePieces)
        && blackKing.equals(other.blackKing)
        && blackPieces.equals(other.blackPieces)
        && Objects.equals(enPassantPawn, other.enPassantPawn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tiles, whitePieces, whiteKing, blackPieces, blackKing, enPassantPawn);
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();

    IntStream.range(MIN_TILES, MAX_TILES)
        .mapToObj(Coordinate::of)
        .map(coordinate -> String.format(getFormat(coordinate), tileAt(coordinate)))
        .forEach(builder::append);

    return builder.toString();
  }

  private Board(BoardBuilder builder) {
    tiles = createTiles(builder);

    whiteKing = builder.whiteKing;
    whitePieces = calculateActivePieces(tiles, Alliance.WHITE);

    blackKing = builder.blackKing;
    blackPieces = calculateActivePieces(tiles, Alliance.BLACK);

    enPassantPawn = builder.enPassantPawn;
  }

  private List<Tile> createTiles(BoardBuilder builder) {
    return IntStream.range(MIN_TILES, MAX_TILES)
        .mapToObj(Coordinate::of)
        .map(coordinate -> Tile.create(coordinate, builder.configuration.get(coordinate)))
        .toList();
  }

  private List<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance alliance) {
    return gameBoard.stream()
        .map(Tile::piece)
        .filter(piece -> piece != null && piece.getAlliance() == alliance)
        .toList();
  }

  private String getFormat(Coordinate coordinate) {
    return (coordinate.index() + 1) % Board.SIDE_LENGTH == 0 ? "%s  \n" : "%s  ";
  }

  /**
   * Responsible for building the board, a complex object.
   *
   * @see Board
   */
  public static class BoardBuilder {

    private final Map<Coordinate, Piece> configuration = new HashMap<>();
    private final King whiteKing;
    private final King blackKing;
    private @Nullable Pawn enPassantPawn;

    /**
     * Add a piece to the board. This action is silently ignored if the piece is null. If multiple
     * pieces are put in the same place, the last one takes precedence. Both the white and black
     * kings are added automatically when the build finishes.
     *
     * @param piece The piece to add.
     * @return The same instance of this builder, to continue the building process.
     */
    public BoardBuilder with(@Nullable Piece piece) {
      if (piece == null) {
        return this;
      }

      configuration.put(piece.getPosition(), piece);
      return this;
    }

    /**
     * Remove a piece from the board. This action is silently ignored if the piece is null.
     *
     * @param piece The piece to remove.
     * @return The same instance of this builder, to continue the building process.
     */
    public BoardBuilder without(@Nullable Piece piece) {
      if (piece == null) {
        return this;
      }

      configuration.remove(piece.getPosition(), piece);
      return this;
    }

    /**
     * Set the pawn that performed a pawn jump in the previous turn, which can be captured by an
     * enemy pawn's en passant move. By default, no en passant pawn is set.
     *
     * @param pawn The pawn that performed the pawn jump.
     * @return The same instance of this builder, to continue the building process.
     */
    public BoardBuilder enPassantPawn(Pawn pawn) {
      this.enPassantPawn = pawn;
      return this;
    }

    /**
     * Finish the building process of the board.
     *
     * @return The finished, unmodifiable board.
     */
    public Board build() {
      with(whiteKing);
      with(blackKing);

      return new Board(this);
    }

    private BoardBuilder(King whiteKing, King blackKing) {
      this.whiteKing = whiteKing;
      this.blackKing = blackKing;
    }

    private BoardBuilder(Board board) {
      this(board.whiteKing, board.blackKing);

      board.whitePieces().forEach(this::with);
      board.blackPieces().forEach(this::with);
    }
  }
}
