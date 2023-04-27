/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.player.Color;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class PieceTest {

    @Test
    void fromSymbol() {
        assertThat(Piece.fromSymbol("p", "a1")).isEqualTo(new Pawn("a1", Color.BLACK));
        assertThat(Piece.fromSymbol("N", "a1")).isEqualTo(new Knight("a1", Color.WHITE));
        assertThat(Piece.fromSymbol("b", "a1")).isEqualTo(new Bishop("a1", Color.BLACK));
        assertThat(Piece.fromSymbol("R", "a1")).isEqualTo(new Rook("a1", Color.WHITE));
        assertThat(Piece.fromSymbol("q", "a1")).isEqualTo(new Queen("a1", Color.BLACK));
        assertThat(Piece.fromSymbol("K", "a1")).isEqualTo(new King("a1", Color.WHITE));
    }

    @Test
    void fromUnknownSymbol() {
        assertThatThrownBy(() -> Piece.fromSymbol("e", "e2"))
                .isInstanceOf(PieceSymbolException.class)
                .hasMessageContaining("piece symbol");
    }

    @Test
    void isAllyOf() {
        var first = new Pawn("a1", Color.WHITE);
        var second = new Rook("a1", Color.WHITE);

        assertThat(first.isAllyOf(second)).isTrue();
        assertThat(first.isEnemyOf(second)).isFalse();
        assertThat(second.isAllyOf(first)).isTrue();
        assertThat(second.isEnemyOf(first)).isFalse();
    }

    @Test
    void isEnemyOf() {
        var first = new Bishop("a1", Color.WHITE);
        var second = new Bishop("a1", Color.BLACK);

        assertThat(first.isAllyOf(second)).isFalse();
        assertThat(first.isEnemyOf(second)).isTrue();
        assertThat(second.isAllyOf(first)).isFalse();
        assertThat(second.isEnemyOf(first)).isTrue();
    }

    @Test
    void isPawn() {
        var pawn = new Pawn("a1", Color.WHITE);
        var notPawn = new Knight("a1", Color.WHITE);

        assertThat(pawn.isPawn()).isTrue();
        assertThat(notPawn.isPawn()).isFalse();
    }

    @Test
    void isKnight() {
        var knight = new Knight("a1", Color.BLACK);
        var notKnight = new Bishop("a1", Color.BLACK);

        assertThat(knight.isKnight()).isTrue();
        assertThat(notKnight.isKnight()).isFalse();
    }

    @Test
    void isBishop() {
        var bishop = new Bishop("a1", Color.WHITE);
        var notBishop = new Rook("a1", Color.WHITE);

        assertThat(bishop.isBishop()).isTrue();
        assertThat(notBishop.isBishop()).isFalse();
    }

    @Test
    void isRook() {
        var rook = new Rook("a1", Color.BLACK);
        var notRook = new Queen("a1", Color.BLACK);

        assertThat(rook.isRook()).isTrue();
        assertThat(notRook.isRook()).isFalse();
    }

    @Test
    void isQueen() {
        var queen = new Queen("a1", Color.WHITE);
        var notQueen = new King("a1", Color.WHITE);

        assertThat(queen.isQueen()).isTrue();
        assertThat(notQueen.isQueen()).isFalse();
    }

    @Test
    void isKing() {
        var king = new King("a1", Color.BLACK);
        var notKing = new Pawn("a1", Color.BLACK);

        assertThat(king.isKing()).isTrue();
        assertThat(notKing.isKing()).isFalse();
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Piece.class)
                .withNonnullFields("coordinate", "moveVectors")
                .verify();
    }
}
