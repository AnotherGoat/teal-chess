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
        var move = Move.createNormal(piece, destination);

        assertThat(move.source()).isEqualTo(source);
    }

    @Test
    void isCapture() {
        var piece = mock(Knight.class);
        var destination = mock(Coordinate.class);
        var capturedPiece = mock(Knight.class);

        var move = Move.createCapture(piece, destination, capturedPiece);

        assertThat(move.isCapture()).isTrue();
    }

    @Test
    void normalIsNotCapture() {
        var piece = mock(Knight.class);
        var destination = mock(Coordinate.class);

        var move = Move.createNormal(piece, destination);

        assertThat(move.isCapture()).isFalse();
    }

    @Test
    void castleIsNotCapture() {
        var king = mock(King.class);
        var rook = mock(Rook.class);

        var move = Move.createCastle(true, king, mock(Coordinate.class), rook, mock(Coordinate.class));

        assertThat(move.isCapture()).isFalse();
    }

    @Test
    void isNone() {
        var source = mock(Coordinate.class);
        var piece = mock(Knight.class);
        when(piece.coordinate()).thenReturn(source);

        var capturedPiece = mock(Knight.class);

        var move = Move.createCapture(piece, source, capturedPiece);

        assertThat(move.isNone()).isTrue();
    }

    @Test
    void isNotNone() {
        var source = mock(Coordinate.class);
        var piece = mock(Knight.class);
        when(piece.coordinate()).thenReturn(source);

        var capturedPiece = mock(Knight.class);

        var destination = mock(Coordinate.class);
        var move = Move.createCapture(piece, destination, capturedPiece);

        assertThat(move.isNone()).isFalse();
    }

    @Test
    void normalToString() {
        var piece = new Rook("a1", Color.WHITE);
        var move = Move.createNormal(piece, Coordinate.of("e1"));

        assertThat(move).hasToString("e1");
    }

    @Test
    void captureToString() {
        var piece = new Bishop("b1", Color.WHITE);
        var capturedPiece = new Bishop("c2", Color.BLACK);
        var move = Move.createCapture(piece, Coordinate.of("c2"), capturedPiece);

        assertThat(move).hasToString("Bc2");
    }

    @Test
    void pawnCaptureToString() {
        var pawn = new Pawn("a8", Color.BLACK);
        var capturedPawn = new Pawn("b7", Color.WHITE);
        var move = Move.createCapture(pawn, Coordinate.of("b7"), capturedPawn);

        assertThat(move).hasToString("axb7");
    }

    @Test
    void kingCastleToString() {
        var king = new King("e5", Color.WHITE);
        var rook = new Rook("e8", Color.WHITE);
        var move = Move.createCastle(true, king, Coordinate.of("e7"), rook, Coordinate.of("e6"));

        assertThat(move).hasToString("0-0");
    }

    @Test
    void queenCastleToString() {
        var king = new King("e5", Color.WHITE);
        var rook = new Rook("e1", Color.WHITE);
        var move = Move.createCastle(false, king, Coordinate.of("e3"), rook, Coordinate.of("e4"));

        assertThat(move).hasToString("0-0-0");
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Move.class)
                .withNonnullFields("type", "piece", "destination", "result")
                .verify();
    }
}
