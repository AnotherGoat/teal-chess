/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.player.Color;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExcludeFromNullAway
@ExtendWith(MockitoExtension.class)
final class PieceTest {

    @Mock
    Coordinate coordinate;

    @Test
    void fromSymbol() {
        assertThat(Piece.fromSymbol("p", coordinate)).isEqualTo(new Pawn(coordinate, Color.BLACK));
        assertThat(Piece.fromSymbol("N", coordinate)).isEqualTo(new Knight(coordinate, Color.WHITE));
        assertThat(Piece.fromSymbol("b", coordinate)).isEqualTo(new Bishop(coordinate, Color.BLACK));
        assertThat(Piece.fromSymbol("R", coordinate)).isEqualTo(new Rook(coordinate, Color.WHITE));
        assertThat(Piece.fromSymbol("q", coordinate)).isEqualTo(new Queen(coordinate, Color.BLACK));
        assertThat(Piece.fromSymbol("K", coordinate)).isEqualTo(new King(coordinate, Color.WHITE));
    }

    @Test
    void fromUnknownSymbol() {
        assertThatThrownBy(() -> Piece.fromSymbol("e", coordinate))
                .isInstanceOf(PieceSymbolException.class)
                .hasMessageContaining("piece symbol");
    }

    @Test
    void isAllyOf() {
        var first = new Pawn(coordinate, Color.WHITE);
        var second = new Rook(coordinate, Color.WHITE);

        assertThat(first.isAllyOf(second)).isTrue();
        assertThat(first.isEnemyOf(second)).isFalse();
        assertThat(second.isAllyOf(first)).isTrue();
        assertThat(second.isEnemyOf(first)).isFalse();
    }

    @Test
    void isEnemyOf() {
        var first = new Bishop(coordinate, Color.WHITE);
        var second = new Bishop(coordinate, Color.BLACK);

        assertThat(first.isAllyOf(second)).isFalse();
        assertThat(first.isEnemyOf(second)).isTrue();
        assertThat(second.isAllyOf(first)).isFalse();
        assertThat(second.isEnemyOf(first)).isTrue();
    }

    @Test
    void isPawn() {
        var pawn = new Pawn(coordinate, Color.WHITE);
        var notPawn = new Knight(coordinate, Color.WHITE);

        assertThat(pawn.isPawn()).isTrue();
        assertThat(notPawn.isPawn()).isFalse();
    }

    @Test
    void isKnight() {
        var knight = new Knight(coordinate, Color.BLACK);
        var notKnight = new Bishop(coordinate, Color.BLACK);

        assertThat(knight.isKnight()).isTrue();
        assertThat(notKnight.isKnight()).isFalse();
    }

    @Test
    void isBishop() {
        var bishop = new Bishop(coordinate, Color.WHITE);
        var notBishop = new Rook(coordinate, Color.WHITE);

        assertThat(bishop.isBishop()).isTrue();
        assertThat(notBishop.isBishop()).isFalse();
    }

    @Test
    void isRook() {
        var rook = new Rook(coordinate, Color.BLACK);
        var notRook = new Queen(coordinate, Color.BLACK);

        assertThat(rook.isRook()).isTrue();
        assertThat(notRook.isRook()).isFalse();
    }

    @Test
    void isQueen() {
        var queen = new Queen(coordinate, Color.WHITE);
        var notQueen = new King(coordinate, Color.WHITE);

        assertThat(queen.isQueen()).isTrue();
        assertThat(notQueen.isQueen()).isFalse();
    }

    @Test
    void isKing() {
        var king = new King(coordinate, Color.BLACK);
        var notKing = new Pawn(coordinate, Color.BLACK);

        assertThat(king.isKing()).isTrue();
        assertThat(notKing.isKing()).isFalse();
    }

    @Test
    void normalSan() {
        var piece = new Rook(coordinate, Color.WHITE);
        assertThat(piece.san()).isUpperCase().isEqualTo("R");
    }

    @Test
    void pawnSan() {
        var pawn = new Pawn(coordinate, Color.WHITE);
        assertThat(pawn.san()).isEmpty();
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Piece.class)
                .withNonnullFields("coordinate", "moveVectors")
                .verify();
    }
}
