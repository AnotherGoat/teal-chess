/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import java.util.Objects;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.piece.King;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.Rook;
import org.eclipse.jdt.annotation.Nullable;

// TODO: Refactor this class after it works, use either a builder and/or a class hierarchy to avoid telescoping
// constructors
/** The action of moving a piece. */
public final class Move {

    private final MoveType type;
    private final Piece piece;
    private final Coordinate destination;

    @Nullable private final Piece otherPiece;

    @Nullable private final Coordinate rookDestination;

    private final boolean promotion;

    // TODO: Actually implement this, it should be used for PGN notation hash (+ for check, # for checkmate)
    private final MoveResult result = MoveResult.CONTINUE;

    /* Creating moves */

    public static Move createNormal(Piece piece, Coordinate destination) {
        var type = piece.isPawn() ? MoveType.PAWN_PUSH : MoveType.NORMAL;
        return new Move(type, piece, destination);
    }

    public static Move createCapture(Piece piece, Coordinate destination, Piece capturedPiece) {
        var type = piece.isPawn() ? MoveType.PAWN_CAPTURE : MoveType.CAPTURE;
        return new Move(type, piece, destination, capturedPiece);
    }

    public static Move createDoublePush(Pawn pawn, Coordinate destination) {
        return new Move(MoveType.DOUBLE_PUSH, pawn, destination);
    }

    public static Move createEnPassant(Pawn pawn, Coordinate destination, @Nullable Pawn enPassantTarget) {
        return new Move(MoveType.EN_PASSANT, pawn, destination, enPassantTarget);
    }

    public static Move createCastle(
            boolean kingSide, King king, Coordinate kingDestination, Rook rook, Coordinate rookDestination) {
        return new Move(
                kingSide ? MoveType.KING_CASTLE : MoveType.QUEEN_CASTLE, king, kingDestination, rook, rookDestination);
    }

    public static Move makePromotion(Move move) {
        return new Move(move.type, move.piece, move.destination, move.otherPiece, null, true);
    }

    /* Getters */

    public MoveType type() {
        return type;
    }

    public Piece piece() {
        return piece;
    }

    public Coordinate source() {
        return piece.coordinate();
    }

    public Coordinate destination() {
        return destination;
    }

    public @Nullable Piece otherPiece() {
        return otherPiece;
    }

    public @Nullable Coordinate rookDestination() {
        return rookDestination;
    }

    public boolean promotion() {
        return promotion;
    }

    public boolean isCapture() {
        return otherPiece != null && rookDestination == null;
    }

    public boolean isNone() {
        return piece.coordinate().equals(destination);
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
                && piece.equals(other.piece)
                && destination.equals(other.destination)
                && Objects.equals(otherPiece, other.otherPiece)
                && Objects.equals(rookDestination, other.rookDestination)
                && result.equals(other.result)
                && promotion == other.promotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, piece, destination, otherPiece, rookDestination, result, promotion);
    }

    /**
     * Represents a move using the notation defined by the PGN standard.
     * @return The move in PGN notation.
     */
    @Override
    public String toString() {
        return simpleToString() + result.endHash();
    }

    private Move(MoveType type, Piece piece, Coordinate destination) {
        this(type, piece, destination, null);
    }

    private Move(MoveType type, Piece piece, Coordinate destination, @Nullable Piece otherPiece) {
        this(type, piece, destination, otherPiece, null);
    }

    private Move(
            MoveType type,
            Piece piece,
            Coordinate destination,
            @Nullable Piece otherPiece,
            @Nullable Coordinate rookDestination) {
        this(type, piece, destination, otherPiece, rookDestination, false);
    }

    private Move(
            MoveType type,
            Piece piece,
            Coordinate destination,
            @Nullable Piece otherPiece,
            @Nullable Coordinate rookDestination,
            boolean promotion) {
        this.type = type;
        this.piece = piece;
        this.destination = destination;
        this.otherPiece = otherPiece;
        this.rookDestination = rookDestination;
        this.promotion = promotion;
    }

    private String simpleToString() {
        return switch (type) {
            case CAPTURE -> piece.singleChar() + destination();
            case PAWN_CAPTURE -> String.join(
                    "x", piece.coordinate().file(), destination().toString());
            case KING_CASTLE -> "0-0";
            case QUEEN_CASTLE -> "0-0-0";
            default -> destination().toString();
        };
    }
}
