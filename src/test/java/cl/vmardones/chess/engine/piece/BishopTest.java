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
class BishopTest {

    @Mock
    Coordinate anywhere;

    @Mock
    Coordinate destination;

    @Test
    void constructor() {
        assertThat(new Bishop(anywhere, Alliance.BLACK)).matches(Bishop::firstMove);
    }

    @Test
    void toSingleChar() {
        assertThat(new Bishop(anywhere, Alliance.WHITE).singleChar()).isEqualTo("B");
        assertThat(new Bishop(anywhere, Alliance.BLACK).singleChar()).isEqualTo("b");
    }

    @Test
    void diagonalMove() {
        var bishop = new Bishop(anywhere, Alliance.BLACK);
        assertThat(bishop.moveVectors()).containsOnlyOnce(new int[] {1, 1});
    }

    @Test
    void illegalMove() {
        var bishop = new Bishop(anywhere, Alliance.BLACK);
        assertThat(bishop.moveVectors()).doesNotContain(new int[] {0, 1});
    }

    @Test
    void moveTo() {
        var bishopToMove = new Bishop(anywhere, Alliance.BLACK);

        assertThat(bishopToMove.moveTo(destination))
                .isInstanceOf(Bishop.class)
                .matches(bishop -> bishop.position().equals(destination))
                .matches(bishop -> !bishop.firstMove());
    }
}
