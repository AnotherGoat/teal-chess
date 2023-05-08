/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExcludeFromNullAway
@ExtendWith(MockitoExtension.class)
final class PawnTest {

    @Mock
    Coordinate coordinate;

    @Test
    void fen() {
        assertThat(new Pawn(coordinate, Color.WHITE).fen()).isEqualTo("P");
        assertThat(new Pawn(coordinate, Color.BLACK).fen()).isEqualTo("p");
    }

    @Test
    void whiteMoves() {
        var whitePawn = new Pawn(coordinate, Color.WHITE);
        assertThat(whitePawn.moveVectors()).hasSize(1).containsOnlyOnce(new Vector(0, 1));
    }

    @Test
    void blackMoves() {
        var blackPawn = new Pawn(coordinate, Color.BLACK);
        assertThat(blackPawn.moveVectors()).hasSize(1).containsOnlyOnce(new Vector(0, -1));
    }

    @Test
    void illegalMove() {
        var pawn = new Pawn(coordinate, Color.WHITE);
        assertThat(pawn.moveVectors()).isNotEmpty().doesNotContain(new Vector(1, 0));
    }

    @Test
    void moveTo() {
        var pawnToMove = new Pawn(coordinate, Color.WHITE);
        var destination = mock(Coordinate.class);

        assertThat(pawnToMove.moveTo(destination)).isInstanceOf(Pawn.class).matches(pawn -> pawn.coordinate()
                .equals(destination));
    }

    @Test
    void canWhiteDoublePush() {
        when(coordinate.rank()).thenReturn(2);

        var pawn = new Pawn(coordinate, Color.WHITE);
        assertThat(pawn.canDoublePush()).isTrue();

        when(coordinate.rank()).thenReturn(7);

        var otherPawn = new Pawn(coordinate, Color.WHITE);
        assertThat(otherPawn.canDoublePush()).isFalse();
    }

    @Test
    void canBlackDoublePush() {
        when(coordinate.rank()).thenReturn(7);

        var pawn = new Pawn(coordinate, Color.BLACK);
        assertThat(pawn.canDoublePush()).isTrue();

        when(coordinate.rank()).thenReturn(2);

        var otherPawn = new Pawn(coordinate, Color.BLACK);
        assertThat(otherPawn.canDoublePush()).isFalse();
    }

    @Test
    void canWhiteBePromoted() {
        when(coordinate.rank()).thenReturn(7);

        var pawn = new Pawn(coordinate, Color.WHITE);
        assertThat(pawn.canBePromoted()).isTrue();

        when(coordinate.rank()).thenReturn(2);

        var otherPawn = new Pawn(coordinate, Color.WHITE);
        assertThat(otherPawn.canBePromoted()).isFalse();
    }

    @Test
    void canBlackBePromoted() {
        when(coordinate.rank()).thenReturn(2);

        var pawn = new Pawn(coordinate, Color.BLACK);
        assertThat(pawn.canBePromoted()).isTrue();

        when(coordinate.rank()).thenReturn(7);

        var otherPawn = new Pawn(coordinate, Color.BLACK);
        assertThat(otherPawn.canBePromoted()).isFalse();
    }

    @Test
    void promoteToKnight() {
        var pawn = new Pawn(coordinate, Color.BLACK);
        assertThat(pawn.promote(PromotionChoice.KNIGHT)).isEqualTo(new Knight(coordinate, Color.BLACK));
    }

    @Test
    void promoteToBishop() {
        var pawn = new Pawn(coordinate, Color.BLACK);
        assertThat(pawn.promote(PromotionChoice.BISHOP)).isEqualTo(new Bishop(coordinate, Color.BLACK));
    }

    @Test
    void promoteToRook() {
        var pawn = new Pawn(coordinate, Color.BLACK);
        assertThat(pawn.promote(PromotionChoice.ROOK)).isEqualTo(new Rook(coordinate, Color.BLACK));
    }

    @Test
    void promoteToQueen() {
        var pawn = new Pawn(coordinate, Color.BLACK);
        assertThat(pawn.promote(PromotionChoice.QUEEN)).isEqualTo(new Queen(coordinate, Color.BLACK));
    }
}
