/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at COPYRIGHT in the root directory.
 */
package engine.piece;

import com.google.common.collect.ImmutableList;
import engine.board.Coordinate;
import engine.move.Move;
import engine.player.Alliance;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * The king piece. The most important piece in the game, must be defended at all costs. It moves
 * like the queen, but only one space at a time. It also cannot move into a position where it could
 * be captured.
 */
@Getter
@AllArgsConstructor
@ToString(includeFieldNames = false)
public class King implements JumpingPiece {

    private static final Collection<int[]> MOVE_OFFSETS = calculateMoveOffsets();

    private Coordinate position;
    private Alliance alliance;
    private boolean firstMove;

    public King(Coordinate position, Alliance alliance) {
        this(position, alliance, true);
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.KING;
    }

    @Override
    public King move(final Move move) {
        return new King(move.getDestination(), alliance, false);
    }

    @Override
    public Collection<int[]> getMoveOffsets() {
        return MOVE_OFFSETS;
    }

    private static Collection<int[]> calculateMoveOffsets() {
        return Stream.concat(
                        Arrays.stream(Vector.Diagonal.values()),
                        Stream.concat(
                                Arrays.stream(Vector.Horizontal.values()), Arrays.stream(Vector.Vertical.values())))
                .map(Vector::getVector)
                .collect(ImmutableList.toImmutableList());
    }
}
