/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KingTest {

    @Mock
    Coordinate anywhere;

    @Mock
    Coordinate destination;

    @Mock
    Move move;

    @Test
    void constructor() {
        assertThat(new King(anywhere, Alliance.BLACK)).matches(King::firstMove);
    }

    @Test
    void toSingleChar() {
        assertThat(new King(anywhere, Alliance.WHITE).singleChar()).isEqualTo("K");
        assertThat(new King(anywhere, Alliance.BLACK).singleChar()).isEqualTo("k");
    }

    @Test
    void diagonalMove() {
        var king = new King(anywhere, Alliance.WHITE);
        assertThat(king.moveOffsets()).containsOnlyOnce(new int[] {1, 1});
    }

    @Test
    void horizontalMove() {
        var king = new King(anywhere, Alliance.WHITE);
        assertThat(king.moveOffsets()).containsOnlyOnce(new int[] {-1, 0});
    }

    @Test
    void verticalMove() {
        var king = new King(anywhere, Alliance.WHITE);
        assertThat(king.moveOffsets()).containsOnlyOnce(new int[] {0, 1});
    }

    @Test
    void illegalMove() {
        var king = new King(anywhere, Alliance.WHITE);
        assertThat(king.moveOffsets()).doesNotContain(new int[] {-1, 2});
    }

    @Test
    void moveTo() {
        var kingToMove = new King(anywhere, Alliance.WHITE);

        assertThat(kingToMove.moveTo(destination))
                .isInstanceOf(King.class)
                .matches(king -> king.position().equals(destination))
                .matches(king -> !king.firstMove());
    }
}
