/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cl.vmardones.chess.engine.board.Square;
import cl.vmardones.chess.engine.piece.Knight;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.player.Color;
import org.junit.jupiter.api.Test;

class AttackTest {

    @Test
    void notAnAttack() {
        var pawn = new Pawn("g2", Color.WHITE);
        var square = Square.create("g2", null);

        assertThatThrownBy(() -> new Attack(pawn, square))
                .isInstanceOf(IllegalMoveException.class)
                .hasMessageContaining("cannot be the same");
    }

    @Test
    void asString() {
        var knight = new Knight("g2", Color.WHITE);
        var attackedSquare = Square.create("e3", null);

        assertThat(new Attack(knight, attackedSquare)).hasToString("g2e3");
    }
}
