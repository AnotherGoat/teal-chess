/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.game.Position;
import cl.vmardones.chess.engine.move.Attack;
import cl.vmardones.chess.engine.piece.Piece;
import org.eclipse.jdt.annotation.Nullable;

final class AttackGenerator {

    private final Board board;
    private final List<Piece> pieces;
    private final List<Piece> opponentPieces;

    AttackGenerator(Position position) {
        board = position.board();
        pieces = board.pieces(position.sideToMove());
        opponentPieces = board.pieces(position.sideToMove().opposite());
    }

    Stream<Attack> calculateAttacks(boolean opponent) {
        var attackingPieces = opponent ? opponentPieces : pieces;

        return attackingPieces.stream().flatMap(this::calculatePieceAttacks);
    }

    private Stream<Attack> calculatePieceAttacks(Piece piece) {

        if (piece.isPawn()) {
            return Stream.of(generatePawnAttack(piece, true), generatePawnAttack(piece, false))
                    .filter(Objects::nonNull);
        }

        return piece.calculatePossibleDestinations(board).stream()
                .map(board::squareAt)
                .map(square -> new Attack(piece, square));
    }

    private @Nullable Attack generatePawnAttack(Piece pawn, boolean leftSide) {

        var direction = leftSide ? -1 : 1;

        var destination = pawn.coordinate().to(direction, pawn.color().direction());

        if (destination == null) {
            return null;
        }

        return new Attack(pawn, board.squareAt(destination));
    }
}
