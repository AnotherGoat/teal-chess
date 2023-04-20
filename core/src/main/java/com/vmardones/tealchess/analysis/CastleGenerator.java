/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import java.util.Objects;
import java.util.stream.Stream;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.game.CastlingRights;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.piece.King;
import com.vmardones.tealchess.piece.Rook;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;

final class CastleGenerator {

    private final Board board;
    private final Color sideToMove;
    private final King king;
    private final CastlingRights castlingRights;
    private final MoveTester moveTester;
    private final boolean inCheck;

    CastleGenerator(Position position, MoveTester moveTester) {
        board = position.board();
        sideToMove = position.sideToMove();
        king = position.board().king(sideToMove);
        castlingRights = position.castlingRights();
        this.moveTester = moveTester;
        inCheck = moveTester.isKingAttacked();
    }

    Stream<Move> calculateCastles() {
        if (castlingIsImpossible()) {
            return Stream.empty();
        }

        return Stream.of(generateCastleMove(true), generateCastleMove(false)).filter(Objects::nonNull);
    }

    private boolean castlingIsImpossible() {
        if (inCheck) {
            return false;
        }

        if (sideToMove == Color.WHITE) {
            return !castlingRights.whiteKingSide() && !castlingRights.whiteQueenSide();
        }

        return !castlingRights.blackKingSide() && !castlingRights.blackQueenSide();
    }

    private @Nullable Move generateCastleMove(boolean kingSide) {

        if (kingSide && !isKingSideCastlePossible() || !kingSide && !isQueenSideCastlePossible()) {
            return null;
        }

        var kingCoordinate = king.coordinate();
        var rookOffset = kingSide ? 3 : -4;
        var rookCoordinate = kingCoordinate.right(rookOffset);

        if (rookCoordinate == null) {
            return null;
        }

        var rook = (Rook) board.pieceAt(rookCoordinate);

        if (rook == null) {
            return null;
        }

        var direction = kingSide ? 1 : -1;
        var kingDestination = kingCoordinate.right(2 * direction);
        var rookDestination = kingCoordinate.right(direction);

        if (kingDestination == null || rookDestination == null) {
            return null;
        }

        return Move.createCastle(kingSide, king, kingDestination, rook, rookDestination);
    }

    private boolean isKingSideCastlePossible() {
        var hasRights = sideToMove == Color.WHITE ? castlingRights.whiteKingSide() : castlingRights.blackKingSide();

        return hasRights
                && isSquareFree(1)
                && isSquareFree(2)
                && squareHasRook(3)
                && isUnreachableByEnemy(1)
                && isUnreachableByEnemy(2);
    }

    private boolean isQueenSideCastlePossible() {
        var hasRights = sideToMove == Color.WHITE ? castlingRights.whiteQueenSide() : castlingRights.blackQueenSide();

        return hasRights
                && isSquareFree(-1)
                && isSquareFree(-2)
                && isSquareFree(-3)
                && squareHasRook(-4)
                && isUnreachableByEnemy(-1)
                && isUnreachableByEnemy(-2);
    }

    private boolean isSquareFree(int offset) {
        var destination = king.coordinate().right(offset);

        return destination != null && board.isEmpty(destination);
    }

    private boolean squareHasRook(int offset) {
        var destination = king.coordinate().right(offset);

        return destination != null && board.contains(destination.toString(), Rook.class);
    }

    private boolean isUnreachableByEnemy(int offset) {
        var destination = king.coordinate().right(offset);

        return destination != null && !moveTester.isAttacked(destination);
    }
}
