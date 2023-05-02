/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.ai;

import java.util.List;
import java.util.Random;

import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.LegalMove;

public final class RandomMoveChooser implements MoveChooser {

    private final Random random = new Random();

    @Override
    public LegalMove chooseMove(Position position, List<LegalMove> legals) {
        return legals.get(random.nextInt(legals.size()));
    }
}
