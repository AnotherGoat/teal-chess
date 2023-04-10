/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import java.util.*;
import java.util.stream.IntStream;

import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Alliance;
import org.eclipse.jdt.annotation.Nullable;

/** The chessboard, made of 8x8 squares. */
public final class Board {

    /** The chessboardd is a square grid composed of squares. This is the number of squares per side. */
    public static final int SIDE_LENGTH = 8;
    /**
     * The index of the first square in the game board. This is meant to be used along with MAX_SQUARES to
     * easily fill the game board.
     */
    public static final int MIN_SQUARES = 0;
    /**
     * The number of squares in the game board. This is meant to be used along with MIN_SQUARES to
     * easily fill the game board.
     */
    public static final int MAX_SQUARES = SIDE_LENGTH * SIDE_LENGTH;

    private final List<Square> squares;
    private final King whiteKing;
    private final List<Piece> whitePieces;
    private final King blackKing;
    private final List<Piece> blackPieces;
    private final @Nullable Pawn enPassantPawn;

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
     * specify only the differences from the previous turn.
     * Only use this version if none of the kings weren't moved in the previous turn.
     *
     * @return The next turn builder.
     */
    public BoardBuilder nextTurnBuilder() {
        return new BoardBuilder(this, whiteKing, blackKing);
    }

    /**
     * A special builder intended to be used when players make a move. This can only be used after the
     * board has been initialized at least once. It keeps the current state of the board and lets you
     * specify only the differences from the previous turn.
     * Use this version if any of the kings were moved in the previous turn.
     *
     * @param whiteKing The white king in the new board.
     * @param blackKing The black king in the new board.
     * @return The next turn builder.
     */
    public BoardBuilder nextTurnBuilder(King whiteKing, King blackKing) {
        return new BoardBuilder(this, whiteKing, blackKing);
    }

    /* Checking the board */

    /**
     * Get the square located at a specific position.
     *
     * @param position The position to search.
     * @return The square found.
     */
    public Square squareAt(Position position) {
        return squares.get(position.index());
    }

    /**
     * Get the square located at a specific position.
     *
     * @param position The position to search, in algebraic notation.
     * @return The square found.
     */
    public Square squareAt(String position) {
        return squareAt(Position.of(position));
    }

    /**
     * Get the piece located at a specific position.
     *
     * @param position The position to search.
     * @return The piece found.
     */
    public @Nullable Piece pieceAt(Position position) {
        return squares.get(position.index()).piece();
    }

    /**
     * Get the piece located at a specific position.
     *
     * @param position The position to search, in algebraic notation.
     * @return The piece found.
     */
    public @Nullable Piece pieceAt(String position) {
        return pieceAt(Position.of(position));
    }

    /**
     * Check whether the specified square contains a type of piece or not.
     *
     * @param position The position to search, in algebraic notation.
     * @param pieceType The type of piece to search.
     * @return True if the square has a piece of the specified type. Always returns false if the square is
     *     empty.
     */
    public boolean contains(String position, Class<? extends Piece> pieceType) {
        var piece = pieceAt(position);

        return pieceType.isInstance(piece);
    }

    /**
     * Check if a specific square is empty.
     *
     * @param position The position of the square to check.
     * @return True if the square doesn't have a piece.
     */
    public boolean isEmpty(Position position) {
        return squareAt(position).piece() == null;
    }

    /* Getters */

    public List<Square> squares() {
        return Collections.unmodifiableList(squares);
    }

    public King king(Alliance alliance) {
        return alliance == Alliance.WHITE ? whiteKing : blackKing;
    }

    public List<Piece> pieces(Alliance alliance) {
        var pieces = alliance == Alliance.WHITE ? whitePieces : blackPieces;

        return Collections.unmodifiableList(pieces);
    }

    public @Nullable Pawn enPassantPawn() {
        return enPassantPawn;
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
                && blackPieces.equals(other.blackPieces)
                && Objects.equals(enPassantPawn, other.enPassantPawn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(squares, whitePieces, whiteKing, blackPieces, blackKing, enPassantPawn);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();

        IntStream.range(MIN_SQUARES, MAX_SQUARES)
                .mapToObj(Position::of)
                .map(position -> String.format(getFormat(position), squareAt(position)))
                .forEach(builder::append);

        return builder.toString();
    }

    private Board(BoardBuilder builder) {
        squares = createSquares(builder);

        whiteKing = builder.whiteKing;
        whitePieces = findPieces(squares, Alliance.WHITE);

        blackKing = builder.blackKing;
        blackPieces = findPieces(squares, Alliance.BLACK);

        enPassantPawn = builder.enPassantPawn;
    }

    // TODO: Cache all possible algebraic coordinates
    private List<Square> createSquares(BoardBuilder builder) {
        return IntStream.range(MIN_SQUARES, MAX_SQUARES)
                .mapToObj(
                        index -> Square.create(AlgebraicConverter.toAlgebraic(index), builder.configuration.get(index)))
                .toList();
    }

    private List<Piece> findPieces(List<Square> gameBoard, Alliance alliance) {
        return gameBoard.stream()
                .map(Square::piece)
                .filter(piece -> piece != null && piece.alliance() == alliance)
                .toList();
    }

    private String getFormat(Position position) {
        return (position.index() + 1) % Board.SIDE_LENGTH == 0 ? "%s  \n" : "%s  ";
    }

    /**
     * Responsible for building the board, a complex object.
     *
     * @see Board
     */
    public static class BoardBuilder {

        private final Map<Integer, Piece> configuration = new HashMap<>();
        private final King whiteKing;
        private final King blackKing;
        private @Nullable Pawn enPassantPawn;

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

            configuration.put(piece.position().index(), piece);
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

            configuration.remove(piece.position().index(), piece);
            return this;
        }

        /**
         * Set the pawn that made a pawn jump in the previous turn, which can be captured by an
         * enemy pawn's en passant move. By default, no en passant pawn is set.
         *
         * @param pawn The pawn that made the pawn jump.
         * @return The same instance of this builder, to continue the building process.
         */
        public BoardBuilder enPassantPawn(Pawn pawn) {
            this.enPassantPawn = pawn;
            return this;
        }

        /**
         * Finish the building process of the board.
         *
         * @return The finished, unmodifiable board.
         */
        public Board build() {
            if (!configuration.get(whiteKing.position().index()).equals(whiteKing)) {
                with(whiteKing);
            }

            if (!configuration.get(blackKing.position().index()).equals(blackKing)) {
                with(blackKing);
            }

            return new Board(this);
        }

        private BoardBuilder(Board board, King whiteKing, King blackKing) {
            this(whiteKing, blackKing);

            for (var alliance : Alliance.values()) {
                board.pieces(alliance).forEach(this::with);
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
