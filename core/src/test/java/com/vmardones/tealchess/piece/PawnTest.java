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
    void toSingleChar() {
        assertThat(new Pawn("a1", Color.WHITE).singleChar()).isEqualTo("P");
        assertThat(new Pawn("a1", Color.BLACK).singleChar()).isEqualTo("p");
    }

    @Test
    void whiteMoves() {
        var whitePawn = new Pawn("a1", Color.WHITE);
        assertThat(whitePawn.moveOffsets()).hasSize(1).containsOnlyOnce(new int[] {0, 1});
    }

    @Test
    void blackMoves() {
        var blackPawn = new Pawn("a1", Color.BLACK);
        assertThat(blackPawn.moveOffsets()).hasSize(1).containsOnlyOnce(new int[] {0, -1});
    }

    @Test
    void illegalMove() {
        var pawn = new Pawn("a1", Color.WHITE);
        assertThat(pawn.moveOffsets()).isNotEmpty().doesNotContain(new int[] {1, 0});
    }

    @Test
    void moveTo() {
        var pawnToMove = new Pawn("a1", Color.WHITE);

        assertThat(pawnToMove.moveTo("a2")).isInstanceOf(Pawn.class).matches(pawn -> pawn.coordinate()
                .equals(Coordinate.of("a2")));
    }

    @Test
    void promote() {
        var pawn = new Pawn("h5", Color.BLACK);
        assertThat(pawn.promote()).isEqualTo(new Queen("h5", Color.BLACK));
    }
}
