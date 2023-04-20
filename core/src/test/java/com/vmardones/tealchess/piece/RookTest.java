/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.Test;

class RookTest {

    @Test
    void toSingleChar() {
        assertThat(new Rook("a1", Color.WHITE).singleChar()).isEqualTo("R");
        assertThat(new Rook("a1", Color.BLACK).singleChar()).isEqualTo("r");
    }

    @Test
    void numberOfMoves() {
        var rook = new Rook("a1", Color.BLACK);
        assertThat(rook.moveVectors()).hasSize(4);
    }

    @Test
    void horizontalMove() {
        var rook = new Rook("a1", Color.BLACK);
        assertThat(rook.moveVectors()).containsOnlyOnce(new int[] {1, 0});
    }

    @Test
    void verticalMove() {
        var rook = new Rook("a1", Color.BLACK);
        assertThat(rook.moveVectors()).containsOnlyOnce(new int[] {0, 1});
    }

    @Test
    void illegalMove() {
        var rook = new Rook("a1", Color.BLACK);
        assertThat(rook.moveVectors()).isNotEmpty().doesNotContain(new int[] {1, -1});
    }

    @Test
    void moveTo() {
        var rookToMove = new Rook("a1", Color.BLACK);

        assertThat(rookToMove.moveTo("a2")).isInstanceOf(Rook.class).matches(rook -> rook.coordinate()
                .equals(Coordinate.of("a2")));
    }
}
