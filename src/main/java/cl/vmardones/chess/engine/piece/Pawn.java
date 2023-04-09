/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Square;
import cl.vmardones.chess.engine.move.*;
import cl.vmardones.chess.engine.player.Alliance;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The pawn piece. It only moves forward (depending on the side) and can eat other pieces
 * diagonally. A very weak piece, but it can be promoted when getting to the last rank at the
 * opposite side.
 */
public final class Pawn extends JumpingPiece {

    private static final Logger LOG = LogManager.getLogger(Pawn.class);

    public Pawn(String position, Alliance alliance) {
        this(position, alliance, true);
    }

    @Override
    public @Nullable Move createMove(Square destination, Board board) {

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

    private @Nullable Move createEnPassantMove(Board board, Square destination) {
        var enPassantPawn = board.enPassantPawn();

        if (enPassantPawn != null) {
            var enPassantMove = Move.createEnPassant(this, destination.position(), enPassantPawn);

            LOG.debug("Created en passant move: {}", enPassantMove);
            return enPassantMove;
        }

        LOG.debug("Not creating en passant move, en passant pawn is null");
        return null;
    }

    private boolean isEnPassantPossible(Board board, Square destination) {

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

    private boolean isJumpPossible(Board board, Square destination) {

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

    @Override
    public Pawn moveTo(String destination) {
        return new Pawn(destination, alliance, false);
    }

    private Pawn(String position, Alliance alliance, boolean firstMove) {
        super(position, alliance, firstMove, generateMoveOffsets(alliance, firstMove));
    }

    private static List<int[]> generateMoveOffsets(Alliance alliance, boolean firstMove) {
        return switch (alliance) {
            case WHITE -> calculateWhiteOffsets(firstMove);
            case BLACK -> calculateBlackOffsets(firstMove);
        };
    }

    private static List<int[]> calculateWhiteOffsets(boolean firstMove) {
        return firstMove ? WHITE_PAWN_MOVES : WHITE_PAWN_MOVES.subList(0, 3);
    }

    private static List<int[]> calculateBlackOffsets(boolean firstMove) {
        return firstMove ? BLACK_PAWN_MOVES : BLACK_PAWN_MOVES.subList(0, 3);
    }
}
