/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.piece.Knight;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class AttackTest {

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
