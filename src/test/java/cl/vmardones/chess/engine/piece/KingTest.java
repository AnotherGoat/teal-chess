/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.player.Color;
import org.junit.jupiter.api.Test;

class KingTest {

    @Test
    void toSingleChar() {
        assertThat(new King("a1", Color.WHITE).singleChar()).isEqualTo("K");
        assertThat(new King("a1", Color.BLACK).singleChar()).isEqualTo("k");
    }

    @Test
    void numberOfMoves() {
        var king = new King("a1", Color.WHITE);
        assertThat(king.moveOffsets()).hasSize(8);
    }

    @Test
    void diagonalMove() {
        var king = new King("a1", Color.WHITE);
        assertThat(king.moveOffsets()).containsOnlyOnce(new int[] {1, 1});
    }

    @Test
    void horizontalMove() {
        var king = new King("a1", Color.WHITE);
        assertThat(king.moveOffsets()).containsOnlyOnce(new int[] {-1, 0});
    }

    @Test
    void verticalMove() {
        var king = new King("a1", Color.WHITE);
        assertThat(king.moveOffsets()).containsOnlyOnce(new int[] {0, 1});
    }

    @Test
    void illegalMove() {
        var king = new King("a1", Color.WHITE);
        assertThat(king.moveOffsets()).isNotEmpty().doesNotContain(new int[] {-1, 2});
    }

    @Test
    void moveTo() {
        var kingToMove = new King("a1", Color.WHITE);

        assertThat(kingToMove.moveTo("a2")).isInstanceOf(King.class).matches(king -> king.coordinate()
                .equals(Coordinate.of("a2")));
    }
}
