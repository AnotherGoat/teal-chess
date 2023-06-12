/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.evaluation;

import com.vmardones.tealchess.game.GameMemento;

public final class MobilityEvaluator implements BoardEvaluator {
    @Override
    public int evaluate(GameMemento state) {
        return state.player().legals().size();
    }
}
