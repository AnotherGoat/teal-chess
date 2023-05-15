/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.coordinate.Coordinate.*;
import static com.vmardones.tealchess.move.MoveType.PAWN_PUSH;
import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.parser.fen.FenParser;
import org.junit.jupiter.api.Test;

final class PawnMoveGeneratorTest {

    @Test
    void whitePawnPushes() {
        var position = FenParser.parse("4k3/8/8/8/6p1/1P4P1/8/4K3 w - - 0 1");
        var generator = new PawnMoveGenerator(position);
        var expectedMove = new Move(PAWN_PUSH, b3, b4);

        assertThat(generator.generate()).hasSize(1).containsOnlyOnce(expectedMove);
    }

    @Test
    void blackPawnPushes() {
        var position = FenParser.parse("4k3/8/1p4p1/6P1/8/8/8/4K3 b - - 0 1");
        var generator = new PawnMoveGenerator(position);
        var expectedMove = new Move(PAWN_PUSH, b6, b5);

        assertThat(generator.generate()).hasSize(1).containsOnlyOnce(expectedMove);
    }
}
