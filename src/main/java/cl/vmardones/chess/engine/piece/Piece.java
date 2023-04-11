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

/** A chess piece. */
public abstract sealed class Piece permits JumpingPiece, SlidingPiece {

    protected static final List<int[]> ORTHOGONALS =
            List.of(new int[] {-1, 0}, new int[] {0, 1}, new int[] {1, 0}, new int[] {0, -1});
    protected static final List<int[]> DIAGONALS =
            List.of(new int[] {-1, 1}, new int[] {1, 1}, new int[] {-1, -1}, new int[] {1, -1});
    protected static final List<int[]> KNIGHT_JUMPS = List.of(
            new int[] {-1, 2},
            new int[] {1, 2},
            new int[] {-2, 1},
            new int[] {2, 1},
            new int[] {-1, -2},
            new int[] {1, -2},
            new int[] {-2, -1},
            new int[] {2, -1});
    protected static final List<int[]> WHITE_PAWN_MOVES =
            List.of(new int[] {-1, 1}, new int[] {0, 1}, new int[] {1, 1}, new int[] {0, 2});
    protected static final List<int[]> BLACK_PAWN_MOVES =
            List.of(new int[] {-1, -1}, new int[] {0, -1}, new int[] {1, -1}, new int[] {0, -2});

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

    /**
     * Move this piece to another square. No checks of any kind are done to check whether the move is
     * legal or not.
     *
     * @param destination The destination to move the piece to.
     * @return The piece after the move is completed.
     */
    public abstract Piece moveTo(String destination);

    // TODO: This method probably shouldn't be on the piece class
    /**
     * Calculates all the legal moves that this piece can do.
     *
     * @param board Current state of the game board.
     * @return List of possible legal moves.
     */
    public List<Move> calculateLegals(Board board) {
        return calculatePossibleDestinations(board).stream()
                .map(board::squareAt)
                .filter(this::canAccess)
                .map(square -> createMove(square, board))
                .filter(Objects::nonNull)
                .toList();
    }

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

    protected abstract List<Position> calculatePossibleDestinations(Board board);

    /**
     * Creates a move, based on the piece and the destination.
     *
     * @param destination The destination square.
     * @param board The current game board.
     * @return A move, selected depending on the source and destination.
     */
    protected @Nullable Move createMove(Square destination, Board board) {
        var destinationPiece = destination.piece();

        if (destinationPiece == null) {
            return Move.createNormal(this, destination.position());
        }

        if (isEnemyOf(destinationPiece)) {
            return Move.createCapture(this, destination.position(), destinationPiece);
        }

        return null;
    }
}
