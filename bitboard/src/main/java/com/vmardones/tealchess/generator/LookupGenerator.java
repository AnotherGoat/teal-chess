/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;

import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;

interface LookupGenerator {

    default long shiftPattern(long pattern, int patternCenter, int newCenter) {
        if (newCenter > patternCenter) {
            return pattern << (newCenter - patternCenter);
        }

        return pattern >> (patternCenter - newCenter);
    }

    default void addMoves(List<Move> moves, MoveType type, long possibleMoves, int source) {
        if (possibleMoves == 0) {
            return;
        }

        var destination = firstBit(possibleMoves);

        do {
            moves.add(new Move(type, source, destination));

            possibleMoves = clear(possibleMoves, destination);
            destination = firstBit(possibleMoves);
        } while (isSet(possibleMoves, destination));
    }
}
