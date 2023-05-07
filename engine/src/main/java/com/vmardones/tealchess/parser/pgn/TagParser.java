/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser.pgn;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

final class TagParser {

    private static final List<String> sevenTagRoster =
            List.of("Event", "Site", "Date", "Round", "White", "Black", "Result");

    static Map<String, String> parse(List<String> tags) {
        var collectedTags = collectTags(tags);
        return sortSevenTagRoster(collectedTags);
    }

    private static Map<String, String> collectTags(List<String> tags) {
        var collectedTags = new LinkedHashMap<String, String>();

        for (var line : tags) {
            var pair = parseTag(line);

            if (pair != null) {
                var tag = pair.tag();

                if (collectedTags.containsKey(tag)) {
                    throw new PgnParseException("Duplicated tag: " + tag);
                }

                collectedTags.put(tag, pair.value());
            }
        }

        for (var tag : sevenTagRoster) {
            if (!collectedTags.containsKey(tag)) {
                throw new PgnParseException("The tag " + tag + " from the Seven Tag Roster is missing");
            }
        }

        return collectedTags;
    }

    private static @Nullable TagPair parseTag(String line) {
        line = line.trim();

        if (line.isEmpty()) {
            return null;
        }

        if (!line.startsWith("[") || !line.endsWith("]")) {
            throw new PgnParseException("The tag pair isn't surrounded by brackets: " + line);
        }

        var tagSeparatorIndex = line.indexOf(" ");

        if (tagSeparatorIndex == -1) {
            throw new PgnParseException("The tag pair isn't separated by a space: " + line);
        }

        var tag = line.substring(1, tagSeparatorIndex);

        if (!Character.isUpperCase(tag.charAt(0))) {
            throw new PgnParseException("The tag name doesn't start with an upper case letter: " + line);
        }

        var stringValue = line.substring(tagSeparatorIndex + 1, line.length() - 1);

        if (!stringValue.startsWith("\"") || !stringValue.endsWith("\"")) {
            throw new PgnParseException("The tag value isn't surrounded by quotes: " + line);
        }

        var value = stringValue.substring(1, stringValue.length() - 1);

        return new TagPair(tag, value);
    }

    private static Map<String, String> sortSevenTagRoster(Map<String, String> tags) {
        var sortedTags = new LinkedHashMap<String, String>();

        for (var tag : sevenTagRoster) {
            var value = tags.get(tag);

            sortedTags.put(tag, value);
            tags.remove(tag);
        }

        sortedTags.putAll(tags);
        return sortedTags;
    }

    private TagParser() {}

    private record TagPair(String tag, String value) {}
}
