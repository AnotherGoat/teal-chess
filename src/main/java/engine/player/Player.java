package engine.player;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import engine.board.Board;
import engine.board.BoardService;
import engine.move.*;
import engine.piece.King;
import engine.piece.Piece;
import engine.piece.Rook;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The entity that controls the pieces in one side of the board.
 * It can be controlled either by a human or an AI.
 */
@ToString
public abstract class Player {

    @ToString.Exclude
    protected final Board board;
    @Getter
    protected final King king;
    @Getter
    @ToString.Exclude
    protected final Collection<Move> legalMoves;
    private final boolean inCheck;
    private Boolean noEscapeMoves;
    private final BoardService boardService;

    protected Player(Board board, King king, Collection<Move> legalMoves,
                     Collection<Move> opponentMoves, BoardService boardService) {
        this.board = board;
        this.king = king;
        this.boardService = boardService;

        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateCastles(opponentMoves)));
        inCheck = !Player.calculateAttacksOnTile(king.getPosition(), opponentMoves).isEmpty();
    }

    protected static Collection<Move> calculateAttacksOnTile(int kingPosition, Collection<Move> moves) {
        return moves.stream()
                .filter(move -> kingPosition == move.getDestination())
                .collect(ImmutableList.toImmutableList());
    }

    /**
     * Used to check if a specific move can be performed.
     *
     * @param move The move to check
     * @return True if the move is legal
     */
    public boolean isMoveLegal(final Move move) {
        return legalMoves.contains(move);
    }

    /**
     * Checks if the player is in check, which means that the king must be protected.
     *
     * @return True if the player is in check
     */
    public boolean isInCheck() {
        return inCheck;
    }

    /**
     * Checks if the player is in checkmate, which means the game is lost.
     * This happens when the king is in check and there are no escape moves.
     *
     * @return True if the player is in checkmate
     */
    public boolean isInCheckmate() {
        return isInCheck() && hasNoEscapeMoves();
    }

    private boolean hasNoEscapeMoves() {
        if (noEscapeMoves == null) {
            noEscapeMoves = legalMoves.stream()
                    .map(this::makeMove)
                    .noneMatch(transition -> transition.getMoveStatus().isDone());
        }

        return noEscapeMoves;
    }

    /**
     * Checks if the player is in stalemate, which means that game ends in a tie.
     * This happens when the king isn't in check and there are no escape moves.
     *
     * @return True if the player is in stalemate
     */
    public boolean inInStalemate() {
        return !isInCheck() && hasNoEscapeMoves();
    }

    public boolean isCastled() {
        return false;
    }

    public MoveTransition makeMove(Move move) {
        if (!isMoveLegal(move)) {
            return new MoveTransition(board, move, MoveStatus.ILLEGAL);
        }

        final var transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.getCurrentPlayer().getOpponent().king.getPosition(),
                transitionBoard.getCurrentPlayer().legalMoves);

        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    /**
     * Obtains the player's current pieces on the board.
     *
     * @return The player's active pieces
     */
    public abstract Collection<Piece> getActivePieces();

    /**
     * Obtains the player's side.
     *
     * @return The player's alliance
     */
    public abstract Alliance getAlliance();

    /**
     * Obtains the player in the other side of the board.
     *
     * @return The opponent
     */
    public abstract Player getOpponent();

    // TODO: Refactor this method, maybe use combinator pattern
    protected Collection<Move> calculateCastles(final Collection<Move> opponentLegalMoves) {

        final List<Move> castles = new ArrayList<>();

        if (king.isFirstMove() || isInCheck() || boardService.getColumn(king.getPosition()) != 4) {
            return ImmutableList.copyOf(castles);
        }

        final var kingPosition = king.getPosition();

        if (board.getTile(kingPosition + 1).getPiece().isEmpty()
                && board.getTile(kingPosition + 2).getPiece().isEmpty()) {

            final var rookTile = board.getTile(kingPosition + 3);

            if (rookTile.getPiece().isPresent() && rookTile.getPiece().get().isFirstMove()
                    && Player.calculateAttacksOnTile(kingPosition + 1, opponentLegalMoves).isEmpty()
                    && Player.calculateAttacksOnTile(kingPosition + 2, opponentLegalMoves).isEmpty()
                    && rookTile.getPiece().get().isRook()) {
                castles.add(new KingSideCastleMove(board, king, kingPosition + 2,
                        (Rook) rookTile.getPiece().get(), kingPosition + 1));
            }
        }

        if (board.getTile(kingPosition - 1).getPiece().isEmpty()
                && board.getTile(kingPosition - 2).getPiece().isEmpty()
                && board.getTile(kingPosition - 3).getPiece().isEmpty()) {
            final var rookTile = board.getTile(kingPosition - 4);

            if (rookTile.getPiece().isPresent() && rookTile.getPiece().get().isFirstMove()
                    && Player.calculateAttacksOnTile(kingPosition - 1, opponentLegalMoves).isEmpty()
                    && Player.calculateAttacksOnTile(kingPosition - 2, opponentLegalMoves).isEmpty()
                    && Player.calculateAttacksOnTile(kingPosition - 3, opponentLegalMoves).isEmpty()
                    && rookTile.getPiece().get().isRook()) {
                castles.add(new QueenSideCastleMove(board, king, kingPosition - 2,
                        (Rook) rookTile.getPiece().get(), kingPosition - 1));
            }
        }

        return ImmutableList.copyOf(castles);
    }
}
