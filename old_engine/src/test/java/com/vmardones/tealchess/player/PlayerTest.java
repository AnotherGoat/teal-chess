/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashSet;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.piece.King;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class PlayerTest {

    @Test
    void checkmatedToString() {
        var player = new Player(Color.WHITE, mock(King.class), emptySet(), emptyList(), PlayerStatus.CHECKMATED);
        assertThat(player).hasToString("White player, in checkmate!");
    }

    @Test
    void stalematedToString() {
        var player = new Player(Color.BLACK, mock(King.class), emptySet(), emptyList(), PlayerStatus.STALEMATED);
        assertThat(player).hasToString("Black player, in stalemate!");
    }

    @Test
    void checkedToString() {
        var player = new Player(Color.WHITE, mock(King.class), emptySet(), emptyList(), PlayerStatus.CHECKED);
        assertThat(player).hasToString("White player, in check!");
    }

    @Test
    void normalToString() {
        var player = new Player(Color.BLACK, mock(King.class), emptySet(), emptyList(), PlayerStatus.NORMAL);
        assertThat(player).hasToString("Black player");
    }

    @Test
    void unmodifiablePieces() {
        var player = new Player(Color.WHITE, mock(King.class), new HashSet<>(), emptyList(), PlayerStatus.NORMAL);
        var pieces = player.pieces();

        assertThatThrownBy(pieces::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void unmodifiablelegals() {
        var player = new Player(Color.WHITE, mock(King.class), emptySet(), new ArrayList<>(), PlayerStatus.NORMAL);
        var legals = player.legals();

        assertThatThrownBy(legals::clear).isInstanceOf(UnsupportedOperationException.class);
    }
}
