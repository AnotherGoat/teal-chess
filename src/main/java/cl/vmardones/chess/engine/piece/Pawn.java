/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Square;
import cl.vmardones.chess.engine.move.*;
import cl.vmardones.chess.engine.player.Alliance;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The pawn piece. It only moves forward (depending on the side) and can eat other pieces
 * diagonally. A very weak piece, but it can be promoted when getting to the last rank at the
 * opposite side.
 */
public final class Pawn extends JumpingPiece {

    private static final List<int[]> WHITE_MOVES =
            List.of(new int[] {-1, 1}, new int[] {0, 1}, new int[] {1, 1}, new int[] {0, 2});
    private static final List<int[]> BLACK_MOVES =
            List.of(new int[] {-1, -1}, new int[] {0, -1}, new int[] {1, -1}, new int[] {0, -2});

    public Pawn(String position, Alliance alliance) {
        this(position, alliance, true);
    }

    @Override
    public Pawn moveTo(String destination) {
        return new Pawn(destination, alliance, false);
    }

    private Pawn(String position, Alliance alliance, boolean firstMove) {
        super(position, alliance, firstMove, calculateMoves(alliance, firstMove));
    }

    private static List<int[]> calculateMoves(Alliance alliance, boolean firstMove) {
        return switch (alliance) {
            case WHITE -> firstMove ? WHITE_MOVES : WHITE_MOVES.subList(0, 3);
            case BLACK -> firstMove ? BLACK_MOVES : BLACK_MOVES.subList(0, 3);
        };
    }
}
