/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.piece.PromotionChoice;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class PawnMoveGeneratorTest {


    @Test
    void whiteEnPassantMoves() {
        var position = FenParser.parse("4k3/8/8/PpP2P2/8/8/8/4K3 w - b6 0 1");
        var generator = new PawnMoveGenerator(position);

        // Only the left and right pawns can en passant
        var board = position.board();
        var leftPawn = (Pawn) board.pieceAt(Coordinate.of("a5"));
        var rightPawn = (Pawn) board.pieceAt(Coordinate.of("c5"));
        var otherPawn = (Pawn) board.pieceAt(Coordinate.of("f5"));

        var attackedPawn = (Pawn) board.pieceAt(Coordinate.of("b5"));

        var expectedMoves = new Move[] {
            Move.enPassant(leftPawn, Coordinate.of("b6"), attackedPawn),
            Move.enPassant(rightPawn, Coordinate.of("b6"), attackedPawn)
        };

        var unexpectedMoves = new Move[] {
            Move.enPassant(rightPawn, Coordinate.of("d6"), attackedPawn),
            Move.enPassant(otherPawn, Coordinate.of("e6"), attackedPawn),
            Move.enPassant(otherPawn, Coordinate.of("g6"), attackedPawn)
        };

        assertThat(generator.generate()).containsOnlyOnce(expectedMoves).doesNotContain(unexpectedMoves);
    }

    @Test
    void blackEnPassantMoves() {
        var position = FenParser.parse("4k3/8/8/8/pPp2p2/8/8/4K3 b - b3 0 1");
        var generator = new PawnMoveGenerator(position);

        // Only the left and right pawns can en passant
        var board = position.board();
        var leftPawn = (Pawn) board.pieceAt(Coordinate.of("a4"));
        var rightPawn = (Pawn) board.pieceAt(Coordinate.of("c4"));
        var otherPawn = (Pawn) board.pieceAt(Coordinate.of("f4"));

        var attackedPawn = (Pawn) board.pieceAt(Coordinate.of("b4"));

        var expectedMoves = new Move[] {
            Move.enPassant(leftPawn, Coordinate.of("b3"), attackedPawn),
            Move.enPassant(rightPawn, Coordinate.of("b3"), attackedPawn)
        };

        var unexpectedMoves = new Move[] {
            Move.enPassant(rightPawn, Coordinate.of("d3"), attackedPawn),
            Move.enPassant(otherPawn, Coordinate.of("e3"), attackedPawn),
            Move.enPassant(otherPawn, Coordinate.of("g3"), attackedPawn)
        };

        assertThat(generator.generate()).containsOnlyOnce(expectedMoves).doesNotContain(unexpectedMoves);
    }


}
