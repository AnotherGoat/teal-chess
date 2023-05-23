/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.position;

import static com.vmardones.tealchess.color.Color.BLACK;
import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.color.Color;
import org.junit.jupiter.api.Test;

final class CastlingRightsTest {

    @Test
    void emptyConstructor() {
        var rights = new CastlingRights();

        assertThat(rights.whiteShort()).isFalse();
        assertThat(rights.whiteLong()).isFalse();
        assertThat(rights.blackShort()).isFalse();
        assertThat(rights.blackLong()).isFalse();
    }

    @Test
    void disableWhite() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights.disable(Color.WHITE)).isEqualTo(new CastlingRights(false, false, true, true));
    }

    @Test
    void disableBlack() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights.disable(BLACK)).isEqualTo(new CastlingRights(true, true, false, false));
    }

    @Test
    void disableWhiteShortCastle() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights.disableShortCastle(Color.WHITE)).isEqualTo(new CastlingRights(false, true, true, true));
    }

    @Test
    void disableBlackShortCastle() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights.disableShortCastle(BLACK)).isEqualTo(new CastlingRights(true, true, false, true));
    }

    @Test
    void disableWhiteLongCastle() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights.disableLongCastle(Color.WHITE)).isEqualTo(new CastlingRights(true, false, true, true));
    }

    @Test
    void disableBlackLongCastle() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights.disableLongCastle(BLACK)).isEqualTo(new CastlingRights(true, true, true, false));
    }

    @Test
    void noRightsFen() {
        var rights = new CastlingRights();
        assertThat(rights.fen()).isEqualTo("-");
    }

    @Test
    void allRightsFen() {
        var rights = new CastlingRights(true, true, true, true);
        assertThat(rights.fen()).isEqualTo("KQkq");
    }

    @Test
    void someRightsFen() {
        var rights1 = new CastlingRights(false, true, true, true);
        assertThat(rights1.fen()).isEqualTo("Qkq");

        var rights2 = new CastlingRights(false, false, true, true);
        assertThat(rights2.fen()).isEqualTo("kq");

        var rights3 = new CastlingRights(false, false, false, true);
        assertThat(rights3.fen()).isEqualTo("q");

        var rights4 = new CastlingRights(true, true, true, false);
        assertThat(rights4.fen()).isEqualTo("KQk");
    }
}
