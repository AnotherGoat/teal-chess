/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.position.Position;

final class KnightMoveGenerator extends MoveGenerator {

    private static final List<Integer> MOVE_OFFSETS = List.of(15, 17, 6, 10, -10, -6, -17, -15);

    KnightMoveGenerator(Position position) {
        super(position);
    }

    @Override
    public List<Move> generate() {
        return null;
    }
}
