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
import org.eclipse.jdt.annotation.Nullable;

final class CaptureGenerator {

    private final Board board;
    private final List<Piece> pieces;

    CaptureGenerator(Position position) {
        board = position.board();
        pieces = board.pieces(position.sideToMove());
    }

    Stream<Move> calculateCaptures() {
        return pieces.stream().flatMap(this::calculatePieceCaptures);
    }

    private Stream<Move> calculatePieceCaptures(Piece piece) {

        if (piece.isPawn()) {
            var pawn = (Pawn) piece;

            return Stream.concat(generatePawnCaptures(pawn, true), generatePawnCaptures(pawn, false));
        }

        return piece.calculatePossibleDestinations(board).stream()
                .map(board::squareAt)
                .map(square -> generateCapture(piece, square))
                .filter(Objects::nonNull);
    }

    private @Nullable Move generateCapture(Piece piece, Square destination) {

        var destinationPiece = destination.piece();

        if (destinationPiece == null || piece.isAllyOf(destinationPiece)) {
            return null;
        }

        return Move.createCapture(piece, destination.coordinate(), destinationPiece);
    }

    private Stream<Move> generatePawnCaptures(Pawn pawn, boolean leftSide) {

        var direction = leftSide ? -1 : 1;

        var destination = pawn.coordinate().to(direction, pawn.color().direction());

        if (destination == null) {
            return Stream.empty();
        }

        var destinationPiece = board.pieceAt(destination);

        if (destinationPiece == null || pawn.isAllyOf(destinationPiece)) {
            return Stream.empty();
        }

        var move = Move.createCapture(pawn, destination, destinationPiece);

        if (pawn.coordinate().rank() == pawn.color().opposite().pawnRank()) {
            return Arrays.stream(PromotionChoice.values()).map(choice -> Move.makePromotion(move, choice));
        }

        return Stream.of(move);
    }
}
