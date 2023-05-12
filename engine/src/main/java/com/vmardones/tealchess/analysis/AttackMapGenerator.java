/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static java.util.stream.Collectors.toSet;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.piece.Piece;
import org.eclipse.jdt.annotation.Nullable;

public final class AttackMapGenerator {

    private final Board board;
    private final Set<Piece> pieces;
    private final Set<Piece> opponentPieces;

    public AttackMapGenerator(Position position) {
        board = position.board();
        pieces = board.pieces(position.sideToMove());
        opponentPieces = board.pieces(position.sideToMove().opposite());
    }

    /**
     * Generate a set of all coordinates attacked by one of the sides.
     * @param opponentAttacks Whether the attacking pieces will be the current player's or the opponent's.
     * @return All attacked coordinates in this position.
     */
    public Set<Coordinate> generate(boolean opponentAttacks) {
        var attackingPieces = opponentAttacks ? opponentPieces : pieces;
        return attackingPieces.stream().flatMap(this::calculatePieceAttacks).collect(toSet());
    }

    private Stream<Coordinate> calculatePieceAttacks(Piece piece) {

        if (piece.isPawn()) {
            return Stream.of(generatePawnAttack(piece, true), generatePawnAttack(piece, false))
                    .filter(Objects::nonNull);
        }

        return new DestinationFinder(board).calculateDestinations(piece);
    }

    private @Nullable Coordinate generatePawnAttack(Piece pawn, boolean leftSide) {
        var direction = leftSide ? -1 : 1;
        return pawn.coordinate().toOrNull(direction, pawn.color().direction());
    }
}
