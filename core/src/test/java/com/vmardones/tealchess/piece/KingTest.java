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
final class KingTest {

    @Test
    void toSingleChar() {
        assertThat(new King("a1", Color.WHITE).singleChar()).isEqualTo("K");
        assertThat(new King("a1", Color.BLACK).singleChar()).isEqualTo("k");
    }

    @Test
    void numberOfMoves() {
        var king = new King("a1", Color.WHITE);
        assertThat(king.moveVectors()).hasSize(8);
    }

    @Test
    void diagonalMove() {
        var king = new King("a1", Color.WHITE);
        assertThat(king.moveVectors()).containsOnlyOnce(new Vector(1, 1));
    }

    @Test
    void horizontalMove() {
        var king = new King("a1", Color.WHITE);
        assertThat(king.moveVectors()).containsOnlyOnce(new Vector(-1, 0));
    }

    @Test
    void verticalMove() {
        var king = new King("a1", Color.WHITE);
        assertThat(king.moveVectors()).containsOnlyOnce(new Vector(0, 1));
    }

    @Test
    void illegalMove() {
        var king = new King("a1", Color.WHITE);
        assertThat(king.moveVectors()).isNotEmpty().doesNotContain(new Vector(-1, 2));
    }

    @Test
    void moveTo() {
        var kingToMove = new King("a1", Color.WHITE);

        assertThat(kingToMove.moveTo("a2")).isInstanceOf(King.class).matches(king -> king.coordinate()
                .equals(Coordinate.of("a2")));
    }
}
