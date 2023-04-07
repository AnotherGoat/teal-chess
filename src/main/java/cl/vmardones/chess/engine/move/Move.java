/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import java.util.Objects;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Piece;
import org.eclipse.jdt.annotation.Nullable;

/** The action of moving a piece. */
public final class Move {

    private final MoveType type;
    private final Board board;
    private final Piece piece;
    private final Position destination;

    @Nullable private final Piece otherPiece;

    @Nullable private final Position rookDestination;

    /* Creating moves */

    public Move(MoveType type, Board board, Piece piece, Position destination) {
        this(type, board, piece, destination, null);
    }

    public Move(MoveType type, Board board, Piece piece, Position destination, @Nullable Piece otherPiece) {
        this(type, board, piece, destination, otherPiece, null);
    }

    public Move(
            MoveType type,
            Board board,
            Piece piece,
            Position destination,
            @Nullable Piece otherPiece,
            @Nullable Position rookDestination) {
        this.type = type;
        this.board = board;
        this.piece = piece;
        this.destination = destination;
        this.otherPiece = otherPiece;
        this.rookDestination = rookDestination;
    }

    /* Getters */

    public MoveType type() {
        return type;
    }

    public Piece piece() {
        return piece;
    }

    public Position destination() {
        return destination;
    }

    public @Nullable Piece otherPiece() {
        return otherPiece;
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

        var other = (Move) o;
        return type == other.type
                && board.equals(other.board)
                && piece.equals(other.piece)
                && destination.equals(other.destination)
                && Objects.equals(otherPiece, other.otherPiece)
                && Objects.equals(rookDestination, other.rookDestination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, board, piece, destination, otherPiece, rookDestination);
    }

    @Override
    public String toString() {
        return switch (type) {
            case CAPTURE -> piece.singleChar() + destination();
            case PAWN_CAPTURE -> String.format("%sx%s", piece.position().file(), destination());
            case KING_CASTLE -> "0-0";
            case QUEEN_CASTLE -> "0-0-0";
            default -> destination().toString();
        };
    }

    /// OLDER
    public boolean isCapturing() {
        return otherPiece != null && rookDestination == null;
    }

    public boolean isCastling() {
        return otherPiece != null && rookDestination != null;
    }

    public boolean isNone() {
        return piece.position() == destination;
    }

    // TODO: Fix en passant implementation, highlighted moves don't match moves that are executed. En
    // passant pawns are set properly, but it doesn't get added to the list of legal moves
    /**
     * When a move is made, a new board is created, due to the board class being immutable.
     *
     * @return The new board, after the move was made.
     */
    public Board make() {
        var builder = board.nextTurnBuilder();

        builder.without(piece).without(otherPiece).with(piece.moveTo(destination.toString()));

        if (type == MoveType.PAWN_JUMP) {
            builder.enPassantPawn((Pawn) piece.moveTo(destination.toString()));
        }

        if (isCastling()) {
            builder.with(otherPiece.moveTo(rookDestination.toString()));
        }

        return builder.build();
    }

    public Position getSource() {
        return piece.position();
    }
}
