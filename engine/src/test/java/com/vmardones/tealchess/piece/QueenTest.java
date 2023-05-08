/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExcludeFromNullAway
@ExtendWith(MockitoExtension.class)
final class QueenTest {

    @Mock
    Coordinate coordinate;

    @Test
    void fen() {
        assertThat(new Queen(coordinate, Color.WHITE).fen()).isEqualTo("Q");
        assertThat(new Queen(coordinate, Color.BLACK).fen()).isEqualTo("q");
    }

    @Test
    void numberOfMoves() {
        var queen = new Queen(coordinate, Color.BLACK);
        assertThat(queen.moveVectors()).hasSize(8);
    }

    @Test
    void diagonalMove() {
        var queen = new Queen(coordinate, Color.BLACK);
        assertThat(queen.moveVectors()).containsOnlyOnce(new Vector(1, -1));
    }

    @Test
    void horizontalMove() {
        var queen = new Queen(coordinate, Color.BLACK);
        assertThat(queen.moveVectors()).containsOnlyOnce(new Vector(-1, 0));
    }

    @Test
    void verticalMove() {
        var queen = new Queen(coordinate, Color.BLACK);
        assertThat(queen.moveVectors()).containsOnlyOnce(new Vector(0, 1));
    }

    @Test
    void illegalMove() {
        var queen = new Queen(coordinate, Color.BLACK);
        assertThat(queen.moveVectors()).isNotEmpty().doesNotContain(new Vector(-1, 2));
    }

    @Test
    void moveTo() {
        var queenToMove = new Queen(coordinate, Color.BLACK);
        var destination = mock(Coordinate.class);

        assertThat(queenToMove.moveTo(destination)).isInstanceOf(Queen.class).matches(queen -> queen.coordinate()
                .equals(destination));
    }
}
