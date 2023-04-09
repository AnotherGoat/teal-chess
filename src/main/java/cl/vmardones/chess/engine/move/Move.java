/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import java.util.Objects;

import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.piece.Rook;
import org.eclipse.jdt.annotation.Nullable;

/** The action of moving a piece. */
public final class Move {

    private final MoveType type;
    private final Piece piece;
    private final Position destination;

    @Nullable private final Piece otherPiece;

    @Nullable private final Position rookDestination;
    // TODO: Actually implement this, it should be used for PGN notation hash (+ for check, # for checkmate)
    private final MoveResult result = MoveResult.CONTINUE;

    /* Creating moves */

    public static Move createNormal(Piece piece, Position destination) {
        var type = piece instanceof Pawn ? MoveType.PAWN_NORMAL : MoveType.NORMAL;
        return new Move(type, piece, destination);
    }

    public static Move createCapture(Piece piece, Position destination, Piece capturedPiece) {
        var type = piece instanceof Pawn ? MoveType.PAWN_CAPTURE : MoveType.CAPTURE;
        return new Move(type, piece, destination, capturedPiece);
    }

    public static Move createPawnJump(Pawn pawn, Position destination) {
        return new Move(MoveType.PAWN_JUMP, pawn, destination);
    }

    public static Move createEnPassant(Pawn pawn, Position destination, Piece capturedPiece) {
        return new Move(MoveType.EN_PASSANT, pawn, destination, capturedPiece);
    }

    public static Move createCastle(
            boolean kingSide, King king, Position kingDestination, Rook rook, Position rookDestination) {
        return new Move(
                kingSide ? MoveType.KING_CASTLE : MoveType.QUEEN_CASTLE, king, kingDestination, rook, rookDestination);
    }

    /* Getters */

    public MoveType type() {
        return type;
    }

    public Piece piece() {
        return piece;
    }

    public Position source() {
        return piece.position();
    }

    public Position destination() {
        return destination;
    }

    public @Nullable Piece otherPiece() {
        return otherPiece;
    }

    public @Nullable Position rookDestination() {
        return rookDestination;
    }

    public boolean isCapture() {
        return otherPiece != null && rookDestination == null;
    }

    public boolean isNone() {
        return piece.position() == destination;
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
                && result.equals(other.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, piece, destination, otherPiece, rookDestination, result);
    }

    /**
     * Represents a move using the notation defined by the PGN standard.
     * @return The move in PGN notation.
     */
    @Override
    public String toString() {
        return simpleToString() + result.endHash();
    }

    private Move(MoveType type, Piece piece, Position destination) {
        this(type, piece, destination, null);
    }

    private Move(MoveType type, Piece piece, Position destination, @Nullable Piece otherPiece) {
        this(type, piece, destination, otherPiece, null);
    }

    private Move(
            MoveType type,
            Piece piece,
            Position destination,
            @Nullable Piece otherPiece,
            @Nullable Position rookDestination) {
        this.type = type;
        this.piece = piece;
        this.destination = destination;
        this.otherPiece = otherPiece;
        this.rookDestination = rookDestination;
    }

    private String simpleToString() {
        return switch (type) {
            case CAPTURE -> piece.singleChar() + destination();
            case PAWN_CAPTURE -> String.format("%sx%s", piece.position().file(), destination());
            case KING_CASTLE -> "0-0";
            case QUEEN_CASTLE -> "0-0-0";
            default -> destination().toString();
        };
    }
}
