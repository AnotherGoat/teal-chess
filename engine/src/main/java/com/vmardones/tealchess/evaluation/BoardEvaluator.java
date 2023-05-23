/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.evaluation;

import com.vmardones.tealchess.position.Position;

public sealed interface BoardEvaluator permits MaterialEvaluator {
    int evaluate(Position position);
}
