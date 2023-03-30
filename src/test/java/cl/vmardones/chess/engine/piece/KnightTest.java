/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.vector.LShaped;
import cl.vmardones.chess.engine.piece.vector.Vertical;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KnightTest {

  @Mock Coordinate anywhere;
  @Mock Coordinate destination;
  @Mock Move move;

  @Test
  void constructor() {
    assertThat(new Knight(anywhere, Alliance.BLACK)).matches(Knight::isFirstMove);
  }

  @Test
  void toSingleChar() {
    assertThat(new Knight(anywhere, Alliance.WHITE).toSingleChar()).isEqualTo("N");
    assertThat(new Knight(anywhere, Alliance.BLACK).toSingleChar()).isEqualTo("n");
  }

  @Test
  void lShapedMove() {
    var knight = new Knight(anywhere, Alliance.WHITE);
    assertThat(knight.getMoveOffsets()).containsOnlyOnce(LShaped.UP_UP_LEFT.getVector());
  }

  @Test
  void illegalMove() {
    var knight = new Knight(anywhere, Alliance.WHITE);
    assertThat(knight.getMoveOffsets()).doesNotContain(Vertical.UP.getVector());
  }

  @Test
  void move() {
    var knightToMove = new Knight(anywhere, Alliance.WHITE);

    when(move.getDestination()).thenReturn(destination);

    assertThat(knightToMove.move(move))
        .isInstanceOf(Knight.class)
        .matches(knight -> knight.getPosition().equals(destination))
        .matches(knight -> !knight.isFirstMove());
  }
}
