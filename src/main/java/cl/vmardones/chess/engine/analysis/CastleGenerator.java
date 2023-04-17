/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import java.util.Objects;
import java.util.stream.Stream;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.game.Position;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Rook;
import org.eclipse.jdt.annotation.Nullable;

final class CastleGenerator {

    private final Board board;
    private final King king;
    private final MoveTester moveTester;
    private final boolean inCheck;

    CastleGenerator(Position position, MoveTester moveTester) {
        board = position.board();
        king = position.board().king(position.sideToMove());
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
        return !king.firstMove() || inCheck || !king.coordinate().file().equals("e");
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

        if (rook == null || !rook.firstMove()) {
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
        return isSquareFree(1)
                && isSquareFree(2)
                && squareHasRook(3)
                && isUnreachableByEnemy(1)
                && isUnreachableByEnemy(2);
    }

    private boolean isQueenSideCastlePossible() {
        return isSquareFree(-1)
                && isSquareFree(-2)
                && isSquareFree(-3)
                && squareHasRook(-4)
                && isUnreachableByEnemy(-1)
                && isUnreachableByEnemy(-2)
                && isUnreachableByEnemy(-3);
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
