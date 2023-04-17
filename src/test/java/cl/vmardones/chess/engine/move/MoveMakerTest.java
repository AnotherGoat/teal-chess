/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.parser.FenParser;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Rook;
import cl.vmardones.chess.engine.player.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoveMakerTest {

    MoveMaker moveMaker;

    @BeforeEach
    void setUp() {
        moveMaker = new MoveMaker();
    }

    @Test
    void noDuplicateWhiteKings() {
        var initialPosition = FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - 0 1");
        var whiteKing = initialPosition.board().pieceAt("e1");

        var move = Move.createNormal(whiteKing, whiteKing.coordinate().up(1));
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.board().king(Color.WHITE))
                .isNotEqualTo(initialPosition.board().king(Color.WHITE));
        assertThat(afterMove.board().pieces(Color.WHITE))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(King.class));
    }

    @Test
    void noDuplicateBlackKings() {
        var initialPosition = FenParser.parse("4k3/8/8/8/8/8/8/4K3 b - - 0 1");
        var blackKing = initialPosition.board().pieceAt("e8");

        var move = Move.createNormal(blackKing, blackKing.coordinate().down(1));
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.board().king(Color.BLACK))
                .isNotEqualTo(initialPosition.board().king(Color.BLACK));
        assertThat(afterMove.board().pieces(Color.BLACK))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(King.class));
    }

    @Test
    void makeCaptureMove() {
        var initialPosition = FenParser.parse("r3k3/8/8/8/8/8/8/B3K3 b q - 0 1");
        var attacker = initialPosition.board().pieceAt("a8");
        var capturablePiece = initialPosition.board().pieceAt("a1");

        var move = Move.createCapture(attacker, Coordinate.of("a1"), capturablePiece);
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.board().pieceAt("a1")).isInstanceOf(Rook.class);
        assertThat(afterMove.board().pieces(Color.WHITE)).doesNotContain(capturablePiece);
        assertThat(afterMove.board().pieces(Color.BLACK))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(Rook.class));
    }

    @Test
    void makeCastleMove() {
        var initialPosition = FenParser.parse("4k3/8/8/8/8/8/8/R3K3 w Q - 0 1");
        var king = (King) initialPosition.board().pieceAt("e1");
        var rook = (Rook) initialPosition.board().pieceAt("a1");

        var move = Move.createCastle(false, king, Coordinate.of("c1"), rook, Coordinate.of("d1"));
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.board().pieceAt("c1")).isInstanceOf(King.class);
        assertThat(afterMove.board().pieceAt("d1")).isInstanceOf(Rook.class);
        assertThat(afterMove.board().pieces(Color.WHITE))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(King.class))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(Rook.class));
    }

    @Test
    void markEnPassantTarget() {
        var initialPosition = FenParser.parse("4k3/p7/8/8/8/8/8/4K3 w - - 0 1");
        var pawn = (Pawn) initialPosition.board().pieceAt("a7");

        var move = Move.createDoublePush(pawn, Coordinate.of("a5"));
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.board().pieceAt("a5")).isInstanceOf(Pawn.class);
        assertThat(afterMove.enPassantTarget()).isNotNull();
        assertThat(afterMove.enPassantTarget().coordinate()).isEqualTo(Coordinate.of("a5"));
        assertThat(afterMove.board().pieces(Color.BLACK))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(Pawn.class));
    }
}
