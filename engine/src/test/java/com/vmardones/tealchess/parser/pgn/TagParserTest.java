/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser.pgn;

import static org.assertj.core.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.List;

import com.vmardones.tealchess.ExcludeFromNullAway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ExcludeFromNullAway
final class TagParserTest {

    @ParameterizedTest
    @ValueSource(strings = {"[Round 3", "Date 2020.01.01]", "Result 1-0"})
    void noSurroundingBrackets(String tagPair) {
        var tags = List.of(tagPair);

        assertThatThrownBy(() -> TagParser.parse(tags))
                .isInstanceOf(PgnParseException.class)
                .hasMessageContaining("brackets")
                .hasMessageContaining(tagPair);
    }

    @Test
    void noSeparator() {
        var tags = List.of("[Result1-0]");

        assertThatThrownBy(() -> TagParser.parse(tags))
                .isInstanceOf(PgnParseException.class)
                .hasMessageContaining("separated")
                .hasMessageContaining("[Result1-0]");
    }

    @Test
    void lowerCaseTag() {
        var tags = List.of("[result 1-0]");

        assertThatThrownBy(() -> TagParser.parse(tags))
                .isInstanceOf(PgnParseException.class)
                .hasMessageContaining("upper case")
                .hasMessageContaining("[result 1-0]");
    }

    @ParameterizedTest
    @ValueSource(strings = {"[Date 2021.10.03\"]", "[Result 1/2-1/2]", "[Round \"4]"})
    void noQuotes(String tagPair) {
        var tags = List.of(tagPair);

        assertThatThrownBy(() -> TagParser.parse(tags))
                .isInstanceOf(PgnParseException.class)
                .hasMessageContaining("quotes")
                .hasMessageContaining(tagPair);
    }

    @Test
    void noDuplicateTags() {
        var tags = List.of("[Result \"1-0\"]", "[Result \"0-1\"]");

        assertThatThrownBy(() -> TagParser.parse(tags))
                .isInstanceOf(PgnParseException.class)
                .hasMessageContaining("Duplicated tag")
                .hasMessageContaining("Result");
    }

    @Test
    void noMissingTags() {
        var tags = List.of("[Event \"?\"]", "[Site \"?\"]");

        assertThatThrownBy(() -> TagParser.parse(tags))
                .isInstanceOf(PgnParseException.class)
                .hasMessageContaining("Seven Tag Roster")
                .hasMessageContaining("Date");
    }

    @Test
    void fullSevenTagRoster() {
        var tags = List.of(
                "[Event \"?\"]",
                "[Site \"?\"]",
                "[Date \"????.??.??\"]",
                "[Round \"?\"]",
                "[White \"?\"]",
                "[Black \"?\"]",
                "[Result \"?\"]");

        var expectedTags = new LinkedHashMap<>();
        expectedTags.put("Event", "?");
        expectedTags.put("Site", "?");
        expectedTags.put("Date", "????.??.??");
        expectedTags.put("Round", "?");
        expectedTags.put("White", "?");
        expectedTags.put("Black", "?");
        expectedTags.put("Result", "?");

        assertThat(TagParser.parse(tags)).isEqualTo(expectedTags);
    }

    @Test
    void ignoreEmptyLines() {
        var tags = List.of(
                "",
                "[Event \"?\"]",
                "[Site \"?\"]",
                "",
                "[Date \"????.??.??\"]",
                "[Round \"?\"]",
                "",
                "[White \"?\"]",
                "",
                "[Black \"?\"]",
                "[Result \"?\"]");

        var expectedTags = new LinkedHashMap<>();
        expectedTags.put("Event", "?");
        expectedTags.put("Site", "?");
        expectedTags.put("Date", "????.??.??");
        expectedTags.put("Round", "?");
        expectedTags.put("White", "?");
        expectedTags.put("Black", "?");
        expectedTags.put("Result", "?");

        assertThat(TagParser.parse(tags)).isEqualTo(expectedTags);
    }

    @Test
    void sortSevenTagRoster() {
        var tags = List.of(
                "[Result \"?\"]",
                "[Date \"????.??.??\"]",
                "[Site \"?\"]",
                "[Event \"?\"]",
                "[White \"?\"]",
                "[Black \"?\"]",
                "[Round \"?\"]");

        var expectedTags = new LinkedHashMap<>();
        expectedTags.put("Event", "?");
        expectedTags.put("Site", "?");
        expectedTags.put("Date", "????.??.??");
        expectedTags.put("Round", "?");
        expectedTags.put("White", "?");
        expectedTags.put("Black", "?");
        expectedTags.put("Result", "?");

        assertThat(TagParser.parse(tags)).isEqualTo(expectedTags);
    }

    @Test
    void startWithSevenTagRoster() {
        var tags = List.of(
                "[Result \"?\"]",
                "[Opening \"Sicilian Defense\"]",
                "[Date \"????.??.??\"]",
                "[Site \"?\"]",
                "[Event \"?\"]",
                "[White \"?\"]",
                "[WhiteElo \"?\"]",
                "[Black \"?\"]",
                "[BlackElo \"?\"]",
                "[Round \"?\"]",
                "[TimeControl \"-\"]");

        var expectedTags = new LinkedHashMap<>();
        expectedTags.put("Event", "?");
        expectedTags.put("Site", "?");
        expectedTags.put("Date", "????.??.??");
        expectedTags.put("Round", "?");
        expectedTags.put("White", "?");
        expectedTags.put("Black", "?");
        expectedTags.put("Result", "?");
        expectedTags.put("Opening", "Sicilian Defense");
        expectedTags.put("WhiteElo", "?");
        expectedTags.put("BlackElo", "?");
        expectedTags.put("TimeControl", "-");

        assertThat(TagParser.parse(tags)).isEqualTo(expectedTags);
    }
}
