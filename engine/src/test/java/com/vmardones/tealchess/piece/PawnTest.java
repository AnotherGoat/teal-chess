/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class PawnTest {

    @Test
    void fen() {
        assertThat(new Pawn("a1", Color.WHITE).fen()).isEqualTo("P");
        assertThat(new Pawn("a1", Color.BLACK).fen()).isEqualTo("p");
    }

    @Test
    void whiteMoves() {
        var whitePawn = new Pawn("a1", Color.WHITE);
        assertThat(whitePawn.moveVectors()).hasSize(1).containsOnlyOnce(new Vector(0, 1));
    }

    @Test
    void blackMoves() {
        var blackPawn = new Pawn("a1", Color.BLACK);
        assertThat(blackPawn.moveVectors()).hasSize(1).containsOnlyOnce(new Vector(0, -1));
    }

    @Test
    void illegalMove() {
        var pawn = new Pawn("a1", Color.WHITE);
        assertThat(pawn.moveVectors()).isNotEmpty().doesNotContain(new Vector(1, 0));
    }

    @Test
    void moveTo() {
        var pawnToMove = new Pawn("a1", Color.WHITE);

        assertThat(pawnToMove.moveTo("a2")).isInstanceOf(Pawn.class).matches(pawn -> pawn.coordinate()
                .equals(Coordinate.of("a2")));
    }

    @Test
    void canWhiteBePromoted() {
        var pawn = new Pawn("h7", Color.WHITE);
        assertThat(pawn.canBePromoted()).isTrue();

        var otherPawn = new Pawn("h2", Color.WHITE);
        assertThat(otherPawn.canBePromoted()).isFalse();
    }

    @Test
    void canBlackBePromoted() {
        var pawn = new Pawn("h2", Color.BLACK);
        assertThat(pawn.canBePromoted()).isTrue();

        var otherPawn = new Pawn("h7", Color.BLACK);
        assertThat(otherPawn.canBePromoted()).isFalse();
    }

    @Test
    void promoteToKnight() {
        var pawn = new Pawn("h5", Color.BLACK);
        assertThat(pawn.promote(PromotionChoice.KNIGHT)).isEqualTo(new Knight("h5", Color.BLACK));
    }

    @Test
    void promoteToBishop() {
        var pawn = new Pawn("h5", Color.BLACK);
        assertThat(pawn.promote(PromotionChoice.BISHOP)).isEqualTo(new Bishop("h5", Color.BLACK));
    }

    @Test
    void promoteToRook() {
        var pawn = new Pawn("h5", Color.BLACK);
        assertThat(pawn.promote(PromotionChoice.ROOK)).isEqualTo(new Rook("h5", Color.BLACK));
    }

    @Test
    void promoteToQueen() {
        var pawn = new Pawn("h5", Color.BLACK);
        assertThat(pawn.promote(PromotionChoice.QUEEN)).isEqualTo(new Queen("h5", Color.BLACK));
    }
}