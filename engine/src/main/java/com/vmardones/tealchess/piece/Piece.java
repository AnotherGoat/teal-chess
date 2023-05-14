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

/**
 * A chess piece, which players can move in the board.
 * @see <a href="https://www.chessprogramming.org/Pieces">Pieces</a>
 */
public abstract sealed class Piece implements Fen, San, Unicode permits Bishop, King, Knight, Pawn, Queen, Rook {

    protected final PieceType type;
    protected final Coordinate coordinate;
    protected final Color color;
    protected final boolean sliding;
    protected final List<Vector> moveVectors;

    /* Getters */

    public Coordinate coordinate() {
        return coordinate;
    }

    public Color color() {
        return color;
    }

    /**
     * Checks whether a piece slides when moving or not.
     * Sliding pieces can move freely in a direction until they're blocked by other pieces and they can easily pin enemy pieces, like a Rook.
     * Non-sliding pieces have a fixed set of possible destinations, like a Knight.
     * @see <a href="https://www.chessprogramming.org/Sliding_Pieces">Sliding Pieces</a>
     * @return True if the piece slides.
     */
    public boolean sliding() {
        return sliding;
    }

    public List<Vector> moveVectors() {
        return moveVectors;
    }

    public String firstChar() {
        return type.firstChar();
    }

    public int value() {
        return type.value();
    }

    @Override
    public String fen() {
        return color.isBlack() ? firstChar().toLowerCase(Locale.ROOT) : firstChar();
    }

    /**
     * Represent this piece in SAN notation.
     * In most contexts, pawns don't have a SAN abbreviation.
     * If "P" is needed to represent a pawn, use singleChar instead.
     * @return String representation of this piece in SAN movetext.
     */
    @Override
    public String san() {
        return isPawn() ? "" : type.firstChar();
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

    /* equals, hashCode and toString */

    @Override
    public final boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Piece other)) {
            return false;
        }

        return type == other.type
                && coordinate.equals(other.coordinate)
                && color == other.color
                && sliding == other.sliding
                && moveVectors.equals(other.moveVectors);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(type, coordinate, color, sliding, moveVectors);
    }

    @Override
    public String toString() {
        return unicode() + coordinate;
    }

    protected Piece(PieceType type, Coordinate coordinate, Color color, List<Vector> moveVectors, boolean sliding) {
        this.type = type;
        this.coordinate = coordinate;
        this.color = color;
        this.moveVectors = moveVectors;
        this.sliding = sliding;
    }
}
