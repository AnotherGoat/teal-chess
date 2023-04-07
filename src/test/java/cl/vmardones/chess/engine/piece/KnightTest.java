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
class KnightTest {

    @Test
    void constructor() {
        assertThat(new Knight("a1", Alliance.BLACK)).matches(Knight::firstMove);
    }

    @Test
    void toSingleChar() {
        assertThat(new Knight("a1", Alliance.WHITE).singleChar()).isEqualTo("N");
        assertThat(new Knight("a1", Alliance.BLACK).singleChar()).isEqualTo("n");
    }

    @Test
    void lShapedMove() {
        var knight = new Knight("a1", Alliance.WHITE);
        assertThat(knight.moveOffsets()).containsOnlyOnce(new int[] {-2, 1});
    }

    @Test
    void illegalMove() {
        var knight = new Knight("a1", Alliance.WHITE);
        assertThat(knight.moveOffsets()).doesNotContain(new int[] {0, 1});
    }

    @Test
    void moveTo() {
        var knightToMove = new Knight("a1", Alliance.WHITE);

        assertThat(knightToMove.moveTo("a2"))
                .isInstanceOf(Knight.class)
                .matches(knight -> knight.position().equals(Position.of("a2")))
                .matches(knight -> !knight.firstMove());
    }
}
