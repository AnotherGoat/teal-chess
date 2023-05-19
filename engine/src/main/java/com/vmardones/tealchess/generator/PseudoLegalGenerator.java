/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.position.Position;

final class PseudoLegalGenerator implements MoveGenerator {

    private final MoveGenerator pawnMoveGenerator;
    private final MoveGenerator knightMoveGenerator;
    private final MoveGenerator bishopMoveGenerator;
    private final MoveGenerator rookMoveGenerator;
    private final MoveGenerator queenMoveGenerator;
    private final MoveGenerator kingMoveGenerator;

    @Override
    public List<Move> generate(Position position) {
        var moves = new ArrayList<Move>();

        moves.addAll(pawnMoveGenerator.generate(position));
        moves.addAll(knightMoveGenerator.generate(position));
        moves.addAll(bishopMoveGenerator.generate(position));
        moves.addAll(rookMoveGenerator.generate(position));
        moves.addAll(queenMoveGenerator.generate(position));
        moves.addAll(kingMoveGenerator.generate(position));

        return moves;
    }

    PseudoLegalGenerator() {
        pawnMoveGenerator = new PawnMoveGenerator();
        knightMoveGenerator = new KnightMoveGenerator();
        bishopMoveGenerator = new BishopMoveGenerator();
        rookMoveGenerator = new RookMoveGenerator();
        queenMoveGenerator = new QueenMoveGenerator();
        kingMoveGenerator = new KingMoveGenerator();
    }
}
