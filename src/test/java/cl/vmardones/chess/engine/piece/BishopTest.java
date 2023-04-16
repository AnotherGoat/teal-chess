/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.player.Color;
import org.junit.jupiter.api.Test;

class BishopTest {

    @Test
    void constructor() {
        assertThat(new Bishop("a1", Color.BLACK)).matches(Bishop::firstMove);
    }

    @Test
    void toSingleChar() {
        assertThat(new Bishop("a1", Color.WHITE).singleChar()).isEqualTo("B");
        assertThat(new Bishop("a1", Color.BLACK).singleChar()).isEqualTo("b");
    }

    @Test
    void numberOfMoves() {
        var bishop = new Bishop("a1", Color.BLACK);
        assertThat(bishop.moveVectors()).hasSize(4);
    }

    @Test
    void diagonalMove() {
        var bishop = new Bishop("a1", Color.BLACK);
        assertThat(bishop.moveVectors()).containsOnlyOnce(new int[] {1, 1});
    }

    @Test
    void illegalMove() {
        var bishop = new Bishop("a1", Color.BLACK);
        assertThat(bishop.moveVectors()).isNotEmpty().doesNotContain(new int[] {0, 1});
    }

    @Test
    void moveTo() {
        var bishopToMove = new Bishop("a1", Color.BLACK);

        assertThat(bishopToMove.moveTo("a2"))
                .isInstanceOf(Bishop.class)
                .matches(bishop -> bishop.position().equals(Position.of("a2")))
                .matches(bishop -> !bishop.firstMove());
    }
}
