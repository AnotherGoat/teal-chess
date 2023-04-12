package cl.vmardones.chess.engine.analysis;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Square;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Piece;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Collections;
import java.util.List;

public class MoveFactory {

    private final Board board;
    private final List<Move> opponentLegals;

    public MoveFactory(Board board) {
        this(board, Collections.emptyList());
    }

    public MoveFactory(Board board, List<Move> opponentLegals) {
        this.board = board;
        this.opponentLegals = opponentLegals;
    }

    /**
     * Creates a move, based on the piece and the destination.
     *
     * @param destination The destination square.
     * @param piece The piece to move.
     * @return A move, selected depending on the source and destination.
     */
    public @Nullable Move createMove(Piece piece, Square destination) {
        var destinationPiece = destination.piece();

        if (destinationPiece == null) {
            return Move.createNormal(piece, destination.position());
        }

        if (piece.isAllyOf(destinationPiece)) {
            return null;
        }

        return Move.createCapture(piece, destination.position(), destinationPiece);
    }

    public @Nullable Move createMove(Pawn pawn, Square destination) {

        if (isNotCapture(destination)) {
            if (isJumpPossible(board, destination)) {
                return createJumpMove(destination);
            }

            return createForwardMove(destination);
        }

        if (isEnPassantPossible(board, destination)) {
            LOG.debug("En passant is possible!");

            return createEnPassantMove(board, destination);
        }

        return createCaptureMove(destination);
    }

    public @Nullable Move createMove(King king, Square destination) {
        var destinationPiece = destination.piece();

        if (destinationPiece == null) {

            if (destination.position().equals(king.position().right(2))) {
                return new CastleChecker(board, king.alliance(), opponentLegals).generateCastleMove(true);
            }

            if (king.position())



            return Move.createNormal(piece, destination.position());
        }

        if (piece.isAllyOf(destinationPiece)) {
            return null;
        }

        return Move.createCapture(piece, destination.position(), destinationPiece);
    }
}
