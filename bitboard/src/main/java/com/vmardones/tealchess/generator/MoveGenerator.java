/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.position.Position;

public abstract sealed class MoveGenerator
        permits BishopMoveGenerator,
                KingMoveGenerator,
                KnightMoveGenerator,
                PawnMoveGenerator,
                PseudoLegalGenerator,
                QueenMoveGenerator,
                RookMoveGenerator {

    protected final Position position;
    protected final List<Move> moves = new ArrayList<>();

    protected MoveGenerator(Position position) {
        this.position = position;
    }

    abstract List<Move> generate();
}
