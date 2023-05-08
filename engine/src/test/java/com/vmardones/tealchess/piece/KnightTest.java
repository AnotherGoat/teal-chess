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
final class KnightTest {

    @Mock
    Coordinate coordinate;

    @Test
    void fen() {
        assertThat(new Knight(coordinate, Color.WHITE).fen()).isEqualTo("N");
        assertThat(new Knight(coordinate, Color.BLACK).fen()).isEqualTo("n");
    }

    @Test
    void numberOfMoves() {
        var knight = new Knight(coordinate, Color.WHITE);
        assertThat(knight.moveVectors()).hasSize(8);
    }

    @Test
    void lShapedMove() {
        var knight = new Knight(coordinate, Color.WHITE);
        assertThat(knight.moveVectors()).containsOnlyOnce(new Vector(-2, 1));
    }

    @Test
    void illegalMove() {
        var knight = new Knight(coordinate, Color.WHITE);
        assertThat(knight.moveVectors()).isNotEmpty().doesNotContain(new Vector(0, 1));
    }

    @Test
    void moveTo() {
        var knightToMove = new Knight(coordinate, Color.WHITE);
        var destination = mock(Coordinate.class);

        assertThat(knightToMove.moveTo(destination)).isInstanceOf(Knight.class).matches(knight -> knight.coordinate()
                .equals(destination));
    }
}
