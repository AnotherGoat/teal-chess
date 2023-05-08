/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.piece.*;
import com.vmardones.tealchess.player.Color;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class BoardTest {

    Board.BoardBuilder builder;
    King whiteKing = new King(Coordinate.of("e1"), Color.WHITE);
    King blackKing = new King(Coordinate.of("e8"), Color.BLACK);

    @BeforeEach
    void setUp() {
        builder = Board.builder(whiteKing, blackKing);
    }

    @Test
    void isEmpty() {
        var board = builder.build();

        assertThat(board.isEmpty(Coordinate.of("a1"))).isTrue();
        assertThat(board.isEmpty(Coordinate.of("h8"))).isTrue();
    }

    @Test
    void nextPositionBuilder() {
        var board = builder.build();
        var nextPositionBoard = board.nextPositionBuilder().build();

        assertThat(board.king(Color.WHITE)).isEqualTo(nextPositionBoard.king(Color.WHITE));
        assertThat(board.king(Color.BLACK)).isEqualTo(nextPositionBoard.king(Color.BLACK));
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Board.class)
                .withNonnullFields("squares", "whiteKing", "whitePieces", "blackKing", "blackPieces")
                .verify();
    }

    @Test
    void addPiece() {
        var piece = new Queen(Coordinate.of("d7"), Color.WHITE);
        var board = builder.with(piece).build();

        assertThat(board.pieces(Color.WHITE)).containsOnlyOnce(piece);
    }

    @Test
    void addNullPiece() {
        var board = builder.build();
        var nextPositionBoard = board.nextPositionBuilder().with(null).build();

        assertThat(board).isEqualTo(nextPositionBoard);
    }

    @Test
    void lastPieceTakesPrecedence() {
        var firstPiece = new Pawn(Coordinate.of("a1"), Color.WHITE);
        var secondPiece = new Rook(Coordinate.of("a1"), Color.WHITE);
        var board = builder.with(firstPiece).with(secondPiece).build();

        assertThat(board.pieces(Color.WHITE)).doesNotContain(firstPiece).containsOnlyOnce(secondPiece);
    }

    @Test
    void withoutPiece() {
        var piece = new Pawn(Coordinate.of("a5"), Color.BLACK);
        var board = builder.with(piece).without(piece).build();

        assertThat(board.pieces(Color.BLACK)).isNotEmpty().doesNotContain(piece);
    }

    @Test
    void withoutNullPiece() {
        var board = builder.build();
        var nextPositionBoard = board.nextPositionBuilder().without(null).build();

        assertThat(board).isEqualTo(nextPositionBoard);
    }

    @Test
    void alwaysAddsWhiteKing() {
        var newWhiteKing = new King(Coordinate.of("h8"), Color.WHITE);
        var impostorQueen = new Queen(Coordinate.of("h8"), Color.WHITE);

        var board = Board.builder(newWhiteKing, blackKing).with(impostorQueen).build();

        assertThat(board.pieces(Color.WHITE)).doesNotContain(impostorQueen).containsOnlyOnce(newWhiteKing);
    }

    @Test
    void alwaysAddsBlackKing() {
        var newBlackKing = new King(Coordinate.of("a1"), Color.BLACK);
        var impostorQueen = new Queen(Coordinate.of("a1"), Color.BLACK);

        var board = Board.builder(whiteKing, newBlackKing).with(impostorQueen).build();

        assertThat(board.pieces(Color.BLACK)).doesNotContain(impostorQueen).containsOnlyOnce(newBlackKing);
    }

    @Test
    void unmodifiableSquares() {
        var squares = FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - 0 1").board().squares();

        assertThatThrownBy(squares::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void unmodifiableWhitePieces() {
        var whitePieces =
                FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - 0 1").board().pieces(Color.WHITE);

        assertThatThrownBy(whitePieces::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void unmodifiableBlackPieces() {
        var blackPieces =
                FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - 0 1").board().pieces(Color.BLACK);

        assertThatThrownBy(blackPieces::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void unicode() {
        var board = Position.INITIAL_POSITION.board();

        assertThat(board.unicode())
                .containsOnlyOnce("♜ ♞ ♝ ♛ ♚ ♝ ♞ ♜")
                .containsOnlyOnce("♟ ♟ ♟ ♟ ♟ ♟ ♟ ♟")
                .contains("□ ■ □ ■ □ ■ □ ■")
                .contains("■ □ ■ □ ■ □ ■ □")
                .containsOnlyOnce("♙ ♙ ♙ ♙ ♙ ♙ ♙ ♙")
                .containsOnlyOnce("♖ ♘ ♗ ♕ ♔ ♗ ♘ ♖")
                .contains("\n");
    }
}
