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

final class NormalGenerator extends MoveGenerator {

    private final Board board;
    private final List<Piece> pieces;
    private final DestinationFinder destinationFinder;

    NormalGenerator(Position position) {
        super(position);
        board = position.board();
        pieces = board.pieces(position.sideToMove());
        destinationFinder = new DestinationFinder(board);
    }

    @Override
    Stream<Move> generate() {
        return pieces.stream().flatMap(this::calculatePieceMoves).filter(Objects::nonNull);
    }

    private Stream<Move> calculatePieceMoves(Piece piece) {
        if (piece.isPawn()) {
            return Stream.empty();
        }

        return destinationFinder.calculateDestinations(piece).map(square -> generateNormalMove(piece, square));
    }

    private @Nullable Move generateNormalMove(Piece piece, Square destination) {

        var destinationPiece = destination.piece();

        if (destinationPiece != null) {
            return null;
        }

        return Move.builder(piece, destination.coordinate()).normal();
    }
}
