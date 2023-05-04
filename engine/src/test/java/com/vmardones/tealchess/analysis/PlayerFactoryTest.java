/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.parser.FenParser;
import com.vmardones.tealchess.player.Color;
import com.vmardones.tealchess.player.PlayerStatus;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class PlayerFactoryTest {

    @Test
    void checkedPlayer() {
        var position = FenParser.parse("4k3/8/8/8/4R3/8/8/4K3 b - - 0 1");

        var pseudoLegals = new PseudoLegalGenerator(position).generate();
        var confirmedLegals = new LegalityTester(position).testPseudoLegals(pseudoLegals);
        var legals = new LegalMoveConverter(position).transformToLegals(confirmedLegals);

        var playerFactory = new PlayerFactory(position, legals);
        var whitePlayer = playerFactory.create(Color.WHITE);
        var blackPlayer = playerFactory.create(Color.BLACK);

        assertThat(whitePlayer.legals()).isEmpty();
        assertThat(blackPlayer.status()).isEqualTo(PlayerStatus.CHECKED);
        assertThat(blackPlayer.legals()).isNotEmpty();
    }

    @Test
    void checkmatedPlayer() {
        var position = FenParser.parse("4k3/4Q3/8/8/4R3/8/8/4K3 b - - 0 1");

        var pseudoLegals = new PseudoLegalGenerator(position).generate();
        var confirmedLegals = new LegalityTester(position).testPseudoLegals(pseudoLegals);
        var legals = new LegalMoveConverter(position).transformToLegals(confirmedLegals);

        var playerFactory = new PlayerFactory(position, legals);
        var whitePlayer = playerFactory.create(Color.WHITE);
        var blackPlayer = playerFactory.create(Color.BLACK);

        assertThat(whitePlayer.legals()).isEmpty();
        assertThat(blackPlayer.status()).isEqualTo(PlayerStatus.CHECKMATED);
        assertThat(blackPlayer.legals()).isEmpty();
    }

    @Test
    void stalematedPlayer() {
        var position = FenParser.parse("4k3/8/8/8/8/4n3/r7/4K3 w - - 0 1");

        var pseudoLegals = new PseudoLegalGenerator(position).generate();
        var confirmedLegals = new LegalityTester(position).testPseudoLegals(pseudoLegals);
        var legals = new LegalMoveConverter(position).transformToLegals(confirmedLegals);

        var playerFactory = new PlayerFactory(position, legals);
        var whitePlayer = playerFactory.create(Color.WHITE);
        var blackPlayer = playerFactory.create(Color.BLACK);

        assertThat(whitePlayer.status()).isEqualTo(PlayerStatus.STALEMATED);
        assertThat(whitePlayer.legals()).isEmpty();
        assertThat(blackPlayer.legals()).isEmpty();
    }
}
