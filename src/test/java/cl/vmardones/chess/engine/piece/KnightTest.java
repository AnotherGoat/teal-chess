/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.player.Color;
import org.junit.jupiter.api.Test;

class KnightTest {

    @Test
    void toSingleChar() {
        assertThat(new Knight("a1", Color.WHITE).singleChar()).isEqualTo("N");
        assertThat(new Knight("a1", Color.BLACK).singleChar()).isEqualTo("n");
    }

    @Test
    void numberOfMoves() {
        var knight = new Knight("a1", Color.WHITE);
        assertThat(knight.moveOffsets()).hasSize(8);
    }

    @Test
    void lShapedMove() {
        var knight = new Knight("a1", Color.WHITE);
        assertThat(knight.moveOffsets()).containsOnlyOnce(new int[] {-2, 1});
    }

    @Test
    void illegalMove() {
        var knight = new Knight("a1", Color.WHITE);
        assertThat(knight.moveOffsets()).isNotEmpty().doesNotContain(new int[] {0, 1});
    }

    @Test
    void moveTo() {
        var knightToMove = new Knight("a1", Color.WHITE);

        assertThat(knightToMove.moveTo("a2")).isInstanceOf(Knight.class).matches(knight -> knight.coordinate()
                .equals(Coordinate.of("a2")));
    }
}
