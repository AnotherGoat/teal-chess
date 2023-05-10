/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import java.util.Objects;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.parser.pgn.San;
import com.vmardones.tealchess.piece.*;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The action of moving a piece. This class only represents pseudo-legal moves.
 * Any legality checks must be done after using any of the static factory methods.
 * @see <a href="https://www.chessprogramming.org/Pseudo-Legal_Move">Pseudo-Legal Move</a>
 */
public final class Move implements San {

    private final MoveType type;
    private final Piece piece;
    private final Coordinate destination;

    @Nullable private final Piece otherPiece;

    @Nullable private final Coordinate rookDestination;

    private final @Nullable PromotionChoice promotionChoice;

    /* Creating moves */

    /**
     * Create a normal move, also known as a pawn push when made by a pawn.
     * @param piece The piece that makes the move.
     * @param destination The destination of the move.
     * @return A normal move.
     */
    public static Move normal(Piece piece, Coordinate destination) {
        var type = piece.isPawn() ? MoveType.PAWN_PUSH : MoveType.NORMAL;
        return new Move(type, piece, destination, null, null, null);
    }

    /**
     * Create a capturing move.
     * @param piece The piece that makes the move.
     * @param capturedPiece The piece captured by this move.
     * @return A capture.
     */
    public static Move capture(Piece piece, Piece capturedPiece) {
        var type = piece.isPawn() ? MoveType.PAWN_CAPTURE : MoveType.CAPTURE;
        return new Move(type, piece, capturedPiece.coordinate(), capturedPiece, null, null);
    }

    /**
     * Create a pawn's special first move, also known as a dobule push.
     * @param pawn The pawn that makes the move.
     * @param destination The destination of the move.
     * @return A double push.
     */
    public static Move doublePush(Pawn pawn, Coordinate destination) {
        return new Move(MoveType.DOUBLE_PUSH, pawn, destination, null, null, null);
    }

    /**
     * Create an en passant move.
     * @param pawn The pawn that makes the move.
     * @param destination The destination of the move.
     * @param enPassantTarget The pawn captured by this move.
     * @return An en passant move.
     */
    public static Move enPassant(Pawn pawn, Coordinate destination, Pawn enPassantTarget) {
        return new Move(MoveType.EN_PASSANT, pawn, destination, enPassantTarget, null, null);
    }

    /**
     * Create a castling move.
     * @param kingSide Whether the castle is done king side (true) or queen side (false).
     * @param king The moved king.
     * @param kingDestination The destination of the king.
     * @param rook The moved rook.
     * @param rookDestination The destination of the rook.
     * @return A castle move.
     */
    public static Move castle(
            boolean kingSide, King king, Coordinate kingDestination, Rook rook, Coordinate rookDestination) {
        var type = kingSide ? MoveType.KING_CASTLE : MoveType.QUEEN_CASTLE;
        return new Move(type, king, kingDestination, rook, rookDestination, null);
    }

    /**
     * Create a normal pawn move that ends in a promotion.
     * @param pawn The moved pawn.
     * @param destination The destination of the move.
     * @param choice The piece that the pawn will be promoted to.
     * @return A pawn push that ends in a promotion.
     */
    public static Move normalPromotion(Pawn pawn, Coordinate destination, PromotionChoice choice) {
        return new Move(MoveType.PAWN_PUSH, pawn, destination, null, null, choice);
    }

    /**
     * Create a capturing pawn move that ends in a promotion.
     * @param pawn The moved pawn.
     * @param capturedPiece The piece captured by this move.
     * @param choice The piece that the pawn will be promoted to.
     * @return A pawn capture that ends in a promotion.
     */
    public static Move capturePromotion(Pawn pawn, Piece capturedPiece, PromotionChoice choice) {
        return new Move(MoveType.PAWN_CAPTURE, pawn, capturedPiece.coordinate(), capturedPiece, null, choice);
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

    public @Nullable PromotionChoice promotionChoice() {
        return promotionChoice;
    }

    /**
     * Represents a move using the SAN movetext notation defined by the PGN standard.
     * Because the move is only pseudo-legal, this doesn't have the end hash.
     * @return The move in SAN movetext.
     */
    @Override
    public String san() {
        var base =
                switch (type) {
                    case CAPTURE -> piece.san() + "x" + destination();
                    case PAWN_CAPTURE, EN_PASSANT -> piece.coordinate().file() + "x" + destination();
                    case KING_CASTLE -> "O-O";
                    case QUEEN_CASTLE -> "O-O-O";
                    default -> piece.san() + destination();
                };

        if (promotionChoice == null) {
            return base;
        }

        return base + "=" + promotionChoice.san();
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
                && Objects.equals(promotionChoice, other.promotionChoice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, piece, destination, otherPiece, rookDestination, promotionChoice);
    }

    @Override
    public String toString() {
        return san();
    }

    Piece movedPiece() {
        return piece.moveTo(destination);
    }

    @Nullable Coordinate otherPieceCoordinate() {
        if (otherPiece == null) {
            return null;
        }

        return otherPiece.coordinate();
    }

    @Nullable Rook movedCastleRook() {
        if (otherPiece == null || !otherPiece.isRook() || rookDestination == null) {
            return null;
        }

        return (Rook) otherPiece.moveTo(rookDestination);
    }

    @Nullable Piece promotedPawn() {
        var movedPiece = movedPiece();

        if (!movedPiece.isPawn() || promotionChoice == null) {
            return null;
        }

        var movedPawn = (Pawn) movedPiece;
        return movedPawn.promote(promotionChoice);
    }

    private Move(
            MoveType type,
            Piece piece,
            Coordinate destination,
            @Nullable Piece otherPiece,
            @Nullable Coordinate rookDestination,
            @Nullable PromotionChoice promotionChoice) {
        if (piece.coordinate().equals(destination)) {
            throw new IllegalMoveException("The source and destination cannot be the same coordinate");
        }

        this.type = type;
        this.piece = piece;
        this.destination = destination;
        this.otherPiece = otherPiece;
        this.rookDestination = rookDestination;
        this.promotionChoice = promotionChoice;
    }
}
