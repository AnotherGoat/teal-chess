/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import static com.vmardones.tealchess.move.MoveType.*;
import static com.vmardones.tealchess.square.Square.*;

import java.util.List;
import java.util.Locale;

import com.vmardones.tealchess.piece.PromotionChoice;
import com.vmardones.tealchess.square.AlgebraicConverter;
import org.eclipse.jdt.annotation.Nullable;

public record Move(MoveType type, int source, int destination, @Nullable PromotionChoice promotionChoice) {

    /**
     * The two steps done during a white short castle.
     * The first one moves the king 2 steps to the right, the second one moves the rook to protect the king.
     */
    public static final List<Move> WHITE_SHORT_CASTLE_STEPS =
            List.of(new Move(SHORT_CASTLE, e1, g1), new Move(NORMAL, h1, f1));
    /**
     * The two steps done during a white long castle.
     * The first one moves the king 3 steps to the left, the second one moves the rook to protect the king.
     */
    public static final List<Move> WHITE_LONG_CASTLE_STEPS =
            List.of(new Move(LONG_CASTLE, e1, c1), new Move(NORMAL, a1, d1));
    /**
     * The two steps done during a black short castle.
     * The first one moves the king 2 steps to the right, the second one moves the rook to protect the king.
     */
    public static final List<Move> BLACK_SHORT_CASTLE_STEPS =
            List.of(new Move(SHORT_CASTLE, e8, g8), new Move(NORMAL, h8, f8));
    /**
     * The two steps done during a black long castle.
     * The first one moves the king 3 steps to the left, the second one moves the rook to protect the king.
     */
    public static final List<Move> BLACK_LONG_CASTLE_STEPS =
            List.of(new Move(LONG_CASTLE, e8, c8), new Move(NORMAL, a8, d8));

    public Move(MoveType type, int source, int destination) {
        this(type, source, destination, null);
    }

    @Override
    public String toString() {
        var base = AlgebraicConverter.toAlgebraic(source) + AlgebraicConverter.toAlgebraic(destination);

        if (promotionChoice == null) {
            return base;
        }

        return base + promotionChoice.san().toLowerCase(Locale.ROOT);
    }
}
