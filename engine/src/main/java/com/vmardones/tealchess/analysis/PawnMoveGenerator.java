/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.PromotionChoice;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;

final class PawnMoveGenerator extends MoveGenerator {

    @Override
    Stream<Move> generate() {

        for (var pawn : pawns) {
            moves.add(generateEnPassant(pawn, true));
            moves.add(generateEnPassant(pawn, false));
        }
    }

    private @Nullable Move generateEnPassant(Pawn pawn, boolean leftSide) {

        if (enPassantTarget == null) {
            return null;
        }

        var direction = leftSide ? -1 : 1;
        var side = pawn.coordinate().toOrNull(direction, 0);

        if (side == null) {
            return null;
        }

        var destination = side.up(sideToMove.direction());

        if (!destination.equals(enPassantTarget)) {
            return null;
        }

        var sidePiece = board.pieceAt(side);

        if (sidePiece == null || !sidePiece.isPawn()) {
            return null;
        }

        return Move.enPassant(pawn, destination, (Pawn) sidePiece);
    }
}
