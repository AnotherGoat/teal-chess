/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;

import static com.vmardones.tealchess.color.Color.BLACK;
import static com.vmardones.tealchess.color.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.generator.AttackGenerator;
import com.vmardones.tealchess.generator.LegalGenerator;
import com.vmardones.tealchess.parser.fen.FenParser;
import org.junit.jupiter.api.Test;

final class PlayerFactoryTest {

    PlayerFactory playerFactory = new PlayerFactory(new AttackGenerator(), new LegalGenerator());

    @Test
    void createFoolsMatePlayers() {
        var position = FenParser.parse("rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3");

        var whitePlayer = playerFactory.create(position, WHITE);

        assertThat(whitePlayer.status()).isEqualTo(PlayerStatus.CHECKMATED);
        assertThat(whitePlayer.legals()).isEmpty();

        var blackPlayer = playerFactory.create(position, BLACK);

        assertThat(blackPlayer.status()).isEqualTo(PlayerStatus.OK);
        assertThat(blackPlayer.legals()).isEmpty();
    }
}
