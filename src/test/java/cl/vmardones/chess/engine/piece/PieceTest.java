/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.board.Square;
import cl.vmardones.chess.engine.move.MoveType;
import cl.vmardones.chess.engine.player.Alliance;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PieceTest {

    @Mock
    Square destinationSquare;

    @Test
    void isAllyOf() {
        var first = new Pawn("a1", Alliance.WHITE);
        var second = new Rook("a1", Alliance.WHITE);

        assertThat(first.isAllyOf(second)).isTrue();
        assertThat(first.isEnemyOf(second)).isFalse();
        assertThat(second.isAllyOf(first)).isTrue();
        assertThat(second.isEnemyOf(first)).isFalse();
    }

    @Test
    void isEnemyOf() {
        var first = new Bishop("a1", Alliance.WHITE);
        var second = new Bishop("a1", Alliance.BLACK);

        assertThat(first.isAllyOf(second)).isFalse();
        assertThat(first.isEnemyOf(second)).isTrue();
        assertThat(second.isAllyOf(first)).isFalse();
        assertThat(second.isEnemyOf(first)).isTrue();
    }

    @Test
    void isEmptyAccesible() {
        var piece = new Rook("a1", Alliance.BLACK);

        when(destinationSquare.piece()).thenReturn(null);

        assertThat(piece.canAccess(destinationSquare)).isTrue();
    }

    @Test
    void isEnemyAccessible() {
        var piece = new Bishop("a1", Alliance.BLACK);
        var destinationPiece = new Pawn("a1", Alliance.WHITE);

        when(destinationSquare.piece()).thenReturn(destinationPiece);

        assertThat(piece.canAccess(destinationSquare)).isTrue();
    }

    @Test
    void isNotAccesible() {
        var piece = new Queen("a1", Alliance.BLACK);
        var destinationPiece = new King("a1", Alliance.BLACK);

        when(destinationSquare.piece()).thenReturn(destinationPiece);

        assertThat(piece.canAccess(destinationSquare)).isFalse();
    }

    @Test
    void createNormalMove() {
        var whiteKing = new King("e1", Alliance.WHITE);
        var blackKing = new King("e8", Alliance.BLACK);

        var piece = new Rook("a1", Alliance.WHITE);

        var board = Board.builder(whiteKing, blackKing).with(piece).build();

        var destination = board.squareAt(Position.of("a7"));

        assertThat(piece.createMove(destination, board).type()).isEqualTo(MoveType.NORMAL);
    }

    @Test
    void createCaptureMove() {
        var whiteKing = new King("e1", Alliance.WHITE);
        var blackKing = new King("e8", Alliance.BLACK);

        var piece = new Rook("a1", Alliance.WHITE);
        var capturablePiece = new Pawn("a7", Alliance.BLACK);

        var board = Board.builder(whiteKing, blackKing)
                .with(piece)
                .with(capturablePiece)
                .build();

        var destination = board.squareAt(Position.of("a7"));

        assertThat(piece.createMove(destination, board).type()).isEqualTo(MoveType.CAPTURE);
    }

    @Test
    void dontCreateMove() {
        var whiteKing = new King("e1", Alliance.WHITE);
        var blackKing = new King("e8", Alliance.BLACK);

        var piece = new Rook("a1", Alliance.WHITE);
        var blockingPiece = new Pawn("a7", Alliance.WHITE);

        var board = Board.builder(whiteKing, blackKing)
                .with(piece)
                .with(blockingPiece)
                .build();

        var destination = board.squareAt(Position.of("a7"));

        assertThat(piece.createMove(destination, board)).isNull();
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Piece.class).withNonnullFields("position").verify();
    }
}
