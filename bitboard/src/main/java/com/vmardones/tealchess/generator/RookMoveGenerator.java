/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.position.Position;

final class RookMoveGenerator implements MoveGenerator, OrthogonalGenerator {

    @Override
    public List<Move> generate(Position position) {
        var board = position.board();
        var sideToMove = position.sideToMove();
        var rooks = board.rooks(sideToMove);

        if (rooks == 0) {
            return emptyList();
        }

        var moves = new ArrayList<Move>();

        return moves;
    }

    RookMoveGenerator() {}
}
