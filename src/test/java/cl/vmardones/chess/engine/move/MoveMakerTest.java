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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoveMakerTest {

    MoveMaker moveMaker;
    King whiteKing;
    King blackKing;

    @BeforeEach
    void setUp() {
        moveMaker = new MoveMaker();
        whiteKing = new King("e1", Alliance.WHITE);
        blackKing = new King("e8", Alliance.BLACK);
    }

    @Test
    void noDuplicateWhiteKings() {
        var initialBoard = Board.builder(whiteKing, blackKing).build();
        var move = Move.createNormal(whiteKing, whiteKing.position().up(1));

        var boardAfterMove = moveMaker.make(initialBoard, move);

        assertThat(boardAfterMove.whiteKing()).isNotEqualTo(initialBoard.whiteKing());
        assertThat(boardAfterMove.whitePieces())
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(King.class));
    }

    @Test
    void noDuplicateBlackKings() {
        var initialBoard = Board.builder(whiteKing, blackKing).build();
        var move = Move.createNormal(blackKing, blackKing.position().down(1));

        var boardAfterMove = moveMaker.make(initialBoard, move);

        assertThat(boardAfterMove.blackKing()).isNotEqualTo(initialBoard.blackKing());
        assertThat(boardAfterMove.blackPieces())
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(King.class));
    }

    @Test
    void makeCaptureMove() {
        var attacker = new Rook("a8", Alliance.BLACK);
        var capturablePiece = new Bishop("a1", Alliance.WHITE);
        var initialBoard = Board.builder(whiteKing, blackKing)
                .with(attacker)
                .with(capturablePiece)
                .build();

        var move = Move.createCapture(attacker, Position.of("a1"), capturablePiece);

        var boardAfterMove = moveMaker.make(initialBoard, move);

        assertThat(boardAfterMove.pieceAt("a1")).isInstanceOf(Rook.class);
        assertThat(boardAfterMove.whitePieces()).doesNotContain(capturablePiece);
        assertThat(boardAfterMove.blackPieces())
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(Rook.class));
    }

    @Test
    void makeCastleMove() {
        var rook = new Rook("a1", Alliance.WHITE);
        var initialBoard = Board.builder(whiteKing, blackKing).with(rook).build();

        var move = Move.createCastle(false, whiteKing, Position.of("c1"), rook, Position.of("d1"));

        var boardAfterMove = moveMaker.make(initialBoard, move);

        assertThat(boardAfterMove.pieceAt("c1")).isInstanceOf(King.class);
        assertThat(boardAfterMove.pieceAt("d1")).isInstanceOf(Rook.class);
        assertThat(boardAfterMove.whitePieces())
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(King.class))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(Rook.class));
    }

    @Test
    void markEnPassantPawn() {
        var pawn = new Pawn("a7", Alliance.BLACK);
        var initialBoard = Board.builder(whiteKing, blackKing).with(pawn).build();

        var move = Move.createPawnJump(pawn, Position.of("a5"));

        var boardAfterMove = moveMaker.make(initialBoard, move);

        assertThat(boardAfterMove.pieceAt("a5")).isInstanceOf(Pawn.class);
        assertThat(boardAfterMove.enPassantPawn().position()).isEqualTo(Position.of("a5"));
        assertThat(boardAfterMove.blackPieces())
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(Pawn.class));
    }
}
