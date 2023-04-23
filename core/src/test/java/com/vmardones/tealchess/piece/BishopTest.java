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
final class BishopTest {

    @Test
    void toSingleChar() {
        assertThat(new Bishop("a1", Color.WHITE).singleChar()).isEqualTo("B");
        assertThat(new Bishop("a1", Color.BLACK).singleChar()).isEqualTo("b");
    }

    @Test
    void numberOfMoves() {
        var bishop = new Bishop("a1", Color.BLACK);
        assertThat(bishop.moveVectors()).hasSize(4);
    }

    @Test
    void diagonalMove() {
        var bishop = new Bishop("a1", Color.BLACK);
        assertThat(bishop.moveVectors()).containsOnlyOnce(new int[] {1, 1});
    }

    @Test
    void illegalMove() {
        var bishop = new Bishop("a1", Color.BLACK);
        assertThat(bishop.moveVectors()).isNotEmpty().doesNotContain(new int[] {0, 1});
    }

    @Test
    void moveTo() {
        var bishopToMove = new Bishop("a1", Color.BLACK);

        assertThat(bishopToMove.moveTo("a2")).isInstanceOf(Bishop.class).matches(bishop -> bishop.coordinate()
                .equals(Coordinate.of("a2")));
    }
}
