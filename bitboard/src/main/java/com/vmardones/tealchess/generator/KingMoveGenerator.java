/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.position.Position;

final class KingMoveGenerator extends MoveGenerator {

    private static final List<Integer> MOVE_OFFSETS = List.of(7, 8, 9, -1, 1, -9, -8, -7);

    KingMoveGenerator(Position position) {
        super(position);
    }

    @Override
    public List<Move> generate() {
        return moves;
    }
}
