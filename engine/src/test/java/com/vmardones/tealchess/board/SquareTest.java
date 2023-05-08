/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.piece.Knight;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.piece.Rook;
import com.vmardones.tealchess.player.Color;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExcludeFromNullAway
@ExtendWith(MockitoExtension.class)
final class SquareTest {

    @Mock
    Coordinate coordinate;

    @Test
    void createOccupied() {
        var piece = new Knight(coordinate, Color.BLACK);
        assertThat(Square.create(coordinate, piece).piece()).isNotNull().isEqualTo(piece);
    }

    @Test
    void createEmpty() {
        assertThat(Square.create(Coordinate.of("a1"), null).piece()).isNull();
    }

    @Test
    void cache() {
        var location = Coordinate.of("d3");
        assertThat(Square.create(location, null)).isSameAs(Square.create(location, null));
    }

    @Test
    void getWhiteColor() {
        var firstWhite = Coordinate.of("a8");
        assertThat(Square.create(firstWhite, null).color()).isEqualTo(Color.WHITE);

        var lastWhite = Coordinate.of("h1");
        assertThat(Square.create(lastWhite, null).color()).isEqualTo(Color.WHITE);
    }

    @Test
    void getBlackColor() {
        var firstBlack = Coordinate.of("b8");
        assertThat(Square.create(firstBlack, null).color()).isEqualTo(Color.BLACK);

        var lastBlack = Coordinate.of("g1");
        assertThat(Square.create(lastBlack, null).color()).isEqualTo(Color.BLACK);
    }

    @Test
    void sameColor() {
        var first = Coordinate.of("a1");
        var second = Coordinate.of("b2");

        assertThat(Square.create(first, null).sameColorAs(Square.create(second, null)))
                .isTrue();
    }

    @Test
    void differentColor() {
        var first = Coordinate.of("a1");
        var second = Coordinate.of("b1");

        assertThat(Square.create(first, null).sameColorAs(Square.create(second, null)))
                .isFalse();
    }

    @Test
    void whitePieceUnicode() {
        var piece = new Pawn(coordinate, Color.WHITE);
        assertThat(Square.create(coordinate, piece).unicode()).isEqualTo("♙");
    }

    @Test
    void blackPieceUnicode() {
        var piece = new Rook(coordinate, Color.BLACK);
        assertThat(Square.create(coordinate, piece).unicode()).isEqualTo("♜");
    }

    @Test
    void emptyUnicode() {
        var white = Coordinate.of("a8");
        assertThat(Square.create(white, null).unicode()).isEqualTo("□");

        var black = Coordinate.of("b8");
        assertThat(Square.create(black, null).unicode()).isEqualTo("■");
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Square.class)
                .withNonnullFields("coordinate", "color")
                .verify();
    }
}
