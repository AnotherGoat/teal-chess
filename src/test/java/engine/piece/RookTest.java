package engine.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import engine.board.Coordinate;
import engine.move.Move;
import engine.player.Alliance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RookTest {

  Rook rook;
  @Mock Move move;

  @BeforeEach
  void setUp() {
    rook = new Rook(Coordinate.of("a1"), Alliance.BLACK);
  }

  @Test
  void horizontalMove() {
    assertThat(rook.isInMoveRange(7)).isTrue();
  }

  @Test
  void verticalMove() {
    assertThat(rook.isInMoveRange(56)).isTrue();
  }

  @Test
  void notInMoveRange() {
    assertThat(rook.isInMoveRange(9)).isFalse();
  }

  @Test
  void movePiece() {
    when(move.getDestination()).thenReturn(5);

    assertThat(rook.move(move))
        .isInstanceOf(Rook.class)
        .matches(rook -> rook.getPosition() == 5)
        .matches(rook -> !rook.isFirstMove());
  }
}
