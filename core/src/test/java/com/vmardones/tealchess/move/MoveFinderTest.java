/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.board.Coordinate;
import org.junit.jupiter.api.Test;

final class MoveFinderTest {

    @Test
    void sameCoordinate() {
        var moves = List.of(mock(LegalMove.class), mock(LegalMove.class), mock(LegalMove.class));
        var coordinate = mock(Coordinate.class);

        assertThat(MoveFinder.choose(moves, coordinate, coordinate)).isEmpty();
    }

    @Test
    void findMove() {
        var source = mock(Coordinate.class);
        var destination = mock(Coordinate.class);
        var otherDestination = mock(Coordinate.class);

        var move1 = mock(LegalMove.class);
        when(move1.source()).thenReturn(source);
        when(move1.destination()).thenReturn(otherDestination);

        var move2 = mock(LegalMove.class);
        when(move2.source()).thenReturn(source);
        when(move2.destination()).thenReturn(destination);

        var moves = List.of(move1, move2);

        assertThat(MoveFinder.choose(moves, source, destination)).isEqualTo(List.of(move2));
    }

    @Test
    void findNoMoves() {
        var source = mock(Coordinate.class);
        var destination = mock(Coordinate.class);

        var move = mock(LegalMove.class);
        when(move.source()).thenReturn(mock(Coordinate.class));
        when(move.destination()).thenReturn(mock(Coordinate.class));

        var moves = List.of(move);

        assertThat(MoveFinder.choose(moves, source, destination)).isEmpty();
    }

    @Test
    void passEmptyMoves() {
        var source = mock(Coordinate.class);
        var destination = mock(Coordinate.class);
        var moves = new ArrayList<LegalMove>();

        assertThat(MoveFinder.choose(moves, source, destination)).isEmpty();
    }

    @Test
    void findMultipleMoves() {
        var source = mock(Coordinate.class);
        var destination = mock(Coordinate.class);

        var move1 = mock(LegalMove.class);
        when(move1.source()).thenReturn(source);
        when(move1.destination()).thenReturn(destination);

        var move2 = mock(LegalMove.class);
        when(move2.source()).thenReturn(source);
        when(move2.destination()).thenReturn(destination);

        var moves = List.of(move1, move2);

        assertThat(MoveFinder.choose(moves, source, destination))
                .containsOnlyOnce(move1)
                .containsOnlyOnce(move2);
    }
}
