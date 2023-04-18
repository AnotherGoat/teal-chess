/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.player.Color;
import org.junit.jupiter.api.Test;

class PawnTest {

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
}
