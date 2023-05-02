/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Attack;
import com.vmardones.tealchess.piece.Piece;
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

    Stream<Attack> calculateAttacks(boolean opponentAttacks) {
        var attackingPieces = opponentAttacks ? opponentPieces : pieces;

        return attackingPieces.stream().flatMap(this::calculatePieceAttacks);
    }

    private Stream<Attack> calculatePieceAttacks(Piece piece) {

        if (piece.isPawn()) {
            return Stream.of(generatePawnAttack(piece, true), generatePawnAttack(piece, false))
                    .filter(Objects::nonNull);
        }

        return new DestinationFinder(board).calculateDestinations(piece).map(square -> new Attack(piece, square));
    }

    private @Nullable Attack generatePawnAttack(Piece pawn, boolean leftSide) {

        var direction = leftSide ? -1 : 1;

        var destination = pawn.coordinate().toOrNull(direction, pawn.color().direction());

        if (destination == null) {
            return null;
        }

        return new Attack(pawn, board.squareAt(destination));
    }
}
