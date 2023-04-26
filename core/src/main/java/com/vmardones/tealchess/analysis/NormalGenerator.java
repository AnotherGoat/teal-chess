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
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.PromotionChoice;

final class NormalGenerator extends MoveGenerator {

    private final Board board;
    private final List<Piece> pieces;

    NormalGenerator(Position position) {
        super(position);
        board = position.board();
        pieces = board.pieces(position.sideToMove());
    }

    @Override
    Stream<Move> generate() {
        return pieces.stream().flatMap(this::calculatePieceMoves);
    }

    private Stream<Move> calculatePieceMoves(Piece piece) {
        return new DestinationFinder(board).calculateDestinations(piece).flatMap(square -> createMoves(piece, square));
    }

    private Stream<Move> createMoves(Piece piece, Square destination) {

        var destinationPiece = destination.piece();

        if (destinationPiece != null) {
            return Stream.empty();
        }

        var move = Move.builder(piece, destination.coordinate()).normal();

        if (!piece.isPawn()) {
            return Stream.of(move);
        }

        var pawn = (Pawn) piece;

        if (pawn.canBePromoted()) {
            return Arrays.stream(PromotionChoice.values()).map(move::makePromotion);
        }

        return Stream.of(move);
    }
}
