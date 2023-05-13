/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.ai;

import java.util.List;

import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.LegalMove;

public sealed interface MoveChooser permits MiniMaxMoveChooser, RandomMoveChooser {
    LegalMove chooseMove(Position position, List<LegalMove> legals);
}
