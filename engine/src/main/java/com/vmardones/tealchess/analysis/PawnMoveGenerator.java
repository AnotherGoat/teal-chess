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
            generatePushes(pawn).forEach(moves::add);
            moves.add(generateDoublePush(pawn));
            generateCaptures(pawn, true).forEach(moves::add);
            generateCaptures(pawn, false).forEach(moves::add);
            moves.add(generateEnPassant(pawn, true));
            moves.add(generateEnPassant(pawn, false));
        }
    }

    private Stream<Move> createPushes(Pawn pawn, Coordinate destination) {

        var destinationPiece = board.pieceAt(destination);

        if (destinationPiece != null) {
            return Stream.empty();
        }

        if (pawn.canBePromoted()) {
            return Arrays.stream(PromotionChoice.values())
                    .map(choice -> Move.normalPromotion(pawn, destination, choice));
        }

        return Stream.of(Move.normal(pawn, destination));
    }

    private @Nullable Move generateDoublePush(Pawn pawn) {
        if (!pawn.canDoublePush()) {
            return null;
        }

        var forward = pawn.coordinate().up(sideToMove.direction());

        if (board.pieceAt(forward) != null) {
            return null;
        }

        var destination = forward.up(sideToMove.direction());

        if (board.pieceAt(destination) != null) {
            return null;
        }

        return Move.doublePush(pawn, destination);
    }

    private Stream<Move> generateCaptures(Pawn pawn, boolean leftSide) {

        var direction = leftSide ? -1 : 1;

        var destination = pawn.coordinate().toOrNull(direction, pawn.color().direction());

        if (destination == null) {
            return Stream.empty();
        }

        var destinationPiece = board.pieceAt(destination);

        if (destinationPiece == null || pawn.isAllyOf(destinationPiece)) {
            return Stream.empty();
        }

        if (pawn.canBePromoted()) {
            return Arrays.stream(PromotionChoice.values())
                    .map(choice -> Move.capturePromotion(pawn, destinationPiece, choice));
        }

        return Stream.of(Move.capture(pawn, destinationPiece));
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
