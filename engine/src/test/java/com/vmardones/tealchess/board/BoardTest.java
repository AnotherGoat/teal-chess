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
    @Test
    void unmodifiablePieces() {
        var pieces = FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - 0 1").board().mailbox();

        assertThatThrownBy(pieces::clear).isInstanceOf(UnsupportedOperationException.class);
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


}
