/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;

interface LookupGenerator {

    default long shiftPattern(long pattern, int patternCenter, int newCenter) {
        if (newCenter > patternCenter) {
            return pattern << (newCenter - patternCenter);
        }

        return pattern >> (patternCenter - newCenter);
    }
}
