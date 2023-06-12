/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.search;

import java.util.Random;

import com.vmardones.tealchess.game.GameMemento;
import com.vmardones.tealchess.move.Move;

public final class RandomMoveChooser implements MoveChooser {

    private final Random random = new Random();

    @Override
    public Move chooseMove(GameMemento state) {
        var legals = state.player().legals();
        return legals.get(random.nextInt(legals.size()));
    }
}
