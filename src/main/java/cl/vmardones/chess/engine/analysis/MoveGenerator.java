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
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.Piece;
import org.eclipse.jdt.annotation.Nullable;

final class MoveGenerator {

    private final Board board;
    private final List<Piece> pieces;

    MoveGenerator(Board board, List<Piece> pieces) {
        this.board = board;
        this.pieces = pieces;
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

        return Move.createNormal(piece, destination.position());
    }
}
