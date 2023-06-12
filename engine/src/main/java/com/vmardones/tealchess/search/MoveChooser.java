/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.search;

import com.vmardones.tealchess.game.GameMemento;
import com.vmardones.tealchess.move.Move;

public sealed interface MoveChooser permits MinimaxMoveChooser, NegamaxMoveChooser, RandomMoveChooser {
    Move chooseMove(GameMemento state);
}
