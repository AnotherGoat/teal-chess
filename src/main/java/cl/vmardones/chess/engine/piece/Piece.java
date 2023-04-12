/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;
import java.util.Objects;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.board.Square;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.player.Alliance;
import org.eclipse.jdt.annotation.Nullable;

/** A chess piece, which players can move in the board. */
public abstract sealed class Piece permits JumpingPiece, SlidingPiece {

    protected final Position position;
    protected final Alliance alliance;
    protected final boolean firstMove;

    /* Getters */

    public Position position() {
        return position;
    }

    public Alliance alliance() {
        return alliance;
    }

    public boolean firstMove() {
        return firstMove;
    }

    public String singleChar() {
        var singleChar = getClass().getSimpleName().substring(0, 1);

        return alliance == Alliance.BLACK ? singleChar.toLowerCase() : singleChar;
    }

    /* Comparing pieces */

    public boolean isAllyOf(Piece other) {
        return alliance() == other.alliance();
    }

    public boolean isEnemyOf(Piece other) {
        return !isAllyOf(other);
    }

    /* Movement */

    public abstract List<Position> calculatePossibleDestinations(Board board);

    /**
     * Move this piece to another square. No checks of any kind are done to check whether the move is
     * legal or not.
     *
     * @param destination The destination to move the piece to.
     * @return The piece after the move is completed.
     */
    public abstract Piece moveTo(String destination);

    /**
     * Checks if this piece can get to the given destination. This happens only if the destination is
     * free or has a piece that can be captured.
     *
     * @param destination The target destination
     * @return True if the piece can get to the destination
     */
    public boolean canAccess(Square destination) {
        var pieceAtDestination = destination.piece();
        return pieceAtDestination == null || isEnemyOf(pieceAtDestination);
    }

    /* equals, hashCode and toString */

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var other = (Piece) o;
        return position.equals(other.position) && alliance == other.alliance && firstMove == other.firstMove;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(position, alliance, firstMove);
    }

    @Override
    public String toString() {
        return String.format("%s [%s, %s, %s]", getClass().getSimpleName(), position, alliance, firstMove);
    }

    protected Piece(String position, Alliance alliance, boolean firstMove) {
        this.position = Position.of(position);
        this.alliance = alliance;
        this.firstMove = firstMove;
    }
}
