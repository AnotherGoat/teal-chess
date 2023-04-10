/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.move.MoveMaker;
import cl.vmardones.chess.engine.move.MoveType;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PawnTest {

    @Test
    void constructor() {
        assertThat(new Pawn("a1", Alliance.BLACK)).matches(Pawn::firstMove);
    }

    @Test
    void toSingleChar() {
        assertThat(new Pawn("a1", Alliance.WHITE).singleChar()).isEqualTo("P");
        assertThat(new Pawn("a1", Alliance.BLACK).singleChar()).isEqualTo("p");
    }

    @Test
    void whiteMoves() {
        var whitePawn = new Pawn("a1", Alliance.WHITE);
        assertThat(whitePawn.moveOffsets()).containsOnlyOnce(new int[] {-1, 1}, new int[] {0, 1}, new int[] {1, 1});
    }

    @Test
    void blackMoves() {
        var blackPawn = new Pawn("a1", Alliance.BLACK);
        assertThat(blackPawn.moveOffsets()).containsOnlyOnce(new int[] {-1, -1}, new int[] {0, -1}, new int[] {1, -1});
    }

    @Test
    void illegalMove() {
        var pawn = new Pawn("a1", Alliance.WHITE);
        assertThat(pawn.moveOffsets()).isNotEmpty().doesNotContain(new int[] {1, 0});
    }

    @Test
    void moveTo() {
        var pawnToMove = new Pawn("a1", Alliance.WHITE);

        assertThat(pawnToMove.moveTo("a2"))
                .isInstanceOf(Pawn.class)
                .matches(pawn -> pawn.position().equals(Position.of("a2")))
                .matches(pawn -> !pawn.firstMove());
    }

    @Test
    void createNormalMove() {
        var whiteKing = new King("e1", Alliance.WHITE);
        var blackKing = new King("e8", Alliance.BLACK);

        var pawn = new Pawn("a2", Alliance.WHITE);

        var board = Board.builder(whiteKing, blackKing).with(pawn).build();

        var destination = board.squareAt("a3");

        assertThat(pawn.createMove(destination, board).type()).isEqualTo(MoveType.PAWN_NORMAL);
    }

    @Test
    void createJumpMove() {
        var whiteKing = new King("e1", Alliance.WHITE);
        var blackKing = new King("e8", Alliance.BLACK);

        var pawn = new Pawn("a2", Alliance.WHITE);

        var board = Board.builder(whiteKing, blackKing).with(pawn).build();

        var destination = board.squareAt("a4");

        assertThat(pawn.createMove(destination, board).type()).isEqualTo(MoveType.PAWN_JUMP);
    }

    @Test
    void dontCreateJumpMove() {
        var whiteKing = new King("e1", Alliance.WHITE);
        var blackKing = new King("e8", Alliance.BLACK);

        var initialPawn = new Pawn("a2", Alliance.WHITE);
        var initialBoard = Board.builder(whiteKing, blackKing).with(initialPawn).build();
        var firstMove = initialPawn.createMove(initialBoard.squareAt("a3"), initialBoard);

        var newBoard = new MoveMaker().make(initialBoard, firstMove);
        var pawn = newBoard.pieceAt("a3");
        var destination = newBoard.squareAt("a5");

        assertThat(pawn.createMove(destination, newBoard).type()).isEqualTo(MoveType.PAWN_NORMAL);
    }

    @Test
    void createCaptureMove() {
        var whiteKing = new King("e1", Alliance.WHITE);
        var blackKing = new King("e8", Alliance.BLACK);

        var piece = new Pawn("a1", Alliance.WHITE);
        var capturablePiece = new Pawn("b2", Alliance.BLACK);

        var board = Board.builder(whiteKing, blackKing)
                .with(piece)
                .with(capturablePiece)
                .build();

        var destination = board.squareAt("b2");

        assertThat(piece.createMove(destination, board).type()).isEqualTo(MoveType.PAWN_CAPTURE);
    }

    @Test
    void createEnPassantMove() {
        var whiteKing = new King("e1", Alliance.WHITE);
        var blackKing = new King("e8", Alliance.BLACK);

        var pawn = new Pawn("a5", Alliance.WHITE);
        var enPassantPawn = new Pawn("b5", Alliance.BLACK);

        var board = Board.builder(whiteKing, blackKing)
                .with(pawn)
                .with(enPassantPawn)
                .enPassantPawn(enPassantPawn)
                .build();

        var destination = board.squareAt("b6");

        assertThat(pawn.createMove(destination, board).type()).isEqualTo(MoveType.EN_PASSANT);
    }

    @Test
    void noEnPassantPawn() {
        var whiteKing = new King("e1", Alliance.WHITE);
        var blackKing = new King("e8", Alliance.BLACK);

        var pawn = new Pawn("a5", Alliance.WHITE);
        var capturablePawn = new Pawn("b5", Alliance.BLACK);

        var board = Board.builder(whiteKing, blackKing)
                .with(pawn)
                .with(capturablePawn)
                .build();

        var destination = board.squareAt("b6");

        assertThat(pawn.createMove(destination, board)).isNull();
    }

    @Test
    void cantReachEnPassantPawn() {
        var whiteKing = new King("e1", Alliance.WHITE);
        var blackKing = new King("e8", Alliance.BLACK);

        var pawn = new Pawn("a5", Alliance.WHITE);
        var enPassantPawn = new Pawn("c5", Alliance.BLACK);

        var board = Board.builder(whiteKing, blackKing)
                .with(pawn)
                .with(enPassantPawn)
                .enPassantPawn(enPassantPawn)
                .build();

        var destination = board.squareAt("b6");

        assertThat(pawn.createMove(destination, board)).isNull();
    }
}
