/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.parser.FenParser;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class LegalMoveConverterTest {

    @Test
    void fileDisambiguation() {
        var position = FenParser.parse("4k3/1r4r1/8/8/8/8/8/4K3 b - - 0 1");

        var pseudoLegals = new PseudoLegalGenerator(position).generate();
        var confirmedLegals = new LegalityTester(position).testPseudoLegals(pseudoLegals);
        var legalConverter = new LegalMoveConverter(position);

        var disambiguatedLegals = legalConverter.transformToLegals(confirmedLegals);

        assertThat(disambiguatedLegals).hasSize(5 + 12 + 12);
        assertThat(disambiguatedLegals.toString())
                .containsOnlyOnce("Rbc7")
                .containsOnlyOnce("Rgc7")
                .containsOnlyOnce("Rbd7")
                .containsOnlyOnce("Rgd7")
                .containsOnlyOnce("Rbe7+")
                .containsOnlyOnce("Rge7+")
                .containsOnlyOnce("Rbf7")
                .containsOnlyOnce("Rgf7");
    }

    @Test
    void rankDisambiguation() {
        // Yes, this is an extremely rare case with 2 bishops on tiles of the same color
        var position = FenParser.parse("4k3/8/2b5/8/2b5/8/8/4K3 b - - 0 1");

        var pseudoLegals = new PseudoLegalGenerator(position).generate();
        var confirmedLegals = new LegalityTester(position).testPseudoLegals(pseudoLegals);
        var legalConverter = new LegalMoveConverter(position);

        var disambiguatedLegals = legalConverter.transformToLegals(confirmedLegals);

        assertThat(disambiguatedLegals).hasSize(5 + 10 + 11);
        assertThat(disambiguatedLegals.toString()).containsOnlyOnce("B4d5").containsOnlyOnce("B6d5");
    }

    @Test
    void fullDisambiguation() {
        // Another rare case that requires a promoted horse
        var position = FenParser.parse("4k3/1N6/4N3/8/8/1N6/8/4K3 w - - 0 1");

        var pseudoLegals = new PseudoLegalGenerator(position).generate();
        var confirmedLegals = new LegalityTester(position).testPseudoLegals(pseudoLegals);
        var legalConverter = new LegalMoveConverter(position);

        var disambiguatedLegals = legalConverter.transformToLegals(confirmedLegals);

        assertThat(disambiguatedLegals).hasSize(5 + 4 + 8 + 6);
        assertThat(disambiguatedLegals.toString())
                .containsOnlyOnce("Nb3c5")
                .containsOnlyOnce("Nb7c5")
                .containsOnlyOnce("Ne6c5");
    }

    @Test
    void allDisambiguations() {
        // Yet another rare case with multiple promoted queens, which test all disambiguation types at the same time
        var position = FenParser.parse("4k3/8/8/2Q2Q2/8/8/5Q2/4K3 w - - 0 1");

        var pseudoLegals = new PseudoLegalGenerator(position).generate();
        var confirmedLegals = new LegalityTester(position).testPseudoLegals(pseudoLegals);
        var legalConverter = new LegalMoveConverter(position);

        var disambiguatedLegals = legalConverter.transformToLegals(confirmedLegals);

        assertThat(disambiguatedLegals).hasSize(4 + 20 + 20 + 15);
        assertThat(disambiguatedLegals.toString())
                .containsOnlyOnce("Qcd5")
                .containsOnlyOnce("Qfd5")
                .containsOnlyOnce("Qce5+")
                .containsOnlyOnce("Qfe5+")
                .containsOnlyOnce("Q2f3")
                .containsOnlyOnce("Q5f3")
                .containsOnlyOnce("Q2f4")
                .containsOnlyOnce("Q5f4")
                .containsOnlyOnce("Qc5c2")
                .containsOnlyOnce("Qf5c2")
                .containsOnlyOnce("Qf2c2");
    }

    @Test
    void firstLongSanExample() {
        // First seven character example from section 8.2.3.6 of the PGN standard at
        // http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm
        var position = FenParser.parse("2k5/1r6/Q7/3Q4/8/8/8/4K3 w - - 0 1");

        var pseudoLegals = new PseudoLegalGenerator(position).generate();
        var confirmedLegals = new LegalityTester(position).testPseudoLegals(pseudoLegals);
        var legalConverter = new LegalMoveConverter(position);

        var disambiguatedLegals = legalConverter.transformToLegals(confirmedLegals);

        assertThat(disambiguatedLegals.toString()).containsOnlyOnce("Qa6xb7#");
    }

    @Test
    void secondLongSanExample() {
        // Second seven character example from section 8.2.3.6 of the PGN standard at
        // http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm
        var position = FenParser.parse("4k3/8/8/8/8/8/5p2/1K4N1 b - - 0 1");

        var pseudoLegals = new PseudoLegalGenerator(position).generate();
        var confirmedLegals = new LegalityTester(position).testPseudoLegals(pseudoLegals);
        var legalConverter = new LegalMoveConverter(position);

        var disambiguatedLegals = legalConverter.transformToLegals(confirmedLegals);

        assertThat(disambiguatedLegals.toString()).containsOnlyOnce("fxg1=Q+");
    }
}
