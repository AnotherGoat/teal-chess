/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.ai;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.position.Position;

public final class PieceValueEvaluator implements BoardEvaluator {
    @Override
    public int evaluate(Position position) {
        return scoreSide(position, position.sideToMove())
                - scoreSide(position, position.sideToMove().opposite());
    }

    private int scoreSide(Position position, Color side) {
        return 0;

        return pieceValue(position.board().pieces(side));
    }

    private int pieceValue(Set<Piece> pieces) {
        return pieces.stream().mapToInt(Piece::value).sum();
    }
}
