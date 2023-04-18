/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Square;
import cl.vmardones.chess.engine.game.Position;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Piece;
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
            return Stream.of(generatePawnCapture(pawn, true), generatePawnCapture(pawn, false))
                    .filter(Objects::nonNull);
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

    private @Nullable Move generatePawnCapture(Pawn pawn, boolean leftSide) {

        var direction = leftSide ? -1 : 1;

        var destination = pawn.coordinate().to(direction, pawn.color().direction());

        if (destination == null) {
            return null;
        }

        var destinationPiece = board.pieceAt(destination);

        if (destinationPiece == null || pawn.isAllyOf(destinationPiece)) {
            return null;
        }

        if (pawn.coordinate().rank() == pawn.color().opposite().pawnRank()) {
            return Move.makePromotion(Move.createCapture(pawn, destination, destinationPiece));
        }

        return Move.createCapture(pawn, destination, destinationPiece);
    }
}
