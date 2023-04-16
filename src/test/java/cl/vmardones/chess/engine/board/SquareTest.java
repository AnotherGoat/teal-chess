/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.piece.Knight;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Rook;
import cl.vmardones.chess.engine.player.Color;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class SquareTest {

    @Test
    void createOccupied() {
        var piece = new Knight("a1", Color.BLACK);
        assertThat(Square.create("a1", piece).piece()).isNotNull().isEqualTo(piece);
    }

    @Test
    void createEmpty() {
        assertThat(Square.create("a1", null).piece()).isNull();
    }

    @Test
    void cache() {
        assertThat(Square.create("g5", null)).isSameAs(Square.create("g5", null));
    }

    @Test
    void getWhiteColor() {
        assertThat(Square.create("a8", null).color()).isEqualTo(Color.WHITE);
    }

    @Test
    void getBlackColor() {
        assertThat(Square.create("b8", null).color()).isEqualTo(Color.BLACK);
    }

    @Test
    void sameColor() {
        assertThat(Square.create("a8", null).sameColorAs(Square.create("h1", null)))
                .isTrue();
    }

    @Test
    void differentColor() {
        assertThat(Square.create("a8", null).sameColorAs(Square.create("h8", null)))
                .isFalse();
    }

    @Test
    void whitePieceToString() {
        var piece = new Pawn("a1", Color.WHITE);
        assertThat(Square.create("a1", piece)).hasToString("♙");
    }

    @Test
    void blackPieceToString() {
        var piece = new Rook("a1", Color.BLACK);
        assertThat(Square.create("a1", piece)).hasToString("♜");
    }

    @Test
    void emptyToString() {
        assertThat(Square.create("h1", null)).hasToString("□");
        assertThat(Square.create("g1", null)).hasToString("■");
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Square.class)
                .withNonnullFields("coordinate", "color")
                .verify();
    }
}
