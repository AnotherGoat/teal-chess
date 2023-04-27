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
import com.vmardones.tealchess.piece.*;
import com.vmardones.tealchess.player.Color;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class MoveTest {

    @Test
    void source() {
        var source = mock(Coordinate.class);
        var piece = mock(Knight.class);
        when(piece.coordinate()).thenReturn(source);

        var destination = mock(Coordinate.class);
        var move = Move.builder(piece, destination).normal();

        assertThat(move.source()).isEqualTo(source);
    }

    @Test
    void normalToString() {
        var piece = new Rook("a1", Color.WHITE);
        var move = Move.builder(piece, Coordinate.of("e1")).normal();

        assertThat(move.san()).isEqualTo("Re1");
    }

    @Test
    void captureToString() {
        var piece = new Bishop("b1", Color.WHITE);
        var capturedPiece = new Bishop("c2", Color.BLACK);
        var move = Move.builder(piece, Coordinate.of("c2")).capture(capturedPiece);

        assertThat(move.san()).isEqualTo("Bxc2");
    }

    @Test
    void pawnCaptureSan() {
        var pawn = new Pawn("a8", Color.BLACK);
        var capturedPawn = new Pawn("b7", Color.WHITE);
        var move = Move.builder(pawn, Coordinate.of("b7")).capture(capturedPawn);

        assertThat(move.san()).isEqualTo("axb7");
    }

    @Test
    void kingCastleSan() {
        var king = new King("e5", Color.WHITE);
        var rook = new Rook("e8", Color.WHITE);
        var move = Move.builder(king, Coordinate.of("e7")).castle(true, rook, Coordinate.of("e6"));

        assertThat(move.san()).isEqualTo("O-O");
    }

    @Test
    void queenCastleSan() {
        var king = new King("e5", Color.WHITE);
        var rook = new Rook("e1", Color.WHITE);
        var move = Move.builder(king, Coordinate.of("e3")).castle(false, rook, Coordinate.of("e4"));

        assertThat(move.san()).isEqualTo("O-O-O");
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Move.class)
                .withNonnullFields("type", "piece", "destination")
                .verify();
    }
}
