/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.piece.Bishop;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Rook;
import cl.vmardones.chess.engine.player.Alliance;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MoveTest {

    @Test
    void normalToString() {
        var piece = new Rook("a1", Alliance.WHITE);
        var move = Move.createNormal(piece, Position.of("e1"));

        assertThat(move).hasToString("e1");
    }

    @Test
    void captureToString() {
        var piece = new Bishop("b1", Alliance.WHITE);
        var capturedPiece = new Bishop("c2", Alliance.BLACK);
        var move = Move.createCapture(piece, Position.of("c2"), capturedPiece);

        assertThat(move).hasToString("Bc2");
    }

    @Test
    void pawnCaptureToString() {
        var pawn = new Pawn("a8", Alliance.BLACK);
        var capturedPawn = new Pawn("b7", Alliance.WHITE);
        var move = Move.createCapture(pawn, Position.of("b7"), capturedPawn);

        assertThat(move).hasToString("axb7");
    }

    @Test
    void kingCastleToString() {
        var king = new King("e5", Alliance.WHITE);
        var rook = new Rook("e8", Alliance.WHITE);
        var move = Move.createCastle(true, king, Position.of("e7"), rook, Position.of("e6"));

        assertThat(move).hasToString("0-0");
    }

    @Test
    void queenCastleToString() {
        var king = new King("e5", Alliance.WHITE);
        var rook = new Rook("e1", Alliance.WHITE);
        var move = Move.createCastle(false, king, Position.of("e3"), rook, Position.of("e4"));

        assertThat(move).hasToString("0-0-0");
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Move.class)
                .withNonnullFields("type", "piece", "destination", "result")
                .verify();
    }
}
