/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.piece.Piece;
import org.eclipse.jdt.annotation.Nullable;

final class CaptureGenerator extends MoveGenerator {

    private final Board board;
    private final Set<Piece> pieces;
    private final DestinationFinder destinationFinder;

    CaptureGenerator(Position position) {
        super(position);
        board = position.board();
        pieces = board.pieces(position.sideToMove());
        destinationFinder = new DestinationFinder(board);
    }

    @Override
    Stream<Move> generate() {
        return pieces.stream().flatMap(this::calculatePieceCaptures).filter(Objects::nonNull);
    }

    private Stream<Move> calculatePieceCaptures(Piece piece) {
        if (piece.isPawn()) {
            return Stream.empty();
        }

        return destinationFinder.calculateDestinations(piece).map(square -> generateCapture(piece, square));
    }

    private @Nullable Move generateCapture(Piece piece, Coordinate destination) {

        var destinationPiece = board.pieceAt(destination);

        if (destinationPiece == null || piece.isAllyOf(destinationPiece)) {
            return null;
        }

        return Move.builder(piece, destination).capture(destinationPiece);
    }
}
