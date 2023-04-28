/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.player.Player;
import org.eclipse.jdt.annotation.Nullable;

record GameMemento(Position position, Player whitePlayer, Player blackPlayer, @Nullable LegalMove lastMove) {}
