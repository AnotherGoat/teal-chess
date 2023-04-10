/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cl.vmardones.chess.engine.board.Position;
import org.junit.jupiter.api.Test;

class MoveFinderTest {

    @Test
    void samePosition() {
        var moves = List.of(mock(Move.class), mock(Move.class), mock(Move.class));
        var position = mock(Position.class);

        assertThat(MoveFinder.choose(moves, position, position)).isNull();
    }

    @Test
    void findMove() {
        var source = mock(Position.class);
        var destination = mock(Position.class);
        var otherDestination = mock(Position.class);

        var move1 = mock(Move.class);
        when(move1.source()).thenReturn(source);
        when(move1.destination()).thenReturn(otherDestination);

        var move2 = mock(Move.class);
        when(move2.source()).thenReturn(source);
        when(move2.destination()).thenReturn(destination);

        var moves = List.of(move1, move2);

        assertThat(MoveFinder.choose(moves, source, destination)).isEqualTo(move2);
    }

    @Test
    void findNoMoves() {
        var source = mock(Position.class);
        var destination = mock(Position.class);

        var move = mock(Move.class);
        when(move.source()).thenReturn(mock(Position.class));
        when(move.destination()).thenReturn(mock(Position.class));

        var moves = List.of(move);

        assertThat(MoveFinder.choose(moves, source, destination)).isNull();
    }

    @Test
    void passEmptyMoves() {
        var source = mock(Position.class);
        var destination = mock(Position.class);
        var moves = new ArrayList<Move>();

        assertThat(MoveFinder.choose(moves, source, destination)).isNull();
    }

    @Test
    void findFirstMove() {
        var source = mock(Position.class);
        var destination = mock(Position.class);

        var move1 = mock(Move.class);
        when(move1.source()).thenReturn(source);
        when(move1.destination()).thenReturn(destination);

        var move2 = mock(Move.class);
        when(move2.source()).thenReturn(source);
        when(move2.destination()).thenReturn(destination);

        var moves = List.of(move1, move2);

        assertThat(MoveFinder.choose(moves, source, destination))
                .isNotEqualTo(move2)
                .isEqualTo(move1);
    }
}
