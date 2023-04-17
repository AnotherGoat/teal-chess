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
    private final Color sideToMove;
    private final List<Piece> pieces;
    private final @Nullable Pawn enPassantPawn;

    PawnMoveGenerator(Board board, Color sideToMove, List<Piece> pieces, @Nullable Pawn enPassantPawn) {
        this.board = board;
        this.pieces = pieces;
        this.sideToMove = sideToMove;
        this.enPassantPawn = enPassantPawn;
    }

    Stream<Move> calculatePawnMoves() {
        var moves = new ArrayList<@Nullable Move>();

        for (var piece : pieces) {
            if (piece.isPawn()) {
                var pawn = (Pawn) piece;
                moves.add(generateDoublePush(pawn));

                if (enPassantPawn != null) {
                    moves.add(generateEnPassant(pawn, true));
                    moves.add(generateEnPassant(pawn, false));
                }
            }
        }

        return moves.stream().filter(Objects::nonNull);
    }

    private @Nullable Move generateDoublePush(Pawn pawn) {
        if (!pawn.firstMove()) {
            return null;
        }

        var forward = pawn.coordinate().up(sideToMove.direction());

        if (forward == null || board.pieceAt(forward) != null) {
            return null;
        }

        var destination = forward.up(sideToMove.direction());

        if (destination == null || board.pieceAt(destination) != null) {
            return null;
        }

        return Move.createDoublePush(pawn, destination);
    }

    private @Nullable Move generateEnPassant(Pawn pawn, boolean leftSide) {

        var direction = leftSide ? -1 : 1;
        var side = pawn.coordinate().right(direction);

        if (side == null) {
            return null;
        }

        var destination = side.up(sideToMove.direction());

        if (destination == null || board.pieceAt(destination) != null) {
            return null;
        }

        var sidePiece = board.pieceAt(side);

        if (sidePiece == null || !sidePiece.isPawn() || !sidePiece.equals(enPassantPawn)) {
            return null;
        }

        var enPassantMove = Move.createEnPassant(pawn, destination, enPassantPawn);
        LOG.debug("Created en passant move: {}", enPassantMove);
        return enPassantMove;
    }
}
