/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RookTest {

    @Test
    void constructor() {
        assertThat(new Rook("a1", Alliance.BLACK)).matches(Rook::firstMove);
    }

    @Test
    void toSingleChar() {
        assertThat(new Rook("a1", Alliance.WHITE).singleChar()).isEqualTo("R");
        assertThat(new Rook("a1", Alliance.BLACK).singleChar()).isEqualTo("r");
    }

    @Test
    void horizontalMove() {
        var rook = new Rook("a1", Alliance.BLACK);
        assertThat(rook.moveVectors()).containsOnlyOnce(new int[] {1, 0});
    }

    @Test
    void verticalMove() {
        var rook = new Rook("a1", Alliance.BLACK);
        assertThat(rook.moveVectors()).containsOnlyOnce(new int[] {0, 1});
    }

    @Test
    void illegalMove() {
        var rook = new Rook("a1", Alliance.BLACK);
        assertThat(rook.moveVectors()).doesNotContain(new int[] {1, -1});
    }

    @Test
    void moveTo() {
        var rookToMove = new Rook("a1", Alliance.BLACK);

        assertThat(rookToMove.moveTo("a2"))
                .isInstanceOf(Rook.class)
                .matches(rook -> rook.position().equals(Position.of("a2")))
                .matches(rook -> !rook.firstMove());
    }
}
