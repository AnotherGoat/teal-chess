/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.piece.Piece;
import org.eclipse.jdt.annotation.Nullable;

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
                .map(square -> createMove(piece, square))
                .filter(Objects::nonNull);
    }

    private @Nullable Move createMove(Piece piece, Square destination) {

        var destinationPiece = destination.piece();

        if (destinationPiece != null) {
            return null;
        }

        if (piece.isPawn()
                && piece.coordinate().rank() == piece.color().opposite().pawnRank()) {
            return Move.makePromotion(Move.createNormal(piece, destination.coordinate()));
        }

        return Move.createNormal(piece, destination.coordinate());
    }
}