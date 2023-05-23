/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.search;

import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.position.Position;

public sealed interface MoveChooser permits MinimaxMoveChooser, NegamaxMoveChooser, RandomMoveChooser {
    Move chooseMove(Position position, List<Move> legals);
}
