/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cl.vmardones.chess.engine.piece.*;
import cl.vmardones.chess.engine.player.Color;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardTest {

    Board.BoardBuilder builder;

    King whiteKing = new King("e1", Color.WHITE);
    King blackKing = new King("e8", Color.BLACK);

    @Mock
    Pawn enPassantPawn;

    @BeforeEach
    void setUp() {
        builder = Board.builder(whiteKing, blackKing);
    }

    @Test
    void contains() {
        var piece = new Bishop("e2", Color.WHITE);
        var board = builder.with(piece).build();

        assertThat(board.contains("e2", Bishop.class)).isTrue();
    }

    @Test
    void isEmpty() {
        var board = builder.build();

        assertThat(board.isEmpty(Position.of("a1"))).isTrue();
        assertThat(board.isEmpty(Position.of("h8"))).isTrue();
    }

    @Test
    void nextTurnBuilder() {
        var board = builder.build();
        var nextTurnBoard = board.nextTurnBuilder().build();

        assertThat(board.king(Color.WHITE)).isEqualTo(nextTurnBoard.king(Color.WHITE));
        assertThat(board.king(Color.BLACK)).isEqualTo(nextTurnBoard.king(Color.BLACK));
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Board.class)
                .withNonnullFields("squares", "whiteKing", "whitePieces", "blackKing", "blackPieces")
                .verify();
    }

    @Test
    void addPiece() {
        var piece = new Queen("d7", Color.WHITE);
        var board = builder.with(piece).build();

        assertThat(board.pieces(Color.WHITE)).containsOnlyOnce(piece);
    }

    @Test
    void addNullPiece() {
        var board = builder.build();
        var nextTurnBoard = board.nextTurnBuilder().with(null).build();

        assertThat(board).isEqualTo(nextTurnBoard);
    }

    @Test
    void lastPieceTakesPrecedence() {
        var firstPiece = new Pawn("a1", Color.WHITE);
        var secondPiece = new Rook("a1", Color.WHITE);
        var board = builder.with(firstPiece).with(secondPiece).build();

        assertThat(board.pieces(Color.WHITE)).doesNotContain(firstPiece).containsOnlyOnce(secondPiece);
    }

    @Test
    void withoutPiece() {
        var piece = new Pawn("a5", Color.BLACK);
        var board = builder.with(piece).without(piece).build();

        assertThat(board.pieces(Color.BLACK)).isNotEmpty().doesNotContain(piece);
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
    void alwaysAddsWhiteKing() {
        var newWhiteKing = new King("h8", Color.WHITE);
        var impostorQueen = new Queen("h8", Color.WHITE);

        var board = Board.builder(newWhiteKing, blackKing).with(impostorQueen).build();

        assertThat(board.pieces(Color.WHITE)).doesNotContain(impostorQueen).containsOnlyOnce(newWhiteKing);
    }

    @Test
    void alwaysAddsBlackKing() {
        var newBlackKing = new King("a1", Color.BLACK);
        var impostorQueen = new Queen("a1", Color.BLACK);

        var board = Board.builder(whiteKing, newBlackKing).with(impostorQueen).build();

        assertThat(board.pieces(Color.BLACK)).doesNotContain(impostorQueen).containsOnlyOnce(newBlackKing);
    }

    @Test
    void unmodifiableSquares() {
        var squares = BoardDirector.createStandardBoard().squares();

        assertThatThrownBy(squares::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void unmodifiableWhitePieces() {
        var whitePieces = BoardDirector.createStandardBoard().pieces(Color.WHITE);

        assertThatThrownBy(whitePieces::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void unmodifiableBlackPieces() {
        var blackPieces = BoardDirector.createStandardBoard().pieces(Color.BLACK);

        assertThatThrownBy(blackPieces::clear).isInstanceOf(UnsupportedOperationException.class);
    }
}
