/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.position.Position;

public final class PseudoLegalGenerator extends MoveGenerator {

    public PseudoLegalGenerator(Position position) {
        super(position);
    }

    @Override
    public List<Move> generate() {
        var moves = new ArrayList<Move>();

        moves.addAll(new PawnMoveGenerator(position).generate());
        moves.addAll(new KnightMoveGenerator(position).generate());

        return moves;
    }
}
