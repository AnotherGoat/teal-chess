/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.position.Position;

final class RookMoveGenerator extends MoveGenerator {

    private static final List<Integer> MOVE_OFFSETS = List.of(8, -1, 1, -8);

    RookMoveGenerator(Position position) {
        super(position);
    }

    @Override
    public List<Move> generate() {
        return null;
    }
}
