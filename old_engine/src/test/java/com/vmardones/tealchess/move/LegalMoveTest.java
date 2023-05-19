/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class LegalMoveTest {

    @Test
    void checkmateHash() {
        var move = mock(Move.class);
        when(move.san()).thenReturn("Qe7");

        var legalMove = new LegalMove(move, MoveResult.CHECKMATE);
        assertThat(legalMove.san()).isEqualTo("Qe7#");
    }

    @Test
    void sanIsToString() {
        var move = mock(Move.class);
        when(move.san()).thenReturn("c6");

        var legalMove = new LegalMove(move, MoveResult.CONTINUE);
        assertThat(legalMove.san()).isEqualTo(legalMove.toString());
    }

    @Test
    void fileDisambiguation() {
        var move = mock(Move.class);
        when(move.source()).thenReturn(Coordinate.of("b3"));
        when(move.san()).thenReturn("Nc5");

        var legalMove = new LegalMove(move, MoveResult.CONTINUE, Disambiguation.FILE);
        assertThat(legalMove.san()).isEqualTo("Nbc5");
    }

    @Test
    void rankDisambiguation() {
        var move = mock(Move.class);
        when(move.source()).thenReturn(Coordinate.of("b3"));
        when(move.san()).thenReturn("Nc5");

        var legalMove = new LegalMove(move, MoveResult.CONTINUE, Disambiguation.RANK);
        assertThat(legalMove.san()).isEqualTo("N3c5");
    }

    @Test
    void fullDisambiguation() {
        var move = mock(Move.class);
        when(move.source()).thenReturn(Coordinate.of("b3"));
        when(move.san()).thenReturn("Nc5");

        var legalMove = new LegalMove(move, MoveResult.CONTINUE, Disambiguation.FULL);
        assertThat(legalMove.san()).isEqualTo("Nb3c5");
    }

    @Test
    void notCastling() {
        var move = mock(Move.class);
        when(move.type()).thenReturn(MoveType.NORMAL);

        var legalMove = new LegalMove(move, MoveResult.CONTINUE);
        assertThat(legalMove.isCastling()).isFalse();
    }

    @Test
    void kingCastling() {
        var move = mock(Move.class);
        when(move.type()).thenReturn(MoveType.KING_CASTLE);

        var legalMove = new LegalMove(move, MoveResult.CONTINUE);
        assertThat(legalMove.isCastling()).isTrue();
    }

    @Test
    void queenCastling() {
        var move = mock(Move.class);
        when(move.type()).thenReturn(MoveType.QUEEN_CASTLE);

        var legalMove = new LegalMove(move, MoveResult.CONTINUE);
        assertThat(legalMove.isCastling()).isTrue();
    }
}
