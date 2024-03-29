/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.evaluation;

import com.vmardones.tealchess.game.GameMemento;

public sealed interface BoardEvaluator permits MaterialEvaluator, MobilityEvaluator {
    int evaluate(GameMemento state);
}
