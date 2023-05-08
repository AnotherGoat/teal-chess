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
import com.vmardones.tealchess.piece.*;
import com.vmardones.tealchess.player.Color;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExcludeFromNullAway
@ExtendWith(MockitoExtension.class)
final class MoveTest {

    @Mock
    Coordinate source;

    @Mock
    Coordinate destination;

    @Test
    void source() {
        var piece = mock(Knight.class);
        when(piece.coordinate()).thenReturn(source);

        var move = Move.builder(piece, destination).normal();

        assertThat(move.source()).isEqualTo(source);
    }

    @Test
    void illegalDoublePush() {
        var bishop = new Bishop(source, Color.BLACK);
        var builder = Move.builder(bishop, destination);

        assertThatThrownBy(builder::doublePush)
                .isInstanceOf(IllegalMoveException.class)
                .hasMessageContaining("double push");
    }

    @Test
    void illegalEnPassant() {
        var queen = new Queen(source, Color.BLACK);
        var builder = Move.builder(queen, destination);
        var pawn = mock(Pawn.class);

        assertThatThrownBy(() -> builder.enPassant(pawn))
                .isInstanceOf(IllegalMoveException.class)
                .hasMessageContaining("en passant");
    }

    @Test
    void illegalCastle() {
        var knight = new Knight(source, Color.WHITE);
        var builder = Move.builder(knight, destination);
        var rook = mock(Rook.class);
        var rookDestination = mock(Coordinate.class);

        assertThatThrownBy(() -> builder.castle(true, rook, rookDestination))
                .isInstanceOf(IllegalMoveException.class)
                .hasMessageContaining("castling");
    }

    @Test
    void illegalPromotion() {
        var king = new King(source, Color.BLACK);
        var move = Move.builder(king, destination).normal();

        assertThatThrownBy(() -> move.makePromotion(PromotionChoice.ROOK))
                .isInstanceOf(IllegalMoveException.class)
                .hasMessageContaining("pawn");
    }

    @Test
    void normalSan() {
        var piece = new Rook(source, Color.WHITE);
        var move = Move.builder(piece, destination).normal();

        when(destination.toString()).thenReturn("e1");

        assertThat(move.san()).isEqualTo("Re1");
    }

    @Test
    void captureSan() {
        var piece = new Bishop(source, Color.WHITE);
        var capturedPiece = new Bishop(destination, Color.BLACK);
        var move = Move.builder(piece, destination).capture(capturedPiece);

        when(destination.toString()).thenReturn("c2");

        assertThat(move.san()).isEqualTo("Bxc2");
    }

    @Test
    void pawnCaptureSan() {
        var pawn = new Pawn(source, Color.BLACK);
        var capturedPawn = new Pawn(source, Color.WHITE);
        var move = Move.builder(pawn, destination).capture(capturedPawn);

        when(source.file()).thenReturn("a");
        when(destination.toString()).thenReturn("b7");

        assertThat(move.san()).isEqualTo("axb7");
    }

    @Test
    void kingCastleSan() {
        var king = new King(source, Color.WHITE);
        var rook = new Rook(mock(Coordinate.class), Color.WHITE);
        var move = Move.builder(king, destination).castle(true, rook, mock(Coordinate.class));

        assertThat(move.san()).isEqualTo("O-O");
    }

    @Test
    void queenCastleSan() {
        var king = new King(source, Color.WHITE);
        var rook = new Rook(mock(Coordinate.class), Color.WHITE);
        var move = Move.builder(king, destination).castle(false, rook, mock(Coordinate.class));

        assertThat(move.san()).isEqualTo("O-O-O");
    }

    @Test
    void promotionSan() {
        var pawn = new Pawn(source, Color.WHITE);
        var normalMove = Move.builder(pawn, destination).normal();
        var promotionMove = normalMove.makePromotion(PromotionChoice.KNIGHT);

        when(destination.toString()).thenReturn("f8");

        assertThat(promotionMove.san()).isEqualTo("f8=N");
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Move.class)
                .withNonnullFields("type", "piece", "destination")
                .verify();
    }
}
