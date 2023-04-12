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
import cl.vmardones.chess.engine.player.Alliance;
import org.eclipse.jdt.annotation.Nullable;

final class AttackGenerator {

    private final Board board;
    private final Alliance nextMoveMaker;
    private final List<Piece> pieces;
    private final List<Piece> opponentPieces;

    AttackGenerator(Board board, Alliance nextMoveMaker, List<Piece> pieces, List<Piece> opponentPieces) {
        this.board = board;
        this.nextMoveMaker = nextMoveMaker;
        this.pieces = pieces;
        this.opponentPieces = opponentPieces;
    }

    Stream<Move> calculateAttacks(Alliance alliance) {
        var attackingPieces = alliance == nextMoveMaker ? pieces : opponentPieces;

        return attackingPieces.stream().flatMap(piece -> calculatePieceAttacks(piece, board));
    }

    private Stream<Move> calculatePieceAttacks(Piece piece, Board board) {
        return piece.calculatePossibleDestinations(board).stream()
                .map(board::squareAt)
                .filter(piece::canAccess)
                .map(square -> createAttack(piece, square))
                .filter(Objects::nonNull);
    }

    private @Nullable Move createAttack(Piece piece, Square destination) {

        var destinationPiece = destination.piece();

        if (destinationPiece == null || piece.isAllyOf(destinationPiece)) {
            return null;
        }

        return Move.createCapture(piece, destination.position(), destinationPiece);
    }
}
