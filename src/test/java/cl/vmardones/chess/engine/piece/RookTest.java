/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RookTest {

  @Mock Coordinate anywhere;
  @Mock Coordinate destination;

  @Test
  void constructor() {
    assertThat(new Rook(anywhere, Alliance.BLACK)).matches(Rook::firstMove);
  }

  @Test
  void toSingleChar() {
    assertThat(new Rook(anywhere, Alliance.WHITE).singleChar()).isEqualTo("R");
    assertThat(new Rook(anywhere, Alliance.BLACK).singleChar()).isEqualTo("r");
  }

  @Test
  void horizontalMove() {
    var rook = new Rook(anywhere, Alliance.BLACK);
    assertThat(rook.moveVectors()).containsOnlyOnce(new int[] {1, 0});
  }

  @Test
  void verticalMove() {
    var rook = new Rook(anywhere, Alliance.BLACK);
    assertThat(rook.moveVectors()).containsOnlyOnce(new int[] {0, 1});
  }

  @Test
  void illegalMove() {
    var rook = new Rook(anywhere, Alliance.BLACK);
    assertThat(rook.moveVectors()).doesNotContain(new int[] {1, -1});
  }

  @Test
  void moveTo() {
    var rookToMove = new Rook(anywhere, Alliance.BLACK);

    assertThat(rookToMove.moveTo(destination))
        .isInstanceOf(Rook.class)
        .matches(rook -> rook.position().equals(destination))
        .matches(rook -> !rook.firstMove());
  }
}
