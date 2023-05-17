/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.square.AlgebraicConverter;

import static com.vmardones.tealchess.board.BitboardManipulator.*;

final class KingMoveGenerator implements MoveGenerator, LookupGenerator {

    @Override
    public List<Move> generate(Position position) {
        var board = position.board();
        var sideToMove = position.sideToMove();
        var king = board.kings(sideToMove);

        var moves = new ArrayList<Move>();

        return moves;
    }

    KingMoveGenerator() {}
}
