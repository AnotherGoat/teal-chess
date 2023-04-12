package cl.vmardones.chess.engine.analysis;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Square;
import cl.vmardones.chess.engine.move.Move;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.annotation.Nullable;

final class PawnMoveChecker {

    private static final Logger LOG = LogManager.getLogger(PawnMoveChecker.class);

    private final Board board;

    public PawnMoveChecker(Board board) {
        this.board = board;
    }

    private @Nullable Move createEnPassantMove(Square destination) {
        var enPassantPawn = board.enPassantPawn();

        if (enPassantPawn != null) {
            var enPassantMove = Move.createEnPassant(this, destination.position(), enPassantPawn);

            LOG.debug("Created en passant move: {}", enPassantMove);
            return enPassantMove;
        }

        LOG.debug("Not creating en passant move, en passant pawn is null");
        return null;
    }

    private boolean isEnPassantPossible(Square destination) {

        if (board.enPassantPawn() == null) {
            return false;
        }

        var side = destination.position().up(alliance.oppositeDirection());

        if (side == null) {
            return false;
        }

        var pieceAtSide = board.pieceAt(side);

        return pieceAtSide != null && pieceAtSide.equals(board.enPassantPawn()) && destination.piece() == null;
    }

    private boolean isNotCapture(Square destination) {
        return position().sameFileAs(destination.position());
    }

    private @Nullable Move createCaptureMove(Square destination) {
        var capturablePiece = destination.piece();

        if (capturablePiece != null && isEnemyOf(capturablePiece)) {
            return Move.createCapture(this, destination.position(), capturablePiece);
        }

        return null;
    }

    private Move createJumpMove(Square destination) {
        return Move.createPawnJump(this, destination.position());
    }

    private boolean isJumpPossible(Square destination) {

        var forward = position.up(alliance.direction());

        if (forward == null) {
            return false;
        }

        return firstMove()
                && board.pieceAt(forward) == null
                && destination.piece() == null
                && !destination.equals(board.squareAt(forward));
    }

    private @Nullable Move createForwardMove(Square destination) {
        if (destination.piece() != null) {
            return null;
        }

        // TODO: Deal with promotions
        return Move.createNormal(this, destination.position());
    }
}
