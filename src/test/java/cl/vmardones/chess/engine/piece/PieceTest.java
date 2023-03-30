/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.board.Tile;
import cl.vmardones.chess.engine.move.MoveType;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PieceTest {

  @Mock Coordinate anywhere;
  @Mock Tile destinationTile;

  @Test
  void isWhite() {
    var piece = new Knight(anywhere, Alliance.WHITE);
    assertThat(piece.isWhite()).isTrue();
  }

  @Test
  void isNotWhite() {
    var piece = new King(anywhere, Alliance.BLACK);
    assertThat(piece.isWhite()).isFalse();
  }

  @Test
  void isBlack() {
    var piece = new Pawn(anywhere, Alliance.BLACK);
    assertThat(piece.isBlack()).isTrue();
  }

  @Test
  void isNotBlack() {
    var piece = new Queen(anywhere, Alliance.WHITE);
    assertThat(piece.isBlack()).isFalse();
  }

  @Test
  void isAllyOf() {
    var first = new Pawn(anywhere, Alliance.WHITE);
    var second = new Rook(anywhere, Alliance.WHITE);

    assertThat(first.isAllyOf(second)).isTrue();
    assertThat(first.isEnemyOf(second)).isFalse();
    assertThat(second.isAllyOf(first)).isTrue();
    assertThat(second.isEnemyOf(first)).isFalse();
  }

  @Test
  void isEnemyOf() {
    var first = new Bishop(anywhere, Alliance.WHITE);
    var second = new Bishop(anywhere, Alliance.BLACK);

    assertThat(first.isAllyOf(second)).isFalse();
    assertThat(first.isEnemyOf(second)).isTrue();
    assertThat(second.isAllyOf(first)).isFalse();
    assertThat(second.isEnemyOf(first)).isTrue();
  }

  @Test
  void isEmptyAccesible() {
    var piece = new Rook(anywhere, Alliance.BLACK);

    when(destinationTile.getPiece()).thenReturn(null);

    assertThat(piece.canAccess(destinationTile)).isTrue();
  }

  @Test
  void isEnemyAccessible() {
    var piece = new Bishop(anywhere, Alliance.BLACK);
    var destinationPiece = new Pawn(anywhere, Alliance.WHITE);

    when(destinationTile.getPiece()).thenReturn(destinationPiece);

    assertThat(piece.canAccess(destinationTile)).isTrue();
  }

  @Test
  void isNotAccesible() {
    var piece = new Queen(anywhere, Alliance.BLACK);
    var destinationPiece = new King(anywhere, Alliance.BLACK);

    when(destinationTile.getPiece()).thenReturn(destinationPiece);

    assertThat(piece.canAccess(destinationTile)).isFalse();
  }

  @Test
  void createMajorMove() {
    var whiteKing = new King(Coordinate.of("e1"), Alliance.WHITE);
    var blackKing = new King(Coordinate.of("e8"), Alliance.BLACK);

    var piece = new Rook(Coordinate.of("a1"), Alliance.WHITE);

    var board = Board.builder(whiteKing, blackKing).piece(piece).build();

    var destination = board.getTile(Coordinate.of("a7"));

    assertThat(piece.createMove(destination, board).getType()).isEqualTo(MoveType.NORMAL);
  }

  @Test
  void createCaptureMove() {
    var whiteKing = new King(Coordinate.of("e1"), Alliance.WHITE);
    var blackKing = new King(Coordinate.of("e8"), Alliance.BLACK);

    var piece = new Rook(Coordinate.of("a1"), Alliance.WHITE);
    var capturablePiece = new Pawn(Coordinate.of("a7"), Alliance.BLACK);

    var board = Board.builder(whiteKing, blackKing).piece(piece).piece(capturablePiece).build();

    var destination = board.getTile(Coordinate.of("a7"));

    assertThat(piece.createMove(destination, board).getType()).isEqualTo(MoveType.CAPTURE);
  }

  @Test
  void dontCreateMove() {
    var whiteKing = new King(Coordinate.of("e1"), Alliance.WHITE);
    var blackKing = new King(Coordinate.of("e8"), Alliance.BLACK);

    var piece = new Rook(Coordinate.of("a1"), Alliance.WHITE);
    var blockingPiece = new Pawn(Coordinate.of("a7"), Alliance.WHITE);

    var board = Board.builder(whiteKing, blackKing).piece(piece).piece(blockingPiece).build();

    var destination = board.getTile(Coordinate.of("a7"));

    assertThat(piece.createMove(destination, board)).isNull();
  }
}
