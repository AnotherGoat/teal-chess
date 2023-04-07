/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.piece.Bishop;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Rook;
import cl.vmardones.chess.engine.player.Alliance;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MoveTest {

    @Mock
    Board board;

    @Test
    void normalToString() {
        var piece = new Pawn("a1", Alliance.WHITE);
        var move = new Move(MoveType.NORMAL, board, piece, Position.of("e5"));

        assertThat(move).hasToString("e5");
    }

    @Test
    void captureToString() {
        var piece = new Bishop("b1", Alliance.WHITE);
        var capturedPiece = new Bishop("c2", Alliance.BLACK);
        var move = new Move(MoveType.CAPTURE, board, piece, Position.of("c2"), capturedPiece);

        assertThat(move).hasToString("Bc2");
    }

    @Test
    void pawnCaptureToString() {
        var pawn = new Pawn("a8", Alliance.BLACK);
        var capturedPawn = new Pawn("b7", Alliance.WHITE);
        var move = new Move(MoveType.PAWN_CAPTURE, board, pawn, Position.of("b7"), capturedPawn);

        assertThat(move).hasToString("axb7");
    }

    @Test
    void kingCastleToString() {
        var piece = new King("e5", Alliance.WHITE);
        var rook = new Rook("e8", Alliance.WHITE);
        var move = new Move(MoveType.KING_CASTLE, board, piece, Position.of("e7"), rook, Position.of("e6"));

        assertThat(move).hasToString("0-0");
    }

    @Test
    void queenCastleToString() {
        var piece = new King("e5", Alliance.WHITE);
        var rook = new Rook("e1", Alliance.WHITE);
        var move = new Move(MoveType.QUEEN_CASTLE, board, piece, Position.of("e3"), rook, Position.of("e4"));

        assertThat(move).hasToString("0-0-0");
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Move.class)
                .withNonnullFields("type", "board", "piece", "destination")
                .verify();
    }
}
