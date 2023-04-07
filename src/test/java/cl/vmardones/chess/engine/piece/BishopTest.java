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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BishopTest {

    @Test
    void constructor() {
        assertThat(new Bishop("a1", Alliance.BLACK)).matches(Bishop::firstMove);
    }

    @Test
    void toSingleChar() {
        assertThat(new Bishop("a1", Alliance.WHITE).singleChar()).isEqualTo("B");
        assertThat(new Bishop("a1", Alliance.BLACK).singleChar()).isEqualTo("b");
    }

    @Test
    void diagonalMove() {
        var bishop = new Bishop("a1", Alliance.BLACK);
        assertThat(bishop.moveVectors()).containsOnlyOnce(new int[] {1, 1});
    }

    @Test
    void illegalMove() {
        var bishop = new Bishop("a1", Alliance.BLACK);
        assertThat(bishop.moveVectors()).doesNotContain(new int[] {0, 1});
    }

    @Test
    void moveTo() {
        var bishopToMove = new Bishop("a1", Alliance.BLACK);

        assertThat(bishopToMove.moveTo("a2"))
                .isInstanceOf(Bishop.class)
                .matches(bishop -> bishop.position().equals(Coordinate.of("a2")))
                .matches(bishop -> !bishop.firstMove());
    }
}
