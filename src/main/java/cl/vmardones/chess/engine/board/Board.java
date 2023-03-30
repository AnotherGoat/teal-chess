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
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/** The game board, made of 8x8 tiles. */
@Slf4j
@ToString
public class Board {
  public static final int SIDE_LENGTH = 8;
  public static final int MIN_TILES = 0;
  public static final int MAX_TILES = SIDE_LENGTH * SIDE_LENGTH;

  private final List<Tile> tiles;

  @Getter private final List<Piece> whitePieces;

  @Getter private final King whiteKing;

  @Getter private final List<Piece> blackPieces;

  @Getter private final King blackKing;

  @Getter private final Pawn enPassantPawn;

  private Board(BoardBuilder builder) {
    tiles = createTiles(builder);
    log.debug("Current gameboard: {}", tiles);

    whiteKing = builder.whiteKing;
    log.debug("White king: {}", whiteKing);
    whitePieces = calculateActivePieces(tiles, Alliance.WHITE);
    log.debug("White pieces: {}", whitePieces);

    blackKing = builder.blackKing;
    log.debug("Black king: {}", blackKing);
    blackPieces = calculateActivePieces(tiles, Alliance.BLACK);
    log.debug("Black pieces: {}", blackPieces);

    enPassantPawn = builder.enPassantPawn;
    log.debug("En passant pawn: {}", enPassantPawn);
  }

  private List<Tile> createTiles(BoardBuilder builder) {
    return IntStream.range(MIN_TILES, MAX_TILES)
        .mapToObj(Coordinate::of)
        .map(coordinate -> Tile.create(coordinate, builder.boardConfig.get(coordinate)))
        .toList();
  }

  private List<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance alliance) {
    return gameBoard.stream()
        .map(Tile::getPiece)
        .filter(piece -> piece != null && piece.getAlliance() == alliance)
        .toList();
  }

  /* Methods for checking the board */

  public Tile getTile(Coordinate coordinate) {
    return tiles.get(coordinate.index());
  }

  public boolean contains(Coordinate coordinate, Class<?> pieceType) {
    var piece = getTile(coordinate).getPiece();

    return pieceType.isInstance(piece);
  }

  public boolean containsNothing(Coordinate coordinate) {
    return getTile(coordinate).getPiece() == null;
  }

  /* Board builders */

  /**
   * The board is a complex object, so this builder is the standard method to create it. Both white
   * and black kings must be supplied for the board to be legal. This builder is intended for
   * creating a new board from scratch.
   *
   * @return The board builder
   */
  public static BoardBuilder builder(King whiteKing, King blackKing) {
    return new BoardBuilder(whiteKing, blackKing);
  }

  /**
   * A special builder intended to be used when players make a move. This can only be used after the
   * board has been initialized at least once. It keeps the current state of the board and lets you
   * specify only the differences from the previous turn.
   *
   * @return The next turn builder
   */
  public BoardBuilder nextTurnBuilder() {
    return new BoardBuilder(this);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class BoardBuilder {

    private final Map<Coordinate, Piece> boardConfig = new HashMap<>();
    private final King whiteKing;
    private final King blackKing;
    private Pawn enPassantPawn;

    private BoardBuilder(Board board) {
      whiteKing = board.whiteKing;
      blackKing = board.blackKing;

      board.getWhitePieces().forEach(this::piece);
      board.getBlackPieces().forEach(this::piece);
    }

    public BoardBuilder piece(Piece piece) {
      boardConfig.put(piece.getPosition(), piece);
      return this;
    }

    public BoardBuilder withoutPiece(Piece piece) {
      boardConfig.remove(piece.getPosition(), piece);
      return this;
    }

    public BoardBuilder enPassantPawn(Pawn pawn) {
      this.enPassantPawn = pawn;
      return this;
    }

    public Board build() {
      return new Board(this);
    }
  }
}
