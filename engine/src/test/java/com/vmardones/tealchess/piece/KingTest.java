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
final class KingTest {

    @Mock
    Coordinate coordinate;

    @Test
    void fen() {
        assertThat(new King(coordinate, Color.WHITE).fen()).isEqualTo("K");
        assertThat(new King(coordinate, Color.BLACK).fen()).isEqualTo("k");
    }

    @Test
    void numberOfMoves() {
        var king = new King(coordinate, Color.WHITE);
        assertThat(king.moveVectors()).hasSize(8);
    }

    @Test
    void diagonalMove() {
        var king = new King(coordinate, Color.WHITE);
        assertThat(king.moveVectors()).containsOnlyOnce(new Vector(1, 1));
    }

    @Test
    void horizontalMove() {
        var king = new King(coordinate, Color.WHITE);
        assertThat(king.moveVectors()).containsOnlyOnce(new Vector(-1, 0));
    }

    @Test
    void verticalMove() {
        var king = new King(coordinate, Color.WHITE);
        assertThat(king.moveVectors()).containsOnlyOnce(new Vector(0, 1));
    }

    @Test
    void illegalMove() {
        var king = new King(coordinate, Color.WHITE);
        assertThat(king.moveVectors()).isNotEmpty().doesNotContain(new Vector(-1, 2));
    }

    @Test
    void moveTo() {
        var kingToMove = new King(coordinate, Color.WHITE);
        var destination = mock(Coordinate.class);

        assertThat(kingToMove.moveTo(destination)).isInstanceOf(King.class).matches(king -> king.coordinate()
                .equals(destination));
    }
}
