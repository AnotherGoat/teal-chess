/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static java.util.Collections.unmodifiableList;

import java.util.*;
import java.util.stream.IntStream;

import com.vmardones.tealchess.parser.Unicode;
import com.vmardones.tealchess.piece.King;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The chessboard, made of 8x8 squares.
 * @see <a href="https://www.chessprogramming.org/Chessboard">Chessboard</a>
 */
public final class Board implements Unicode {

    /** The chessboard is a square grid composed of squares. This is the number of squares per side. */
    public static final int SIDE_LENGTH = 8;
    /**
     * The number of squares in the game board.
     */
    public static final int MAX_SQUARES = SIDE_LENGTH * SIDE_LENGTH;

    static final int FIRST_SQUARE_INDEX = 0;

    private final List<Square> squares;
    private final King whiteKing;
    private final List<Piece> whitePieces;
    private final King blackKing;
    private final List<Piece> blackPieces;

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
     * Get the square located at a specific coordinate.
     *
     * @param coordinate The coordinate to search.
     * @return The square found.
     */
    public Square squareAt(Coordinate coordinate) {
        return squares.get(coordinate.index());
    }

    /**
     * Get the square located at a specific coordinate.
     *
     * @param coordinate The coordinate to search, in algebraic notation.
     * @return The square found.
     */
    public Square squareAt(String coordinate) {
        return squareAt(Coordinate.of(coordinate));
    }

    /**
     * Get the piece located at a specific coordinate.
     *
     * @param coordinate The coordinate to search.
     * @return The piece found.
     */
    public @Nullable Piece pieceAt(Coordinate coordinate) {
        return squares.get(coordinate.index()).piece();
    }

    /**
     * Get the piece located at a specific coordinate.
     *
     * @param coordinate The coordinate to search, in algebraic notation.
     * @return The piece found.
     */
    public @Nullable Piece pieceAt(String coordinate) {
        return pieceAt(Coordinate.of(coordinate));
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

    /* Getters */

    public List<Square> squares() {
        return unmodifiableList(squares);
    }

    public King king(Color color) {
        return color.isWhite() ? whiteKing : blackKing;
    }

    public List<Piece> pieces(Color color) {
        var pieces = color.isWhite() ? whitePieces : blackPieces;

        return unmodifiableList(pieces);
    }

    @Override
    public String unicode() {
        var result = new StringBuilder();

        for (Square square : squares) {
            result.append(square.unicode()).append(" ");

            if ((square.coordinate().index() + 1) % Board.SIDE_LENGTH == 0) {
                result.append("\n");
            }
        }

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
        return squares.equals(other.squares)
                && whiteKing.equals(other.whiteKing)
                && whitePieces.equals(other.whitePieces)
                && blackKing.equals(other.blackKing)
                && blackPieces.equals(other.blackPieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(squares, whitePieces, whiteKing, blackPieces, blackKing);
    }

    private Board(BoardBuilder builder) {
        squares = createSquares(builder);

        whiteKing = builder.whiteKing;
        whitePieces = findPieces(squares, Color.WHITE);

        blackKing = builder.blackKing;
        blackPieces = findPieces(squares, Color.BLACK);
    }

    private List<Square> createSquares(BoardBuilder builder) {
        return IntStream.range(FIRST_SQUARE_INDEX, MAX_SQUARES)
                .mapToObj(
                        index -> Square.create(AlgebraicConverter.toAlgebraic(index), builder.configuration.get(index)))
                .toList();
    }

    private List<Piece> findPieces(List<Square> gameBoard, Color color) {
        return gameBoard.stream()
                .map(Square::piece)
                .filter(piece -> piece != null && piece.color() == color)
                .toList();
    }

    /**
     * Responsible for building the board, a complex object.
     */
    public static class BoardBuilder {

        private final Map<Integer, Piece> configuration = new HashMap<>();
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

            configuration.put(piece.coordinate().index(), piece);
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
         * @param piece The piece to remove.
         * @return The same instance of this builder, to continue the building process.
         */
        public BoardBuilder without(@Nullable Piece piece) {
            if (piece == null) {
                return this;
            }

            configuration.remove(piece.coordinate().index(), piece);
            return this;
        }

        /**
         * Finish the building process of the board.
         *
         * @return The finished, unmodifiable board.
         */
        public Board build() {
            if (!whiteKing.equals(configuration.get(whiteKing.coordinate().index()))) {
                with(whiteKing);
            }

            if (!blackKing.equals(configuration.get(blackKing.coordinate().index()))) {
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
