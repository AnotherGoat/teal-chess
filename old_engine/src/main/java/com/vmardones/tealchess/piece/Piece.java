/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.parser.Unicode;
import com.vmardones.tealchess.parser.fen.Fen;
import com.vmardones.tealchess.parser.pgn.San;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;


public abstract sealed class Piece  {

    /* Getters */


    public String firstChar() {
        return type.firstChar();
    }

    public int value() {
        return type.value();
    }

    /* Comparing pieces */

    public boolean isAllyOf(Piece other) {
        return color == other.color;
    }

    public boolean isEnemyOf(Piece other) {
        return !isAllyOf(other);
    }

    public boolean sameTypeAs(Piece other) {
        return type == other.type;
    }

    /* Movement */

    /**
     * Move this piece to another square. No checks of any kind are done to check whether the move is
     * legal or not.
     *
     * @param destination The destination to move the piece to.
     * @return The piece after the move is completed.
     */
    public abstract Piece moveTo(Coordinate destination);
}
