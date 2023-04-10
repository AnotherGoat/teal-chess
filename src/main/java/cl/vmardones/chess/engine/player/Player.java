/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.player;

import java.util.Collections;
import java.util.List;

import cl.vmardones.chess.engine.board.BoardChecker;
import cl.vmardones.chess.engine.move.*;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Piece;

/**
 * The entity that controls the pieces in one side of the board. It can be controlled either by a
 * human or an AI.
 */
public abstract sealed class Player permits HumanPlayer {

    protected final Alliance alliance;
    protected final King king;
    protected final List<Piece> pieces;
    protected final List<Move> legals;
    protected final List<Move> opponentLegals;

    private final boolean inCheck;
    private final boolean noEscapeMoves;

    /* Player creation */

    /**
     * Create a new player.
     *
     * @param alliance The player's side of the board.
     * @param king The player's king.
     * @param pieces The player's pieces (including the king).
     * @param legals The legal moves of the player.
     * @param opponentLegals The legal moves of the player on the opposite side..
     */
    protected Player(Alliance alliance, King king, List<Piece> pieces, List<Move> legals, List<Move> opponentLegals) {
        this.alliance = alliance;
        this.king = king;
        this.pieces = pieces;
        this.legals = legals;
        this.opponentLegals = opponentLegals;
        // TODO: Instead of making the player calculate whether they are in check or not, pass a PlayerStatus enum
        inCheck = !BoardChecker.isUnderAttack(king.position(), opponentLegals).isEmpty();
        noEscapeMoves = calculateEscapeMoves();
    }

    /* Getters */

    public Alliance alliance() {
        return alliance;
    }

    public King king() {
        return king;
    }

    public List<Piece> pieces() {
        return Collections.unmodifiableList(pieces);
    }

    public List<Move> legals() {
        return Collections.unmodifiableList(legals);
    }

    /* Checking state */

    /**
     * Checks if the player is in check, which means that the king must be protected.
     *
     * @return True if the player is in check
     */
    public boolean inCheck() {
        return inCheck;
    }

    /**
     * Checks if the player is checkmated, which means they lost the game. This happens when the king
     * is in check and there are no escape moves.
     *
     * @return True if the player is in checkmate
     */
    public boolean isCheckmated() {
        return inCheck() && noEscapeMoves;
    }

    /**
     * Checks if the player is in stalemate, which means that game ends in a tie. This happens when
     * the king isn't in check and there are no escape moves.
     *
     * @return True if the player is in stalemate
     */
    public boolean inInStalemate() {
        return !inCheck() && noEscapeMoves;
    }

    /* Making moves */

    public MoveStatus testMove(Player currentPlayer, Move move) {
        if (move.isNone()) {
            return MoveStatus.NONE;
        }

        if (isIllegal(move)) {
            return MoveStatus.ILLEGAL;
        }

        var kingAttacks = BoardChecker.isUnderAttack(currentPlayer.king().position(), opponentLegals);

        if (!kingAttacks.isEmpty()) {
            return MoveStatus.CHECKS;
        }

        return MoveStatus.NORMAL;
    }

    /* toString */

    @Override
    public String toString() {
        if (isCheckmated()) {
            return String.format("%s Player, in checkmate!", alliance.name());
        }

        if (inCheck()) {
            return String.format("%s Player, in check!", alliance.name());
        }

        if (inInStalemate()) {
            return String.format("%s Player, in stalemate!", alliance.name());
        }

        return String.format("%s Player", alliance.name());
    }

    private boolean calculateEscapeMoves() {
        return legals.stream().map(move -> testMove(this, move)).noneMatch(status -> status == MoveStatus.NORMAL);
    }

    private boolean isIllegal(Move move) {
        return !legals.contains(move);
    }
}
