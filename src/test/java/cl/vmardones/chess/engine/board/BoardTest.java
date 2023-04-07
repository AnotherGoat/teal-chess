/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.piece.*;
import cl.vmardones.chess.engine.player.Alliance;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardTest {

    Board.BoardBuilder builder;

    King whiteKing = new King("e1", Alliance.WHITE);
    King blackKing = new King("e8", Alliance.BLACK);

    @Mock
    Pawn enPassantPawn;

    @BeforeEach
    void setUp() {
        builder = Board.builder(whiteKing, blackKing);
    }

    @Test
    void contains() {
        var piece = new Bishop("e2", Alliance.WHITE);
        var board = builder.with(piece).build();

        assertThat(board.contains("e2", Bishop.class)).isTrue();
    }

    @Test
    void isEmpty() {
        var board = builder.build();

        assertThat(board.isEmpty(Coordinate.of("a1"))).isTrue();
        assertThat(board.isEmpty(Coordinate.of("h8"))).isTrue();
    }

    @Test
    void nextTurnBuilder() {
        var board = builder.build();
        var nextTurnBoard = board.nextTurnBuilder().build();

        assertThat(board.whiteKing()).isEqualTo(nextTurnBoard.whiteKing());
        assertThat(board.blackKing()).isEqualTo(nextTurnBoard.blackKing());
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Board.class)
                .withNonnullFields("squares", "whiteKing", "whitePieces", "blackKing", "blackPieces")
                .verify();
    }

    @Test
    void addPiece() {
        var piece = new Queen("d7", Alliance.WHITE);
        var board = builder.with(piece).build();

        assertThat(board.whitePieces()).containsOnlyOnce(piece);
    }

    @Test
    void addNullPiece() {
        var board = builder.build();
        var nextTurnBoard = board.nextTurnBuilder().with(null).build();

        assertThat(board).isEqualTo(nextTurnBoard);
    }

    @Test
    void lastPieceTakesPrecedence() {
        var firstPiece = new Pawn("a1", Alliance.WHITE);
        var secondPiece = new Rook("a1", Alliance.WHITE);
        var board = builder.with(firstPiece).with(secondPiece).build();

        assertThat(board.whitePieces()).doesNotContain(firstPiece).containsOnlyOnce(secondPiece);
    }

    @Test
    void withoutPiece() {
        var piece = new Pawn("a5", Alliance.BLACK);
        var board = builder.with(piece).without(piece).build();

        assertThat(board.blackPieces()).doesNotContain(piece);
    }

    @Test
    void withoutNullPiece() {
        var board = builder.build();
        var nextTurnBoard = board.nextTurnBuilder().without(null).build();

        assertThat(board).isEqualTo(nextTurnBoard);
    }

    @Test
    void enPassantPawn() {
        var board = builder.enPassantPawn(enPassantPawn).build();

        assertThat(board.enPassantPawn()).isEqualTo(enPassantPawn);
    }

    @Test
    void noEnPassantPawnNextTurn() {
        var board = builder.enPassantPawn(enPassantPawn).build();
        var nextTurnBoard = board.nextTurnBuilder().build();

        assertThat(board.enPassantPawn()).isEqualTo(enPassantPawn);
        assertThat(nextTurnBoard.enPassantPawn()).isNull();
    }

    @Test
    void alwaysAddsKings() {
        var newBlackKing = new King("a1", Alliance.BLACK);
        var impostorQueen = new Queen("a1", Alliance.BLACK);

        var board = Board.builder(whiteKing, newBlackKing).with(impostorQueen).build();

        assertThat(board.blackPieces()).doesNotContain(impostorQueen).containsOnlyOnce(newBlackKing);
    }
}
