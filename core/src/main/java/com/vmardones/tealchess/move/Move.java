/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import java.util.Objects;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.parser.San;
import com.vmardones.tealchess.piece.*;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The action of moving a piece. This class only represents pseudo-legal moves.
 * @see <a href="https://www.chessprogramming.org/Pseudo-Legal_Move">Pseudo-Legal Move</a>
 */
public final class Move implements San {

    private final MoveType type;
    private final Piece piece;
    private final Coordinate destination;

    @Nullable private final Piece otherPiece;

    @Nullable private final Coordinate rookDestination;

    private final @Nullable PromotionChoice promotionChoice;

    /* Building moves */

    /**
     * The standard and only method to build a move. This is because there are many move types that require different information, so the build simplifies the building process. Please note that the move builder never checks if the move is pseudo-legal or not. Any checks should be done outside, before using the builder, and any moves built using this builder are assumed to be pseudo-legal.
     * @param piece Source square.
     * @param destination Destination square.
     * @return The move builder.
     */
    public static MoveBuilder builder(Piece piece, Coordinate destination) {
        return new MoveBuilder(piece, destination);
    }

    /* Transforming moves */

    /**
     * Mark this move as a promotion move. This should only be used if the piece is a pawn.
     * @param choice The piece that the pawn will be promoted to.
     * @return A version of this move that promotes the piece.
     */
    public Move makePromotion(PromotionChoice choice) {
        if (!piece.isPawn()) {
            throw new IllegalMoveException("Only pawns can be promoted to other pieces");
        }

        return new Move(type, piece, destination, otherPiece, rookDestination, choice);
    }

    /**
     * Convert this pseudo-legal move to a legal one. For this, a move result must be supplied.
     * @param result What happens to the opponent after the move is done.
     * @return A legal version of this move.
     */
    public LegalMove makeLegal(MoveResult result) {
        return new LegalMove(this, result);
    }

    /**
     * Convert this pseudo-legal move to a legal one. For this, a move result must be supplied.
     * This alternative also asks for a disambiguation type, which is needed for proper SAN movetext representation.
     * For unambiguous cases, use the shorter version of this method.
     * @param result What happens to the opponent after the move is done.
     * @param disambiguation Disambiguation type.
     * @return A legal version of this move.
     */
    public LegalMove makeLegal(MoveResult result, Disambiguation disambiguation) {
        return new LegalMove(this, result, disambiguation);
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

        return base + promotionChoice.san();
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

    private Move(MoveBuilder builder) {
        this(builder.type, builder.piece, builder.destination, builder.otherPiece, builder.rookDestination, null);
    }

    private Move(
            MoveType type,
            Piece piece,
            Coordinate destination,
            @Nullable Piece otherPiece,
            @Nullable Coordinate rookDestination,
            @Nullable PromotionChoice promotionChoice) {
        this.type = type;
        this.piece = piece;
        this.destination = destination;
        this.otherPiece = otherPiece;
        this.rookDestination = rookDestination;
        this.promotionChoice = promotionChoice;
    }

    /**
     * The class responsible for building any kind of pseudo-legal chess moves. Any legality checks must be done after using this builder. Every method of this builder finishes the building process, to avoid unexpected behavior.
     */
    public static class MoveBuilder {

        private MoveType type;
        private final Piece piece;
        private final Coordinate destination;

        @Nullable private Piece otherPiece;

        @Nullable private Coordinate rookDestination;

        public Move normal() {
            type = piece.isPawn() ? MoveType.PAWN_PUSH : MoveType.NORMAL;
            return new Move(this);
        }

        public Move capture(Piece capturedPiece) {
            type = piece.isPawn() ? MoveType.PAWN_CAPTURE : MoveType.CAPTURE;
            otherPiece = capturedPiece;
            return new Move(this);
        }

        public Move doublePush() {
            if (!piece.isPawn()) {
                throw new IllegalMoveException("Only pawns can make double push moves");
            }

            type = MoveType.DOUBLE_PUSH;
            return new Move(this);
        }

        public Move enPassant(Pawn attackedPawn) {
            if (!piece.isPawn()) {
                throw new IllegalMoveException("Only pawns can make en passant moves");
            }

            type = MoveType.EN_PASSANT;
            otherPiece = attackedPawn;
            return new Move(this);
        }

        public Move castle(boolean kingSide, Rook rook, Coordinate rookDestination) {
            if (!piece.isKing()) {
                throw new IllegalMoveException("Only kings can make castling moves");
            }

            type = kingSide ? MoveType.KING_CASTLE : MoveType.QUEEN_CASTLE;
            otherPiece = rook;
            this.rookDestination = rookDestination;
            return new Move(this);
        }

        private MoveBuilder(Piece piece, Coordinate destination) {
            this.piece = piece;
            this.destination = destination;
            type = MoveType.NORMAL;
        }
    }
}
