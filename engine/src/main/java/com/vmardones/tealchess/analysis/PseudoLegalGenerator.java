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
        var builder = Stream.<Move>builder();

        new NormalGenerator(position).generate().forEach(builder::add);
        new CaptureGenerator(position).generate().forEach(builder::add);
        new CastleGenerator(position).generate().forEach(builder::add);

        return builder.build();
    }
}
