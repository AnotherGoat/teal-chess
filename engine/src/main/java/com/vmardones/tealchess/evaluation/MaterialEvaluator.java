/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.evaluation;

import java.util.List;
import java.util.Objects;

import com.vmardones.tealchess.board.Mailbox;
import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.position.Position;
import org.eclipse.jdt.annotation.Nullable;

public final class MaterialEvaluator implements BoardEvaluator {
    @Override
    public int evaluate(Position position) {
        var mailbox = new Mailbox(position.board());
        var pieces = mailbox.pieces();

        return materialValue(pieces, position.sideToMove())
                - materialValue(pieces, position.sideToMove().opposite());
    }

    // TODO: Possibly add a method that returns all the pieces of a specific color to the mailbox
    private int materialValue(List<@Nullable Piece> pieces, Color side) {
        return pieces.stream()
                .filter(Objects::nonNull)
                .filter(piece -> piece.color() == side)
                .mapToInt(Piece::value)
                .sum();
    }
}
