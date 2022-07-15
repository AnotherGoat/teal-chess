package piece;

import board.BoardUtils;
import player.Alliance;

/**
 * The bishop piece.
 * It moves diagonally.
 */
public final class Bishop extends SlidingPiece {

    private static final int[] MOVE_VECTORS = {-9, -7, 7, 9};

    public Bishop(int coordinate, Alliance alliance) {
        super(coordinate, alliance);
    }

    @Override
    int[] getMoveVectors() {
        return MOVE_VECTORS;
    }

    @Override
    protected boolean isIllegalMove(int destination) {
        return !BoardUtils.sameColor(position, destination);
    }

    @Override
    public String toString() {
        return PieceType.BISHOP.getPieceName();
    }
}
