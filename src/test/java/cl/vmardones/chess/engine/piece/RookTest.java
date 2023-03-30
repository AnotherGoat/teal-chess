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
import cl.vmardones.chess.engine.piece.vector.Vertical;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RookTest {

  @Mock Coordinate anywhere;
  @Mock Coordinate destination;
  @Mock Move move;

  @Test
  void constructor() {
    assertThat(new Rook(anywhere, Alliance.BLACK)).matches(Rook::isFirstMove);
  }

  @Test
  void toSingleChar() {
    assertThat(new Rook(anywhere, Alliance.WHITE).toSingleChar()).isEqualTo("R");
    assertThat(new Rook(anywhere, Alliance.BLACK).toSingleChar()).isEqualTo("r");
  }

  @Test
  void horizontalMove() {
    var rook = new Rook(anywhere, Alliance.BLACK);
    assertThat(rook.getMoveVectors()).containsOnlyOnce(Horizontal.RIGHT.getVector());
  }

  @Test
  void verticalMove() {
    var rook = new Rook(anywhere, Alliance.BLACK);
    assertThat(rook.getMoveVectors()).containsOnlyOnce(Vertical.UP.getVector());
  }

  @Test
  void illegalMove() {
    var rook = new Rook(anywhere, Alliance.BLACK);
    assertThat(rook.getMoveVectors()).doesNotContain(Diagonal.DOWN_RIGHT.getVector());
  }

  @Test
  void move() {
    var rookToMove = new Rook(anywhere, Alliance.BLACK);

    when(move.getDestination()).thenReturn(destination);

    assertThat(rookToMove.move(move))
        .isInstanceOf(Rook.class)
        .matches(rook -> rook.getPosition().equals(destination))
        .matches(rook -> !rook.isFirstMove());
  }
}
