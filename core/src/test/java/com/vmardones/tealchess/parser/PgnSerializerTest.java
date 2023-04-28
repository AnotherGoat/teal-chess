/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.move.LegalMove;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class PgnSerializerTest {

    @Test
    void serializeEmptyList() {
        assertThat(PgnSerializer.serializeMoves(emptyList())).isEmpty();
    }

    @Test
    void serializeMoves() {
        // Ruy López Opening
        var move1 = mock(LegalMove.class);
        when(move1.san()).thenReturn("e4");

        var move2 = mock(LegalMove.class);
        when(move2.san()).thenReturn("e5");

        var move3 = mock(LegalMove.class);
        when(move3.san()).thenReturn("Nf3");

        var move4 = mock(LegalMove.class);
        when(move4.san()).thenReturn("Nc6");

        var move5 = mock(LegalMove.class);
        when(move5.san()).thenReturn("Bb5");

        var moves = List.of(move1, move2, move3, move4, move5);

        assertThat(PgnSerializer.serializeMoves(moves)).isEqualTo("1. e4 e5 2. Nf3 Nc6 3. Bb5");
    }
}
