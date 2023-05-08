/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.piece.Knight;
import com.vmardones.tealchess.piece.Pawn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExcludeFromNullAway
@ExtendWith(MockitoExtension.class)
final class AttackTest {

    @Mock
    Coordinate source;

    @Mock
    Coordinate destination;

    @Test
    void notAnAttack() {
        var pawn = mock(Pawn.class);
        when(pawn.coordinate()).thenReturn(source);

        var square = mock(Square.class);
        when(square.coordinate()).thenReturn(source);

        assertThatThrownBy(() -> new Attack(pawn, square))
                .isInstanceOf(IllegalMoveException.class)
                .hasMessageContaining("cannot be the same");
    }

    @Test
    void asString() {
        var knight = mock(Knight.class);
        when(knight.coordinate()).thenReturn(source);

        var attackedSquare = mock(Square.class);
        when(attackedSquare.coordinate()).thenReturn(destination);

        when(source.toString()).thenReturn("g2");
        when(destination.toString()).thenReturn("e3");

        assertThat(new Attack(knight, attackedSquare)).hasToString("g2e3");
    }
}
