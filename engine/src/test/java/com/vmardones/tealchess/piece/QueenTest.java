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
final class QueenTest {

    @Test
    void fen() {
        assertThat(new Queen("a1", Color.WHITE).fen()).isEqualTo("Q");
        assertThat(new Queen("a1", Color.BLACK).fen()).isEqualTo("q");
    }

    @Test
    void numberOfMoves() {
        var queen = new Queen("a1", Color.BLACK);
        assertThat(queen.moveVectors()).hasSize(8);
    }

    @Test
    void diagonalMove() {
        var queen = new Queen("a1", Color.BLACK);
        assertThat(queen.moveVectors()).containsOnlyOnce(new Vector(1, -1));
    }

    @Test
    void horizontalMove() {
        var queen = new Queen("a1", Color.BLACK);
        assertThat(queen.moveVectors()).containsOnlyOnce(new Vector(-1, 0));
    }

    @Test
    void verticalMove() {
        var queen = new Queen("a1", Color.BLACK);
        assertThat(queen.moveVectors()).containsOnlyOnce(new Vector(0, 1));
    }

    @Test
    void illegalMove() {
        var queen = new Queen("a1", Color.BLACK);
        assertThat(queen.moveVectors()).isNotEmpty().doesNotContain(new Vector(-1, 2));
    }

    @Test
    void moveTo() {
        var queenToMove = new Queen("a1", Color.BLACK);

        assertThat(queenToMove.moveTo("a2")).isInstanceOf(Queen.class).matches(queen -> queen.coordinate()
                .equals(Coordinate.of("a2")));
    }
}
