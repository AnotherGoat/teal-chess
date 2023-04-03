/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.vector.Diagonal;
import cl.vmardones.chess.engine.piece.vector.Horizontal;
import cl.vmardones.chess.engine.piece.vector.LShaped;
import cl.vmardones.chess.engine.piece.vector.Vertical;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KingTest {

  @Mock Coordinate anywhere;
  @Mock Coordinate destination;
  @Mock Move move;

  @Test
  void constructor() {
    assertThat(new King(anywhere, Alliance.BLACK)).matches(King::isFirstMove);
  }

  @Test
  void toSingleChar() {
    assertThat(new King(anywhere, Alliance.WHITE).toSingleChar()).isEqualTo("K");
    assertThat(new King(anywhere, Alliance.BLACK).toSingleChar()).isEqualTo("k");
  }

  @Test
  void diagonalMove() {
    var king = new King(anywhere, Alliance.WHITE);
    assertThat(king.getMoveOffsets()).containsOnlyOnce(Diagonal.UP_RIGHT.getVector());
  }

  @Test
  void horizontalMove() {
    var king = new King(anywhere, Alliance.WHITE);
    assertThat(king.getMoveOffsets()).containsOnlyOnce(Horizontal.LEFT.getVector());
  }

  @Test
  void verticalMove() {
    var king = new King(anywhere, Alliance.WHITE);
    assertThat(king.getMoveOffsets()).containsOnlyOnce(Vertical.UP.getVector());
  }

  @Test
  void illegalMove() {
    var king = new King(anywhere, Alliance.WHITE);
    assertThat(king.getMoveOffsets()).doesNotContain(LShaped.UP_UP_LEFT.getVector());
  }

  @Test
  void move() {
    var kingToMove = new King(anywhere, Alliance.WHITE);

    when(move.destination()).thenReturn(destination);

    assertThat(kingToMove.move(move))
        .isInstanceOf(King.class)
        .matches(king -> king.getPosition().equals(destination))
        .matches(king -> !king.isFirstMove());
  }
}
