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
import cl.vmardones.chess.engine.piece.vector.Jump;
import cl.vmardones.chess.engine.piece.vector.Vertical;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PawnTest {

  @Mock Coordinate anywhere;
  @Mock Coordinate destination;
  @Mock Move move;

  @Test
  void constructor() {
    assertThat(new Pawn(anywhere, Alliance.BLACK)).matches(Pawn::isFirstMove);
  }

  @Test
  void toSingleChar() {
    assertThat(new Pawn(anywhere, Alliance.WHITE).toSingleChar()).isEqualTo("P");
    assertThat(new Pawn(anywhere, Alliance.BLACK).toSingleChar()).isEqualTo("p");
  }

  @Test
  void whiteMoves() {
    var whitePawn = new Pawn(anywhere, Alliance.WHITE);
    assertThat(whitePawn.getMoveOffsets())
        .containsOnlyOnce(
            Vertical.UP.getVector(), Jump.UP.getVector(), Diagonal.UP_RIGHT.getVector());
  }

  @Test
  void blackMoves() {
    var blackPawn = new Pawn(anywhere, Alliance.BLACK);
    assertThat(blackPawn.getMoveOffsets())
        .containsOnlyOnce(
            Vertical.DOWN.getVector(), Jump.DOWN.getVector(), Diagonal.DOWN_RIGHT.getVector());
  }

  @Test
  void illegalMove() {
    var pawn = new Pawn(anywhere, Alliance.WHITE);
    assertThat(pawn.getMoveOffsets()).doesNotContain(Horizontal.RIGHT.getVector());
  }

  @Test
  void move() {
    var pawnToMove = new Pawn(anywhere, Alliance.WHITE);

    when(move.getDestination()).thenReturn(destination);

    assertThat(pawnToMove.move(move))
        .isInstanceOf(Pawn.class)
        .matches(pawn -> pawn.getPosition().equals(destination))
        .matches(pawn -> !pawn.isFirstMove());
  }
}
