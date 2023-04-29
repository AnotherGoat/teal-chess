/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import com.vmardones.tealchess.ExcludeFromNullAway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ExcludeFromNullAway
final class PgnTagParserTest {

    @Test
    void ignoreWhitespace() {
        var tags = List.of("  ", "\t\t");

        assertThat(PgnTagParser.parseTags(tags)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"[Round 3", "Date 2020.01.01]", "Result 1-0"})
    void noSurroundingBrackets(String tagPair) {
        var tags = List.of(tagPair);

        assertThatThrownBy(() -> PgnTagParser.parseTags(tags))
                .isInstanceOf(PgnParseException.class)
                .hasMessageContaining("brackets")
                .hasMessageContaining(tagPair);
    }

    @Test
    void noSeparator() {
        var tags = List.of("[Result1-0]");

        assertThatThrownBy(() -> PgnTagParser.parseTags(tags))
                .isInstanceOf(PgnParseException.class)
                .hasMessageContaining("separated")
                .hasMessageContaining("[Result1-0]");
    }

    @Test
    void lowerCaseTag() {
        var tags = List.of("[result 1-0]");

        assertThatThrownBy(() -> PgnTagParser.parseTags(tags))
                .isInstanceOf(PgnParseException.class)
                .hasMessageContaining("upper case")
                .hasMessageContaining("[result 1-0]");
    }

    @ParameterizedTest
    @ValueSource(strings = {"[Date 2021.10.03\"]", "[Result 1/2-1/2]", "[Round \"4]"})
    void noQuotes(String tagPair) {
        var tags = List.of(tagPair);

        assertThatThrownBy(() -> PgnTagParser.parseTags(tags))
                .isInstanceOf(PgnParseException.class)
                .hasMessageContaining("quotes")
                .hasMessageContaining(tagPair);
    }

    @Test
    void correctTag() {
        var tags = List.of("[Result \"1-0\"]");
        var expectedMap = Map.of("Result", "1-0");

        assertThat(PgnTagParser.parseTags(tags)).isEqualTo(expectedMap);
    }

    @Test
    void noDuplicateTags() {
        var tags = List.of("[Result \"1-0\"]", "[Result \"0-1\"]");

        assertThatThrownBy(() -> PgnTagParser.parseTags(tags))
                .isInstanceOf(PgnParseException.class)
                .hasMessageContaining("Duplicated tag")
                .hasMessageContaining("Result");
    }
}
