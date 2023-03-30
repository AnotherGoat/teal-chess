/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.piece.Bishop;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Queen;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardTest {

  Board.BoardBuilder builder;

  @Mock King whiteKing;
  @Mock King blackKing;
  @Mock Pawn enPassantPawn;

  @BeforeEach
  void setUp() {
    builder = Board.builder(whiteKing, blackKing);
  }

  @Test
  void contains() {
    var piece = new Bishop(Coordinate.of("e2"), Alliance.WHITE);
    var board = builder.piece(piece).build();

    assertThat(board.contains(Coordinate.of("e2"), Bishop.class)).isTrue();
  }

  @Test
  void containsNothing() {
    var board = builder.build();

    assertThat(board.containsNothing(Coordinate.of("a1"))).isTrue();
    assertThat(board.containsNothing(Coordinate.of("h8"))).isTrue();
  }

  @Test
  void nextTurnBuilder() {
    var board = builder.build();
    var nextTurnBoard = board.nextTurnBuilder().build();

    assertThat(board.getWhiteKing()).isEqualTo(nextTurnBoard.getWhiteKing());
    assertThat(board.getBlackKing()).isEqualTo(nextTurnBoard.getBlackKing());
  }

  @Test
  void addPiece() {
    var piece = new Queen(Coordinate.of("d7"), Alliance.WHITE);
    var board = builder.piece(piece).build();

    assertThat(board.getWhitePieces()).containsOnlyOnce(piece);
  }

  @Test
  void withoutPiece() {
    var piece = new Pawn(Coordinate.of("a5"), Alliance.BLACK);
    var board = builder.piece(piece).withoutPiece(piece).build();

    assertThat(board.getBlackPieces()).doesNotContain(piece);
  }

  @Test
  void enPassantPawn() {
    var board = builder.enPassantPawn(enPassantPawn).build();

    assertThat(board.getEnPassantPawn()).isEqualTo(enPassantPawn);
  }

  @Test
  void noEnPassantPawnNextTurn() {
    var board = builder.enPassantPawn(enPassantPawn).build();
    var nextTurnBoard = board.nextTurnBuilder().build();

    assertThat(board.getEnPassantPawn()).isEqualTo(enPassantPawn);
    assertThat(nextTurnBoard.getEnPassantPawn()).isNull();
  }
}
