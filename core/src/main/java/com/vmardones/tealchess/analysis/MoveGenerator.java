/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.stream.Stream;

import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Move;

abstract sealed class MoveGenerator
        permits CaptureGenerator, CastleGenerator, NormalGenerator, PawnMoveGenerator, PseudoLegalGenerator {

    protected final Position position;

    MoveGenerator(Position position) {
        this.position = position;
    }

    abstract Stream<Move> generate();
}
