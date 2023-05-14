/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.position.Position;

final class BishopMoveGenerator implements MoveGenerator {

    private static final List<Integer> MOVE_OFFSETS = List.of(7, 9, -9, -7);

    @Override
    public List<Move> generate(Position position) {
        return null;
    }
}
