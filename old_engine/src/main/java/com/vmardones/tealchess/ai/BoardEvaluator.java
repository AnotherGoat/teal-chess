/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.ai;

import com.vmardones.tealchess.position.Position;

public sealed interface BoardEvaluator permits PieceValueEvaluator {
    int evaluate(Position position);
}
