/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.piece.*;
import com.vmardones.tealchess.player.Color;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class MoveTest {

    @Mock
    Coordinate source;

    @Mock
    Coordinate destination;

    @Test
    void illegalMove() {
        var rook = mock(Rook.class);
        when(rook.coordinate()).thenReturn(source);

        assertThatThrownBy(() -> Move.normal(rook, source))
                .isInstanceOf(IllegalMoveException.class)
                .hasMessageContaining("cannot be the same");
    }

    @Test
    void source() {
        var piece = mock(Knight.class);
        when(piece.coordinate()).thenReturn(source);

        var move = Move.normal(piece, destination);

        assertThat(move.source()).isEqualTo(source);
    }

    @Test
    void normalSan() {
        var piece = new Rook(source, Color.WHITE);
        var move = Move.normal(piece, destination);

        when(destination.toString()).thenReturn("e1");

        assertThat(move.san()).isEqualTo("Re1");
    }

    @Test
    void captureSan() {
        var piece = new Bishop(source, Color.WHITE);
        var capturedPiece = new Bishop(destination, Color.BLACK);
        var move = Move.capture(piece, capturedPiece);

        when(destination.toString()).thenReturn("c2");

        assertThat(move.san()).isEqualTo("Bxc2");
    }

    @Test
    void pawnCaptureSan() {
        var pawn = new Pawn(source, Color.BLACK);
        var capturedPiece = new Queen(destination, Color.WHITE);
        var move = Move.capture(pawn, capturedPiece);

        when(source.file()).thenReturn("a");
        when(destination.toString()).thenReturn("b7");

        assertThat(move.san()).isEqualTo("axb7");
    }

    @Test
    void kingCastleSan() {
        var king = new King(source, Color.WHITE);
        var rook = new Rook(mock(Coordinate.class), Color.WHITE);
        var move = Move.castle(true, king, destination, rook, mock(Coordinate.class));

        assertThat(move.san()).isEqualTo("O-O");
    }

    @Test
    void queenCastleSan() {
        var king = new King(source, Color.WHITE);
        var rook = new Rook(mock(Coordinate.class), Color.WHITE);
        var move = Move.castle(false, king, destination, rook, mock(Coordinate.class));

        assertThat(move.san()).isEqualTo("O-O-O");
    }

    @Test
    void normalPromotionSan() {
        var pawn = new Pawn(source, Color.WHITE);
        var promotionMove = Move.normalPromotion(pawn, destination, PromotionChoice.KNIGHT);

        when(destination.toString()).thenReturn("f8");

        assertThat(promotionMove.san()).isEqualTo("f8=N");
    }

    @Test
    void capturePromotionSan() {
        var pawn = new Pawn(source, Color.WHITE);
        var capturedPiece = new Knight(destination, Color.BLACK);
        var promotionMove = Move.capturePromotion(pawn, capturedPiece, PromotionChoice.ROOK);

        when(source.file()).thenReturn("e");
        when(destination.toString()).thenReturn("f8");

        assertThat(promotionMove.san()).isEqualTo("exf8=R");
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Move.class)
                .withNonnullFields("type", "piece", "destination")
                .verify();
    }
}
