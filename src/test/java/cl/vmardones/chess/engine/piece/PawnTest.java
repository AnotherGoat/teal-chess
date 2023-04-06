/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.move.MoveType;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PawnTest {

    @Mock
    Coordinate anywhere;

    @Mock
    Coordinate destination;

    @Test
    void constructor() {
        assertThat(new Pawn(anywhere, Alliance.BLACK)).matches(Pawn::firstMove);
    }

    @Test
    void toSingleChar() {
        assertThat(new Pawn(anywhere, Alliance.WHITE).singleChar()).isEqualTo("P");
        assertThat(new Pawn(anywhere, Alliance.BLACK).singleChar()).isEqualTo("p");
    }

    @Test
    void whiteMoves() {
        var whitePawn = new Pawn(anywhere, Alliance.WHITE);
        assertThat(whitePawn.moveOffsets()).containsOnlyOnce(new int[] {-1, 1}, new int[] {0, 1}, new int[] {1, 1});
    }

    @Test
    void blackMoves() {
        var blackPawn = new Pawn(anywhere, Alliance.BLACK);
        assertThat(blackPawn.moveOffsets()).containsOnlyOnce(new int[] {-1, -1}, new int[] {0, -1}, new int[] {1, -1});
    }

    @Test
    void illegalMove() {
        var pawn = new Pawn(anywhere, Alliance.WHITE);
        assertThat(pawn.moveOffsets()).doesNotContain(new int[] {1, 0});
    }

    @Test
    void moveTo() {
        var pawnToMove = new Pawn(anywhere, Alliance.WHITE);

        assertThat(pawnToMove.moveTo(destination))
                .isInstanceOf(Pawn.class)
                .matches(pawn -> pawn.position().equals(destination))
                .matches(pawn -> !pawn.firstMove());
    }

    @Test
    void createNormalMove() {
        var whiteKing = new King(Coordinate.of("e1"), Alliance.WHITE);
        var blackKing = new King(Coordinate.of("e8"), Alliance.BLACK);

        var pawn = new Pawn(Coordinate.of("a2"), Alliance.WHITE);

        var board = Board.builder(whiteKing, blackKing).with(pawn).build();

        var destination = board.squareAt(Coordinate.of("a3"));

        assertThat(pawn.createMove(destination, board).type()).isEqualTo(MoveType.PAWN_NORMAL);
    }

    @Test
    void createJumpMove() {
        var whiteKing = new King(Coordinate.of("e1"), Alliance.WHITE);
        var blackKing = new King(Coordinate.of("e8"), Alliance.BLACK);

        var pawn = new Pawn(Coordinate.of("a2"), Alliance.WHITE);

        var board = Board.builder(whiteKing, blackKing).with(pawn).build();

        var destination = board.squareAt(Coordinate.of("a4"));

        assertThat(pawn.createMove(destination, board).type()).isEqualTo(MoveType.PAWN_JUMP);
    }

    @Test
    void dontCreateJumpMove() {
        var whiteKing = new King(Coordinate.of("e1"), Alliance.WHITE);
        var blackKing = new King(Coordinate.of("e8"), Alliance.BLACK);

        var initialPawn = new Pawn(Coordinate.of("a2"), Alliance.WHITE);
        var initialBoard = Board.builder(whiteKing, blackKing).with(initialPawn).build();
        var firstMove = initialPawn.createMove(initialBoard.squareAt(Coordinate.of("a3")), initialBoard);

        var newBoard = firstMove.execute();
        var pawn = newBoard.squareAt(Coordinate.of("a3")).piece();
        var destination = newBoard.squareAt(Coordinate.of("a5"));

        assertThat(pawn.createMove(destination, newBoard).type()).isEqualTo(MoveType.PAWN_NORMAL);
    }

    @Test
    void createCaptureMove() {
        var whiteKing = new King(Coordinate.of("e1"), Alliance.WHITE);
        var blackKing = new King(Coordinate.of("e8"), Alliance.BLACK);

        var piece = new Pawn(Coordinate.of("a1"), Alliance.WHITE);
        var capturablePiece = new Pawn(Coordinate.of("b2"), Alliance.BLACK);

        var board = Board.builder(whiteKing, blackKing)
                .with(piece)
                .with(capturablePiece)
                .build();

        var destination = board.squareAt(Coordinate.of("b2"));

        assertThat(piece.createMove(destination, board).type()).isEqualTo(MoveType.PAWN_CAPTURE);
    }

    @Test
    void createEnPassantMove() {
        var whiteKing = new King(Coordinate.of("e1"), Alliance.WHITE);
        var blackKing = new King(Coordinate.of("e8"), Alliance.BLACK);

        var pawn = new Pawn(Coordinate.of("a5"), Alliance.WHITE);
        var capturablePiece = new Pawn(Coordinate.of("b7"), Alliance.BLACK);
        var initialBoard = Board.builder(whiteKing, blackKing)
                .with(pawn)
                .with(capturablePiece)
                .build();

        var jumpMove = capturablePiece.createMove(initialBoard.squareAt(Coordinate.of("b5")), initialBoard);
        var newBoard = jumpMove.execute();
        var destination = newBoard.squareAt(Coordinate.of("b6"));

        assertThat(pawn.createMove(destination, newBoard).type()).isEqualTo(MoveType.EN_PASSANT);
    }
}
