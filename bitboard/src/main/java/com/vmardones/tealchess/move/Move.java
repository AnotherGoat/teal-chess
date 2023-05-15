/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import com.vmardones.tealchess.piece.PromotionChoice;
import org.eclipse.jdt.annotation.Nullable;

public record Move(MoveType type, int source, int destination, @Nullable PromotionChoice choice) {
    public Move(MoveType type, int source, int destination) {
        this(type, source, destination, null);
    }
}
