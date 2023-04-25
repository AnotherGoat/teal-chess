/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.PromotionChoice;

final class MoveGenerator {

    private final Board board;
    private final List<Piece> pieces;

    MoveGenerator(Position position) {
        board = position.board();
        pieces = board.pieces(position.sideToMove());
    }

    Stream<Move> calculateMoves() {
        return pieces.stream().flatMap(this::calculatePieceMoves);
    }

    private Stream<Move> calculatePieceMoves(Piece piece) {
        return piece.calculatePossibleDestinations(board).stream()
                .map(board::squareAt)
                .flatMap(square -> createMoves(piece, square));
    }

    private Stream<Move> createMoves(Piece piece, Square destination) {

        var destinationPiece = destination.piece();

        if (destinationPiece != null) {
            return Stream.empty();
        }

        var move = Move.createNormal(piece, destination.coordinate());

        // TODO: In the frontend, check if a destination has more than 1 possible move, in that case it's a promotion
        if (piece.isPawn()
                && piece.coordinate().rank() == piece.color().opposite().pawnRank()) {
            return Arrays.stream(PromotionChoice.values()).map(choice -> Move.makePromotion(move, choice));
        }

        return Stream.of(move);
    }
}
