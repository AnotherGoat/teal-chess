/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser.pgn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.move.Move;

/**
 * PGN (Portable Game Notation) serializer.
 * Parses data written in PNG export format.
 */
public final class PgnSerializer {

    public static String serialize(Game game) {
        var tags = serializeTags(game.tags());
        var moves = serializeMoves(game.moveHistory());

        return tags + "\n" + moves;
    }

    public static String serializeTags(Map<String, String> tags) {
        var result = new StringBuilder();

        for (var entry : tags.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();

            result.append("[").append(key).append(" \"").append(value).append("\"]\n");
        }

        return result.toString();
    }

    public static String serializeMoves(List<Move> moves) {
        List<String> moveText = new ArrayList<>();

        for (var i = 0; i < moves.size(); i++) {
            if (i % 2 == 0) {
                var fullMove = String.valueOf(i / 2 + 1);
                moveText.add(fullMove + ".");
            }

            // TODO: Reimplement san() method for moves
            // moveText.add(moves.get(i).san());
        }

        return String.join(" ", moveText.toArray(new String[moves.size()]));
    }

    private PgnSerializer() {}
}
