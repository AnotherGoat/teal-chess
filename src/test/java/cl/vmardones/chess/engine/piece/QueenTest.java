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
class QueenTest {

  @Mock Coordinate anywhere;
  @Mock Coordinate destination;
  @Mock Move move;

  @Test
  void constructor() {
    assertThat(new Queen(anywhere, Alliance.BLACK)).matches(Queen::isFirstMove);
  }

  @Test
  void toSingleChar() {
    assertThat(new Queen(anywhere, Alliance.WHITE).toSingleChar()).isEqualTo("Q");
    assertThat(new Queen(anywhere, Alliance.BLACK).toSingleChar()).isEqualTo("q");
  }

  @Test
  void diagonalMove() {
    var queen = new Queen(anywhere, Alliance.BLACK);
    assertThat(queen.getMoveVectors()).containsOnlyOnce(Diagonal.DOWN_RIGHT.getVector());
  }

  @Test
  void horizontalMove() {
    var queen = new Queen(anywhere, Alliance.BLACK);
    assertThat(queen.getMoveVectors()).containsOnlyOnce(Horizontal.LEFT.getVector());
  }

  @Test
  void verticalMove() {
    var queen = new Queen(anywhere, Alliance.BLACK);
    assertThat(queen.getMoveVectors()).containsOnlyOnce(Vertical.UP.getVector());
  }

  @Test
  void illegalMove() {
    var queen = new Queen(anywhere, Alliance.BLACK);
    assertThat(queen.getMoveVectors()).doesNotContain(LShaped.UP_UP_LEFT.getVector());
  }

  @Test
  void move() {
    var queenToMove = new Queen(anywhere, Alliance.BLACK);

    when(move.destination()).thenReturn(destination);

    assertThat(queenToMove.move(move))
        .isInstanceOf(Queen.class)
        .matches(queen -> queen.getPosition().equals(destination))
        .matches(queen -> !queen.isFirstMove());
  }
}
