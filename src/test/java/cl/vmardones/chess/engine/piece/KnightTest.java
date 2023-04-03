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
class KnightTest {

  @Mock Coordinate anywhere;
  @Mock Coordinate destination;

  @Test
  void constructor() {
    assertThat(new Knight(anywhere, Alliance.BLACK)).matches(Knight::firstMove);
  }

  @Test
  void toSingleChar() {
    assertThat(new Knight(anywhere, Alliance.WHITE).singleChar()).isEqualTo("N");
    assertThat(new Knight(anywhere, Alliance.BLACK).singleChar()).isEqualTo("n");
  }

  @Test
  void lShapedMove() {
    var knight = new Knight(anywhere, Alliance.WHITE);
    assertThat(knight.moveOffsets()).containsOnlyOnce(new int[] {-2, 1});
  }

  @Test
  void illegalMove() {
    var knight = new Knight(anywhere, Alliance.WHITE);
    assertThat(knight.moveOffsets()).doesNotContain(new int[] {0, 1});
  }

  @Test
  void moveTo() {
    var knightToMove = new Knight(anywhere, Alliance.WHITE);

    assertThat(knightToMove.moveTo(destination))
        .isInstanceOf(Knight.class)
        .matches(knight -> knight.position().equals(destination))
        .matches(knight -> !knight.firstMove());
  }
}
