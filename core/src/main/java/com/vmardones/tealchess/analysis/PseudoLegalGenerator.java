/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.stream.Stream;

import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Move;

final class PseudoLegalGenerator extends MoveGenerator {

    PseudoLegalGenerator(Position position) {
        super(position);
    }

    @Override
    Stream<Move> generate() {
        var moves = new NormalGenerator(position).generate();
        var captures = new CaptureGenerator(position).generate();
        var pawnMoves = new PawnMoveGenerator(position).generate();
        var castles = new CastleGenerator(position).generate();

        return Stream.concat(Stream.concat(moves, captures), Stream.concat(pawnMoves, castles));
    }
}
