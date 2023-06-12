/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.board.BitboardManipulator.*;
import static com.vmardones.tealchess.move.Move.*;

import java.util.ArrayList;
import java.util.List;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.square.AlgebraicConverter;
import com.vmardones.tealchess.square.Square;

final class KingMoveGenerator implements MoveGenerator, LookupGenerator {

    private static final long FILE_A = 0x01_01_01_01_01_01_01_01L;
    private static final long FILE_H = 0x80_80_80_80_80_80_80_80L;
    private static final long KING_PATTERN = 0x07_05_07L;
    private static final int KING_PATTERN_CENTER = Square.b2;
    private static final int WHITE_KING = Square.e1;
    private static final int BLACK_KING = Square.e8;

    private final AttackGenerator attackGenerator;

    @Override
    public List<Move> generate(Position position) {
        var board = position.board();
        var sideToMove = position.sideToMove();
        var king = board.kings(sideToMove);

        var moves = new ArrayList<Move>();

        var emptySquares = board.emptySquares();
        addKingMoves(moves, MoveType.NORMAL, king, emptySquares);

        var capturablePieces = board.capturablePieces(sideToMove);
        addKingMoves(moves, MoveType.CAPTURE, king, capturablePieces);

        if (sideToMove.isWhite()) {
            addWhiteCastles(moves, king, position);
        } else {
            addBlackCastles(moves, king, position);
        }

        return moves;
    }

    KingMoveGenerator() {
        attackGenerator = new AttackGenerator();
    }

    private void addKingMoves(List<Move> moves, MoveType type, long king, long intersection) {

        var kingSquare = firstBit(king);
        var movesToAdd = shiftPattern(KING_PATTERN, KING_PATTERN_CENTER, kingSquare) & intersection;

        var fileIndex = AlgebraicConverter.fileIndex(kingSquare);
        if (fileIndex < 4) {
            movesToAdd = movesToAdd & ~FILE_H;
        } else {
            movesToAdd = movesToAdd & ~FILE_A;
        }

        addMoves(moves, type, movesToAdd, kingSquare);
    }

    private void addWhiteCastles(List<Move> moves, long king, Position position) {
        var rights = position.castlingRights();

        if (!rights.whiteShort() && !rights.whiteLong()) {
            return;
        }

        var opponentAttacks = attackGenerator.generate(position, Color.BLACK);

        if ((king & opponentAttacks) != 0) {
            return;
        }

        var emptySquares = position.board().emptySquares();

        if (rights.whiteShort() && isShortCastlePossible(WHITE_KING, emptySquares, opponentAttacks)) {
            moves.add(WHITE_SHORT_CASTLE_STEPS.get(0));
        }

        if (rights.whiteLong() && isLongCastlePossible(WHITE_KING, emptySquares, opponentAttacks)) {
            moves.add(WHITE_LONG_CASTLE_STEPS.get(0));
        }
    }

    private void addBlackCastles(List<Move> moves, long king, Position position) {
        var rights = position.castlingRights();

        if (!rights.blackShort() && !rights.blackLong()) {
            return;
        }

        var opponentAttacks = attackGenerator.generate(position, Color.WHITE);

        if ((king & opponentAttacks) != 0) {
            return;
        }

        var emptySquares = position.board().emptySquares();

        if (rights.blackShort() && isShortCastlePossible(BLACK_KING, emptySquares, opponentAttacks)) {
            moves.add(BLACK_SHORT_CASTLE_STEPS.get(0));
        }

        if (rights.blackLong() && isLongCastlePossible(BLACK_KING, emptySquares, opponentAttacks)) {
            moves.add(BLACK_LONG_CASTLE_STEPS.get(0));
        }
    }

    private boolean isShortCastlePossible(int kingSquare, long emptySquares, long opponentAttacks) {
        return isSet(emptySquares, kingSquare + 1)
                && isSet(emptySquares, kingSquare + 2)
                && isCleared(opponentAttacks, kingSquare + 1)
                && isCleared(opponentAttacks, kingSquare + 2);
    }

    private boolean isLongCastlePossible(int kingSquare, long emptySquares, long opponentAttacks) {
        return isSet(emptySquares, kingSquare - 1)
                && isSet(emptySquares, kingSquare - 2)
                && isSet(emptySquares, kingSquare - 3)
                && isCleared(opponentAttacks, kingSquare - 1)
                && isCleared(opponentAttacks, kingSquare - 2);
    }
}
