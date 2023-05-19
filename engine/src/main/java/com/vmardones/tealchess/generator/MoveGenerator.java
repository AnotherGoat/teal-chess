/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;

import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.position.Position;

public sealed interface MoveGenerator
        permits BishopMoveGenerator,
                KingMoveGenerator,
                KnightMoveGenerator,
                LegalGenerator,
                PawnMoveGenerator,
                PseudoLegalGenerator,
                QueenMoveGenerator,
                RookMoveGenerator {
    List<Move> generate(Position position);

    default void addMoves(List<Move> moves, MoveType type, long movesToAdd, int source) {
        if (movesToAdd == 0) {
            return;
        }

        var destination = firstBit(movesToAdd);

        do {
            moves.add(new Move(type, source, destination));

            movesToAdd = clear(movesToAdd, destination);
            destination = firstBit(movesToAdd);
        } while (isSet(movesToAdd, destination));
    }
}
