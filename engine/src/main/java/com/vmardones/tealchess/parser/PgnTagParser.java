/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vmardones.tealchess.ExcludeFromGeneratedReport;
import org.eclipse.jdt.annotation.Nullable;

final class PgnTagParser {

    static Map<String, String> parseTags(List<String> tags) {

        var tagMap = new HashMap<String, String>();

        for (var line : tags) {
            var pair = parseTag(line);

            if (pair != null) {
                var tag = pair.tag();

                if (tagMap.containsKey(tag)) {
                    throw new PgnParseException("Duplicated tag: " + tag);
                }

                tagMap.put(tag, pair.value());
            }
        }

        return tagMap;
    }

    @ExcludeFromGeneratedReport
    private PgnTagParser() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
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

    private record TagPair(String tag, String value) {}
}
