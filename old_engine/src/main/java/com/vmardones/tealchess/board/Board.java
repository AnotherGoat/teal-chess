/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toUnmodifiableSet;

import java.util.*;

import com.vmardones.tealchess.parser.Unicode;
import com.vmardones.tealchess.piece.King;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;
import org.jspecify.annotations.Nullable;


public final class Board implements Unicode {

    private final Map<Coordinate, @Nullable Piece> mailbox;
    private final Set<Piece> whitePieces;
    private final Set<Piece> blackPieces;



    /**
     * A special builder intended to be used when players make a move. This can only be used after the
     * board has been initialized at least once. It keeps the current state of the board and lets you
     * specify only the differences from the previous position.
     * Use this version if any of the kings were moved in the previous position.
     *
     * @param whiteKing The white king in the new board.
     * @param blackKing The black king in the new board.
     * @return The next position builder.
     */
    public BoardBuilder nextPositionBuilder(King whiteKing, King blackKing) {
        return new BoardBuilder(this, whiteKing, blackKing);
    }

    /* Getters */

    /**
     * Get a map containing all the pieces in the board.
     * The map is unmodifiable and sorted, starting from the a8 square and ending in the h1 square.
     * If a square doesn't have a piece, the entry will have a null value.
     * @return Unmodifiable and sorted map containing all the pieces.
     */
    public Map<Coordinate, @Nullable Piece> mailbox() {
        return unmodifiableMap(mailbox);
    }

    public Set<Piece> pieces(Color color) {
        return color.isWhite() ? whitePieces : blackPieces;
    }

    private Board(BoardBuilder builder) {
        mailbox = fillEmptySquares(builder.pieces);

        whiteKing = builder.whiteKing;
        whitePieces = filterPieces(builder.pieces.values(), Color.WHITE);

        blackKing = builder.blackKing;
        blackPieces = filterPieces(builder.pieces.values(), Color.BLACK);
    }

    private Map<Coordinate, @Nullable Piece> fillEmptySquares(Map<Coordinate, Piece> pieces) {

        var filledPieces = new LinkedHashMap<Coordinate, @Nullable Piece>();

        for (int i = 0; i < NUMBER_OF_SQUARES; i++) {
            var coordinate = Coordinate.forIndex(i);
            filledPieces.put(coordinate, pieces.get(coordinate));
        }

        return filledPieces;
    }

    private Set<Piece> filterPieces(Collection<Piece> pieces, Color color) {
        return pieces.stream().filter(piece -> piece.color() == color).collect(toUnmodifiableSet());
    }

    public static class BoardBuilder {
        private BoardBuilder(Board board, King whiteKing, King blackKing) {
            this(whiteKing, blackKing);

            for (var color : Color.values()) {
                board.pieces(color).forEach(this::with);
            }
        }
    }
}
