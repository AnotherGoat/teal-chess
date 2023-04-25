/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import com.vmardones.tealchess.move.LegalMove;
import org.eclipse.jdt.annotation.Nullable;

// TODO: Make this memento save more things than just the last position
record GameMemento(Position position, @Nullable LegalMove lastMove) {}
