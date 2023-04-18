/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.player.Color;
import org.junit.jupiter.api.Test;

class QueenTest {

    @Test
    void toSingleChar() {
        assertThat(new Queen("a1", Color.WHITE).singleChar()).isEqualTo("Q");
        assertThat(new Queen("a1", Color.BLACK).singleChar()).isEqualTo("q");
    }

    @Test
    void numberOfMoves() {
        var queen = new Queen("a1", Color.BLACK);
        assertThat(queen.moveVectors()).hasSize(8);
    }

    @Test
    void diagonalMove() {
        var queen = new Queen("a1", Color.BLACK);
        assertThat(queen.moveVectors()).containsOnlyOnce(new int[] {1, -1});
    }

    @Test
    void horizontalMove() {
        var queen = new Queen("a1", Color.BLACK);
        assertThat(queen.moveVectors()).containsOnlyOnce(new int[] {-1, 0});
    }

    @Test
    void verticalMove() {
        var queen = new Queen("a1", Color.BLACK);
        assertThat(queen.moveVectors()).containsOnlyOnce(new int[] {0, 1});
    }

    @Test
    void illegalMove() {
        var queen = new Queen("a1", Color.BLACK);
        assertThat(queen.moveVectors()).isNotEmpty().doesNotContain(new int[] {-1, 2});
    }

    @Test
    void moveTo() {
        var queenToMove = new Queen("a1", Color.BLACK);

        assertThat(queenToMove.moveTo("a2")).isInstanceOf(Queen.class).matches(queen -> queen.coordinate()
                .equals(Coordinate.of("a2")));
    }
}
