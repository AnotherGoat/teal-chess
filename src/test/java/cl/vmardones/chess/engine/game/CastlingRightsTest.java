/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.player.Color;
import org.junit.jupiter.api.Test;

class CastlingRightsTest {

    @Test
    void emptyConstructor() {
        var rights = new CastlingRights();

        assertThat(rights.whiteKingSide()).isFalse();
        assertThat(rights.whiteQueenSide()).isFalse();
        assertThat(rights.blackKingSide()).isFalse();
        assertThat(rights.blackQueenSide()).isFalse();
    }

    @Test
    void disableWhite() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights.disable(Color.WHITE)).isEqualTo(new CastlingRights(false, false, true, true));
    }

    @Test
    void disableBlack() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights.disable(Color.BLACK)).isEqualTo(new CastlingRights(true, true, false, false));
    }

    @Test
    void disableWhiteKingSide() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights.disableKingSide(Color.WHITE)).isEqualTo(new CastlingRights(false, true, true, true));
    }

    @Test
    void disableBlackKingSide() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights.disableKingSide(Color.BLACK)).isEqualTo(new CastlingRights(true, true, false, true));
    }

    @Test
    void disableWhiteQueenSide() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights.disableQueenSide(Color.WHITE)).isEqualTo(new CastlingRights(true, false, true, true));
    }

    @Test
    void disableBlackQueenSide() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights.disableQueenSide(Color.BLACK)).isEqualTo(new CastlingRights(true, true, true, false));
    }

    @Test
    void noRightsToString() {
        var rights = new CastlingRights();
        assertThat(rights).hasToString("-");
    }

    @Test
    void allRightsToString() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights).hasToString("KQkq");
    }

    @Test
    void someRightsToString() {
        var rights = new CastlingRights(false, true, true, false);
        assertThat(rights).hasToString("Qk");
    }
}
