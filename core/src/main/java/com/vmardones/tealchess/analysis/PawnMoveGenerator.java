/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;

final class PawnMoveGenerator extends MoveGenerator {

    private final Board board;
    private final Color sideToMove;
    private final List<Piece> pieces;
    private final @Nullable Pawn enPassantTarget;

    PawnMoveGenerator(Position position) {
        super(position);
        board = position.board();
        sideToMove = position.sideToMove();
        pieces = board.pieces(sideToMove);
        enPassantTarget = position.enPassantTarget();
    }

    @Override
    Stream<Move> generate() {
        var moves = new ArrayList<@Nullable Move>();

        for (var piece : pieces) {
            if (piece.isPawn()) {
                var pawn = (Pawn) piece;
                moves.add(generateDoublePush(pawn));
                moves.add(generateEnPassant(pawn, true));
                moves.add(generateEnPassant(pawn, false));
            }
        }

        return moves.stream().filter(Objects::nonNull);
    }

    private @Nullable Move generateDoublePush(Pawn pawn) {
        if (pawn.coordinate().rank() != pawn.color().pawnRank()) {
            return null;
        }

        var forward = pawn.coordinate().up(sideToMove.direction());

        if (forward == null || board.pieceAt(forward) != null) {
            return null;
        }

        var destination = forward.up(sideToMove.direction());

        if (destination == null || board.pieceAt(destination) != null) {
            return null;
        }

        return Move.builder(pawn, destination).doublePush();
    }

    private @Nullable Move generateEnPassant(Pawn pawn, boolean leftSide) {

        if (enPassantTarget == null) {
            return null;
        }

        var direction = leftSide ? -1 : 1;
        var side = pawn.coordinate().right(direction);

        if (side == null) {
            return null;
        }

        var destination = side.up(sideToMove.direction());

        if (destination == null || board.pieceAt(destination) != null) {
            return null;
        }

        var sidePiece = board.pieceAt(side);

        if (sidePiece == null || !sidePiece.isPawn() || !sidePiece.equals(enPassantTarget)) {
            return null;
        }

        return Move.builder(pawn, destination).enPassant(enPassantTarget);
    }
}
