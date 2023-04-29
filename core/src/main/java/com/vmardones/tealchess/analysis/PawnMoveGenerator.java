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
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.PromotionChoice;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;

final class PawnMoveGenerator extends MoveGenerator {

    private final Board board;
    private final Color sideToMove;
    private final List<Pawn> pawns;
    private final @Nullable Square enPassantTarget;

    PawnMoveGenerator(Position position) {
        super(position);
        board = position.board();
        sideToMove = position.sideToMove();
        pawns = board.pieces(sideToMove).stream()
                .filter(Piece::isPawn)
                .map(Pawn.class::cast)
                .toList();
        enPassantTarget = position.enPassantTarget();
    }

    @Override
    Stream<Move> generate() {
        var moves = Stream.<@Nullable Move>builder();

        for (var pawn : pawns) {
            generatePushes(pawn).forEach(moves::add);
            moves.add(generateDoublePush(pawn));
            generateCaptures(pawn, true).forEach(moves::add);
            generateCaptures(pawn, false).forEach(moves::add);
            moves.add(generateEnPassant(pawn, true));
            moves.add(generateEnPassant(pawn, false));
        }

        return moves.build().filter(Objects::nonNull);
    }

    private Stream<Move> generatePushes(Pawn pawn) {
        return new DestinationFinder(board).calculateDestinations(pawn).flatMap(square -> createPushes(pawn, square));
    }

    private Stream<Move> createPushes(Pawn pawn, Square destination) {

        var destinationPiece = destination.piece();

        if (destinationPiece != null) {
            return Stream.empty();
        }

        var move = Move.builder(pawn, destination.coordinate()).normal();

        if (pawn.canBePromoted()) {
            return Arrays.stream(PromotionChoice.values()).map(move::makePromotion);
        }

        return Stream.of(move);
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

    private Stream<Move> generateCaptures(Pawn pawn, boolean leftSide) {

        var direction = leftSide ? -1 : 1;

        var destination = pawn.coordinate().to(direction, pawn.color().direction());

        if (destination == null) {
            return Stream.empty();
        }

        var destinationPiece = board.pieceAt(destination);

        if (destinationPiece == null || pawn.isAllyOf(destinationPiece)) {
            return Stream.empty();
        }

        var move = Move.builder(pawn, destination).capture(destinationPiece);

        if (pawn.canBePromoted()) {
            return Arrays.stream(PromotionChoice.values()).map(move::makePromotion);
        }

        return Stream.of(move);
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

        if (destination == null || !board.squareAt(destination).equals(enPassantTarget)) {
            return null;
        }

        var sidePiece = board.pieceAt(side);

        if (sidePiece == null || !sidePiece.isPawn()) {
            return null;
        }

        return Move.builder(pawn, destination).enPassant((Pawn) sidePiece);
    }
}
