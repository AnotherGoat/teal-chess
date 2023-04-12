/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import java.util.Objects;
import java.util.stream.Stream;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Rook;
import org.eclipse.jdt.annotation.Nullable;

final class CastleGenerator {

    private final MoveTester moveTester;
    private final Board board;
    private final King king;
    private final boolean inCheck;

    CastleGenerator(MoveTester moveTester, Board board, King king) {
        this.moveTester = moveTester;
        this.board = board;
        this.king = king;
        this.inCheck = !moveTester.attacksOnKing().isEmpty();
    }

    Stream<Move> calculateCastles() {

        if (castlingIsImpossible()) {
            return Stream.empty();
        }

        return Stream.of(generateCastleMove(true), generateCastleMove(false)).filter(Objects::nonNull);
    }

    private boolean castlingIsImpossible() {
        return !king.firstMove() || inCheck || king.position().file() != 'e';
    }

    private @Nullable Move generateCastleMove(boolean kingSide) {

        if (kingSide && !isKingSideCastlePossible() || !kingSide && !isQueenSideCastlePossible()) {
            return null;
        }

        var kingPosition = king.position();
        var rookOffset = kingSide ? 3 : -4;
        var rookPosition = kingPosition.right(rookOffset);

        if (rookPosition == null) {
            return null;
        }

        var rook = (Rook) board.pieceAt(rookPosition);

        if (rook == null || !rook.firstMove()) {
            return null;
        }

        var direction = kingSide ? 1 : -1;
        var kingDestination = kingPosition.right(2 * direction);
        var rookDestination = kingPosition.right(direction);

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
        var destination = king.position().right(offset);

        return destination != null && board.isEmpty(destination);
    }

    private boolean squareHasRook(int offset) {
        var destination = king.position().right(offset);

        return destination != null && board.contains(destination.toString(), Rook.class);
    }

    private boolean isUnreachableByEnemy(int offset) {
        var destination = king.position().right(offset);

        return destination != null && moveTester.attacksOn(destination).isEmpty();
    }
}
