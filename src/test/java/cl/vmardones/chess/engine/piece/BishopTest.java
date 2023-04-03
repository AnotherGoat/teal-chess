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
import cl.vmardones.chess.engine.piece.vector.Vertical;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BishopTest {

  @Mock Coordinate anywhere;
  @Mock Coordinate destination;
  @Mock Move move;

  @Test
  void constructor() {
    assertThat(new Bishop(anywhere, Alliance.BLACK)).matches(Bishop::isFirstMove);
  }

  @Test
  void toSingleChar() {
    assertThat(new Bishop(anywhere, Alliance.WHITE).toSingleChar()).isEqualTo("B");
    assertThat(new Bishop(anywhere, Alliance.BLACK).toSingleChar()).isEqualTo("b");
  }

  @Test
  void diagonalMove() {
    var bishop = new Bishop(anywhere, Alliance.BLACK);
    assertThat(bishop.getMoveVectors()).containsOnlyOnce(Diagonal.UP_RIGHT.getVector());
  }

  @Test
  void illegalMove() {
    var bishop = new Bishop(anywhere, Alliance.BLACK);
    assertThat(bishop.getMoveVectors()).doesNotContain(Vertical.UP.getVector());
  }

  @Test
  void move() {
    var bishopToMove = new Bishop(anywhere, Alliance.BLACK);

    when(move.destination()).thenReturn(destination);

    assertThat(bishopToMove.move(move))
        .isInstanceOf(Bishop.class)
        .matches(bishop -> bishop.getPosition().equals(destination))
        .matches(bishop -> !bishop.isFirstMove());
  }
}
