/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;
import java.util.Objects;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.player.Color;

/**
 * A piece that can move to a specific set of coordinates. It usually doesn't matter if there are
 * other pieces in the way.
 */
abstract sealed class JumpingPiece extends Piece permits King, Knight, Pawn {

    protected final List<int[]> moveOffsets;

    public List<int[]> moveOffsets() {
        return moveOffsets;
    }

    protected JumpingPiece(PieceType type, String coordinate, Color color, List<int[]> moveOffsets) {
        super(type, coordinate, color);
        this.moveOffsets = moveOffsets;
    }

    @Override
    public List<Coordinate> calculatePossibleDestinations(Board board) {
        return moveOffsets.stream()
                .map(offset -> coordinate().to(offset[0], offset[1]))
                .filter(Objects::nonNull)
                .toList();
    }
}
