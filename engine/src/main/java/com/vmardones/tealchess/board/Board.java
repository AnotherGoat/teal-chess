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
import org.eclipse.jdt.annotation.Nullable;

/**
 * The chessboard, composed of 8x8 squares.
 * Each square is associated with a coordinate and can be individually checked.
 * @see <a href="https://www.chessprogramming.org/Chessboard">Chessboard</a>
 * @see <a href="https://www.chessprogramming.org/Squares">Squares</a>
 */
public final class Board implements Unicode {

    /** The chessboard is a square grid composed of squares. This is the number of squares per side. */
    public static final int SIDE_LENGTH = AlgebraicConverter.SIDE_LENGTH;
    /**
     * The number of squares in the game board.
     */
    public static final int NUMBER_OF_SQUARES = AlgebraicConverter.NUMBER_OF_SQUARES;

    private final Map<Coordinate, @Nullable Piece> mailbox;
    private final King whiteKing;
    private final Set<Piece> whitePieces;
    private final King blackKing;
    private final Set<Piece> blackPieces;

    /* Building the board */

    /**
     * The board is a complex object, so this builder is the standard method to create it. Both white
     * and black kings must be supplied for the board to be legal. This builder is intended for
     * creating a new board from scratch.
     *
     * @param whiteKing The white player's king.
     * @param blackKing The black player's king.
     * @return The board builder.
     */
    public static BoardBuilder builder(King whiteKing, King blackKing) {
        return new BoardBuilder(whiteKing, blackKing);
    }

    /**
     * A special builder intended to be used when players make a move. This can only be used after the
     * board has been initialized at least once. It keeps the current state of the board and lets you
     * specify only the differences from the previous position.
     * Only use this version if none of the kings weren't moved in the previous position.
     *
     * @return The next position builder.
     */
    public BoardBuilder nextPositionBuilder() {
        return new BoardBuilder(this, whiteKing, blackKing);
    }

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

    /* Checking the board */

    /**
     * Get the piece located at a specific coordinate.
     *
     * @param coordinate The coordinate to search.
     * @return The piece found.
     */
    public @Nullable Piece pieceAt(Coordinate coordinate) {
        return mailbox.get(coordinate);
    }

    /**
     * Check if a specific square is empty.
     *
     * @param coordinate The coordinate of the square to check.
     * @return True if the square doesn't have a piece.
     */
    public boolean isEmpty(Coordinate coordinate) {
        return pieceAt(coordinate) == null;
    }

    /**
     * Represent a square using Unicode characters.
     * This returns the piece's Unicode representation or a white/black square character for empty squares.
     * @param coordinate The coordinate of the square.
     * @return Unicode representation of the square.
     */
    public String unicodeSquare(Coordinate coordinate) {
        var piece = pieceAt(coordinate);

        if (piece == null) {
            return colorOf(coordinate).isWhite() ? "□" : "■";
        }

        return piece.unicode();
    }

    /**
     * Find the color of the square at a specific coordinate.
     * Mostly used to draw the board.
     * @param coordinate The coordinate of the square.
     * @return The color of the square.
     */
    public Color colorOf(Coordinate coordinate) {
        var index = coordinate.index();

        if ((index + index / SIDE_LENGTH) % 2 == 0) {
            return Color.WHITE;
        }

        return Color.BLACK;
    }

    /* Getters */

    public King king(Color color) {
        return color.isWhite() ? whiteKing : blackKing;
    }

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

    @Override
    public String unicode() {
        var result = new StringBuilder();

        for (int i = 0; i < NUMBER_OF_SQUARES; i++) {
            var coordinate = Coordinate.forIndex(i);
            result.append(unicodeSquare(coordinate)).append(" ");

            if ((i + 1) % SIDE_LENGTH == 0) {
                result.deleteCharAt(result.length() - 1).append("\n");
            }
        }

        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    /* equals, hashCode and toString */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var other = (Board) o;
        return mailbox.equals(other.mailbox)
                && whiteKing.equals(other.whiteKing)
                && whitePieces.equals(other.whitePieces)
                && blackKing.equals(other.blackKing)
                && blackPieces.equals(other.blackPieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mailbox, whitePieces, whiteKing, blackPieces, blackKing);
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

    /**
     * Responsible for building the board, a complex object.
     */
    public static class BoardBuilder {

        private final Map<Coordinate, Piece> pieces = new HashMap<>();
        private final King whiteKing;
        private final King blackKing;

        /**
         * Add a piece to the board. This action is silently ignored if the piece is null. If multiple
         * pieces are put in the same place, the last one takes precedence. Both the white and black
         * kings are added automatically when the build finishes.
         *
         * @param piece The piece to add.
         * @return The same instance of this builder, to continue the building process.
         */
        public BoardBuilder with(@Nullable Piece piece) {
            if (piece == null) {
                return this;
            }

            pieces.put(piece.coordinate(), piece);
            return this;
        }

        /**
         * Add multiple pieces to the board. Null pieces are silently ignored. If multiple pieces are put in the same coordinate, the last one takes precedence.
         * @param pieces The pieces to add.
         * @return The same instance of this builder, to continue the building process.
         */
        public BoardBuilder withAll(List<@Nullable Piece> pieces) {
            pieces.forEach(this::with);
            return this;
        }

        /**
         * Remove a piece from the board. This action is silently ignored if the piece is null.
         *
         * @param coordinate The coordinate to remove a piece from.
         * @return The same instance of this builder, to continue the building process.
         */
        public BoardBuilder without(@Nullable Coordinate coordinate) {
            if (coordinate == null) {
                return this;
            }

            pieces.remove(coordinate);
            return this;
        }

        /**
         * Finish the building process of the board.
         *
         * @return The finished, unmodifiable board.
         */
        public Board build() {
            if (!whiteKing.equals(pieces.get(whiteKing.coordinate()))) {
                with(whiteKing);
            }

            if (!blackKing.equals(pieces.get(blackKing.coordinate()))) {
                with(blackKing);
            }

            return new Board(this);
        }

        private BoardBuilder(Board board, King whiteKing, King blackKing) {
            this(whiteKing, blackKing);

            for (var color : Color.values()) {
                board.pieces(color).forEach(this::with);
            }
        }

        private BoardBuilder(King whiteKing, King blackKing) {
            this.whiteKing = whiteKing;
            with(whiteKing);

            this.blackKing = blackKing;
            with(blackKing);
        }
    }
}
