/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;
import java.util.Objects;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.player.Color;

/**
 * A piece that can move to a specific set of positions. It usually doesn't matter if there are
 * other pieces in the way.
 */
abstract sealed class JumpingPiece extends Piece permits King, Knight, Pawn {

    protected final List<int[]> moveOffsets;

    public List<int[]> moveOffsets() {
        return moveOffsets;
    }

    protected JumpingPiece(String position, Color color, boolean firstMove, List<int[]> moveOffsets) {
        super(position, color, firstMove);
        this.moveOffsets = moveOffsets;
    }

    @Override
    public List<Position> calculatePossibleDestinations(Board board) {
        return moveOffsets.stream()
                .map(offset -> position().to(offset[0], offset[1]))
                .filter(Objects::nonNull)
                .toList();
    }
}
