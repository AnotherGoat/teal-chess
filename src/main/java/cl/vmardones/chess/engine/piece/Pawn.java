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
                return createJumpMove(board, destination);
            }

            return createForwardMove(board, destination);
        }

        if (isEnPassantPossible(board, destination)) {
            LOG.debug("En passant is possible!");
            return createEnPassantMove(board, destination);
        }

        return createCaptureMove(board, destination);
    }

    private Move createEnPassantMove(Board board, Square destination) {
        var enPassantMove = new Move(MoveType.EN_PASSANT, board, this, destination.coordinate(), board.enPassantPawn());

        LOG.debug("Created en passant move: {}", enPassantMove);
        return enPassantMove;
    }

    private boolean isEnPassantPossible(Board board, Square destination) {

        if (board.enPassantPawn() == null) {
            return false;
        }

        var side = destination.coordinate().up(alliance.oppositeDirection());

        if (side == null) {
            return false;
        }

        var pieceAtSide = board.squareAt(side).piece();

        return pieceAtSide != null && pieceAtSide.equals(board.enPassantPawn()) && destination.piece() == null;
    }

    private boolean isNotCapture(Square destination) {
        return position().sameColumnAs(destination.coordinate());
    }

    private @Nullable Move createCaptureMove(Board board, Square destination) {
        var capturablePiece = destination.piece();

        if (capturablePiece != null && isEnemyOf(capturablePiece)) {
            return new Move(MoveType.PAWN_CAPTURE, board, this, destination.coordinate(), capturablePiece);
        }

        return null;
    }

    private Move createJumpMove(Board board, Square destination) {
        return new Move(MoveType.PAWN_JUMP, board, this, destination.coordinate());
    }

    private boolean isJumpPossible(Board board, Square destination) {

        var forward = position.up(alliance.direction());

        if (forward == null) {
            return false;
        }

        return firstMove()
                && board.squareAt(forward).piece() == null
                && destination.piece() == null
                && !destination.equals(board.squareAt(forward));
    }

    private @Nullable Move createForwardMove(Board board, Square destination) {
        if (destination.piece() != null) {
            return null;
        }

        // TODO: Deal with promotions
        return new Move(MoveType.PAWN_NORMAL, board, this, destination.coordinate());
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
