/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class KnightTest {

    @Test
    void fen() {
        assertThat(new Knight("a1", Color.WHITE).fen()).isEqualTo("N");
        assertThat(new Knight("a1", Color.BLACK).fen()).isEqualTo("n");
    }

    @Test
    void numberOfMoves() {
        var knight = new Knight("a1", Color.WHITE);
        assertThat(knight.moveVectors()).hasSize(8);
    }

    @Test
    void lShapedMove() {
        var knight = new Knight("a1", Color.WHITE);
        assertThat(knight.moveVectors()).containsOnlyOnce(new Vector(-2, 1));
    }

    @Test
    void illegalMove() {
        var knight = new Knight("a1", Color.WHITE);
        assertThat(knight.moveVectors()).isNotEmpty().doesNotContain(new Vector(0, 1));
    }

    @Test
    void moveTo() {
        var knightToMove = new Knight("a1", Color.WHITE);

        assertThat(knightToMove.moveTo("a2")).isInstanceOf(Knight.class).matches(knight -> knight.coordinate()
                .equals(Coordinate.of("a2")));
    }
}
