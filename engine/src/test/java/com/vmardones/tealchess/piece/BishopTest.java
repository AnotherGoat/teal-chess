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
final class BishopTest {

    @Mock
    Coordinate coordinate;

    @Test
    void fen() {
        assertThat(new Bishop(coordinate, Color.WHITE).fen()).isEqualTo("B");
        assertThat(new Bishop(coordinate, Color.BLACK).fen()).isEqualTo("b");
    }

    @Test
    void numberOfMoves() {
        var bishop = new Bishop(coordinate, Color.BLACK);
        assertThat(bishop.moveVectors()).hasSize(4);
    }

    @Test
    void diagonalMove() {
        var bishop = new Bishop(coordinate, Color.BLACK);
        assertThat(bishop.moveVectors()).containsOnlyOnce(new Vector(1, 1));
    }

    @Test
    void illegalMove() {
        var bishop = new Bishop(coordinate, Color.BLACK);
        assertThat(bishop.moveVectors()).isNotEmpty().doesNotContain(new Vector(0, 1));
    }

    @Test
    void moveTo() {
        var bishopToMove = new Bishop(coordinate, Color.BLACK);
        var destination = mock(Coordinate.class);

        assertThat(bishopToMove.moveTo(destination)).isInstanceOf(Bishop.class).matches(bishop -> bishop.coordinate()
                .equals(destination));
    }
}
