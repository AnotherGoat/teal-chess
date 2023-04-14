/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Color;
import org.eclipse.jdt.annotation.Nullable;

final class PawnMoveGenerator {

    private static final Logger LOG = LogManager.getLogger(PawnMoveGenerator.class);

    private final Board board;
    private final Color activeColor;
    private final List<Piece> pieces;

    PawnMoveGenerator(Board board, Color activeColor, List<Piece> pieces) {
        this.board = board;
        this.pieces = pieces;
        this.activeColor = activeColor;
    }

    Stream<Move> calculatePawnMoves() {
        var moves = new ArrayList<@Nullable Move>();

        for (var piece : pieces) {
            if (piece instanceof Pawn pawn) {
                moves.add(generateJump(pawn));

                if (board.enPassantPawn() != null) {
                    moves.add(generateEnPassant(pawn, true));
                    moves.add(generateEnPassant(pawn, false));
                }
            }
        }

        return moves.stream().filter(Objects::nonNull);
    }

    private @Nullable Move generateJump(Pawn pawn) {
        if (!pawn.firstMove()) {
            return null;
        }

        var forward = pawn.position().up(activeColor.direction());

        if (forward == null || board.pieceAt(forward) != null) {
            return null;
        }

        var destination = forward.up(activeColor.direction());

        if (destination == null || board.pieceAt(destination) != null) {
            return null;
        }

        return Move.createPawnJump(pawn, destination);
    }

    private @Nullable Move generateEnPassant(Pawn pawn, boolean leftSide) {

        var enPassantPawn = board.enPassantPawn();

        if (enPassantPawn == null) {
            return null;
        }

        var direction = leftSide ? -1 : 1;
        var side = pawn.position().right(direction);

        if (side == null) {
            return null;
        }

        var destination = side.up(activeColor.direction());

        if (destination == null || board.pieceAt(destination) != null) {
            return null;
        }

        var sidePiece = board.pieceAt(side);

        if (!(sidePiece instanceof Pawn) || !sidePiece.equals(enPassantPawn)) {
            return null;
        }

        var enPassantMove = Move.createEnPassant(pawn, destination, enPassantPawn);
        LOG.debug("Created en passant move: {}", enPassantMove);
        return enPassantMove;
    }
}
