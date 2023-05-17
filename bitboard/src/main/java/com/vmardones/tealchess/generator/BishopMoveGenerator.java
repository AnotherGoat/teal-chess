/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.position.Position;

// TODO: Adapt this tomorrow
final class BishopMoveGenerator implements MoveGenerator, DiagonalGenerator {

    @Override
    public List<Move> generate(Position position) {
        var board = position.board();
        var sideToMove = position.sideToMove();
        var bishops = board.bishops(sideToMove);

        if (bishops == 0) {
            return emptyList();
        }

        var moves = new ArrayList<Move>();

        return moves;
    }

    BishopMoveGenerator() {}
}
