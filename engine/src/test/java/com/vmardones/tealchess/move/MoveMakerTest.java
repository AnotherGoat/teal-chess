/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.piece.*;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class MoveMakerTest {

    MoveMaker moveMaker;

    @BeforeEach
    void setUp() {
        moveMaker = new MoveMaker();
    }

    @Test
    void noDuplicateWhiteKings() {
        var initialPosition = FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - 0 1");
        var whiteKing = initialPosition.board().pieceAt("e1");

        var move = Move.builder(whiteKing, whiteKing.coordinate().up(1)).normal();
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

        var move = Move.builder(blackKing, blackKing.coordinate().down(1)).normal();
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

        var move = Move.builder(attacker, Coordinate.of("a1")).capture(capturablePiece);
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.board().pieceAt("a1")).isInstanceOf(Rook.class);
        assertThat(afterMove.board().pieces(Color.WHITE)).doesNotContain(capturablePiece);
        assertThat(afterMove.board().pieces(Color.BLACK))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(Rook.class));
    }

    @Test
    void makeWhiteKingSideCastle() {
        var initialPosition = FenParser.parse("4k3/8/8/8/8/8/8/4K2R w K - 0 1");
        var king = (King) initialPosition.board().pieceAt("e1");
        var rook = (Rook) initialPosition.board().pieceAt("h1");

        var move = Move.builder(king, Coordinate.of("g1")).castle(true, rook, Coordinate.of("f1"));
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.board().pieceAt("g1")).isInstanceOf(King.class);
        assertThat(afterMove.board().pieceAt("f1")).isInstanceOf(Rook.class);
        assertThat(afterMove.board().pieces(Color.WHITE))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(King.class))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(Rook.class));
    }

    @Test
    void makeWhiteQueenSideCastle() {
        var initialPosition = FenParser.parse("4k3/8/8/8/8/8/8/R3K3 w Q - 0 1");
        var king = (King) initialPosition.board().pieceAt("e1");
        var rook = (Rook) initialPosition.board().pieceAt("a1");

        var move = Move.builder(king, Coordinate.of("c1")).castle(false, rook, Coordinate.of("d1"));
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.board().pieceAt("c1")).isInstanceOf(King.class);
        assertThat(afterMove.board().pieceAt("d1")).isInstanceOf(Rook.class);
        assertThat(afterMove.board().pieces(Color.WHITE))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(King.class))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(Rook.class));
    }

    @Test
    void makeBlackKingSideCastle() {
        var initialPosition = FenParser.parse("4k2r/8/8/8/8/8/8/4K3 b k - 0 1");
        var king = (King) initialPosition.board().pieceAt("e8");
        var rook = (Rook) initialPosition.board().pieceAt("h8");

        var move = Move.builder(king, Coordinate.of("g8")).castle(true, rook, Coordinate.of("f8"));
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.board().pieceAt("g8")).isInstanceOf(King.class);
        assertThat(afterMove.board().pieceAt("f8")).isInstanceOf(Rook.class);
        assertThat(afterMove.board().pieces(Color.BLACK))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(King.class))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(Rook.class));
    }

    @Test
    void makeBlackQueenSideCastle() {
        var initialPosition = FenParser.parse("r3k3/8/8/8/8/8/8/4K3 b q - 0 1");
        var king = (King) initialPosition.board().pieceAt("e8");
        var rook = (Rook) initialPosition.board().pieceAt("a8");

        var move = Move.builder(king, Coordinate.of("c8")).castle(false, rook, Coordinate.of("d8"));
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.board().pieceAt("c8")).isInstanceOf(King.class);
        assertThat(afterMove.board().pieceAt("d8")).isInstanceOf(Rook.class);
        assertThat(afterMove.board().pieces(Color.BLACK))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(King.class))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(Rook.class));
    }

    @Test
    void makePromotion() {
        var initialPosition = FenParser.parse("4k3/P7/8/8/8/8/8/4K3 w - - 0 1");

        var pawn = (Pawn) initialPosition.board().pieceAt("a7");
        var move = Move.builder(pawn, Coordinate.of("a8")).normal().makePromotion(PromotionChoice.BISHOP);
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.board().pieceAt("a8")).isInstanceOf(Bishop.class);
    }

    @Test
    void markWhiteEnPassantTarget() {
        var initialPosition = FenParser.parse("4k3/8/8/8/8/8/P7/4K3 w - - 0 1");
        var pawn = (Pawn) initialPosition.board().pieceAt("a2");

        var move = Move.builder(pawn, Coordinate.of("a4")).doublePush();
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.board().pieceAt("a4")).isInstanceOf(Pawn.class);
        assertThat(afterMove.enPassantTarget()).isNotNull();
        assertThat(afterMove.enPassantTarget().coordinate()).isEqualTo(Coordinate.of("a3"));
        assertThat(afterMove.board().pieces(Color.WHITE))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(Pawn.class));
    }

    @Test
    void markBlackEnPassantTarget() {
        var initialPosition = FenParser.parse("4k3/p7/8/8/8/8/8/4K3 b - - 0 1");
        var pawn = (Pawn) initialPosition.board().pieceAt("a7");

        var move = Move.builder(pawn, Coordinate.of("a5")).doublePush();
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.board().pieceAt("a5")).isInstanceOf(Pawn.class);
        assertThat(afterMove.enPassantTarget()).isNotNull();
        assertThat(afterMove.enPassantTarget().coordinate()).isEqualTo(Coordinate.of("a6"));
        assertThat(afterMove.board().pieces(Color.BLACK))
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(Pawn.class));
    }

    @Test
    void keepCastlingRights() {
        var initialPosition = FenParser.parse("4k3/8/8/8/8/3N4/8/4K3 w KQkq - 0 1");
        var knight = initialPosition.board().pieceAt("d3");

        var move = Move.builder(knight, Coordinate.of("b4")).normal();
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.castlingRights()).isEqualTo(initialPosition.castlingRights());
    }

    @Test
    void loseWhiteCastles() {
        var initialPosition = FenParser.parse("4k3/8/8/8/8/8/8/4K3 w KQkq - 0 1");
        var king = initialPosition.board().pieceAt("e1");

        var move = Move.builder(king, Coordinate.of("e2")).normal();
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.castlingRights().whiteKingSide()).isFalse();
        assertThat(afterMove.castlingRights().whiteQueenSide()).isFalse();
    }

    @Test
    void loseBlackCastles() {
        var initialPosition = FenParser.parse("4k3/8/8/8/8/8/8/4K3 b KQkq - 0 1");
        var king = initialPosition.board().pieceAt("e8");

        var move = Move.builder(king, Coordinate.of("e7")).normal();
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.castlingRights().blackKingSide()).isFalse();
        assertThat(afterMove.castlingRights().blackQueenSide()).isFalse();
    }

    @Test
    void loseWhiteKingSideCastle() {
        var initialPosition = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        var rook = initialPosition.board().pieceAt("h1");

        var move = Move.builder(rook, Coordinate.of("h3")).normal();
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.castlingRights().whiteKingSide()).isFalse();
        assertThat(afterMove.castlingRights().blackKingSide()).isTrue();
    }

    @Test
    void loseWhiteQueenSideCastle() {
        var initialPosition = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        var rook = initialPosition.board().pieceAt("a1");

        var move = Move.builder(rook, Coordinate.of("d1")).normal();
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.castlingRights().whiteQueenSide()).isFalse();
        assertThat(afterMove.castlingRights().blackQueenSide()).isTrue();
    }

    @Test
    void loseBlackKingSideCastle() {
        var initialPosition = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1");
        var rook = initialPosition.board().pieceAt("h8");

        var move = Move.builder(rook, Coordinate.of("g8")).normal();
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.castlingRights().whiteKingSide()).isTrue();
        assertThat(afterMove.castlingRights().blackKingSide()).isFalse();
    }

    @Test
    void loseBlackQueenSideCastle() {
        var initialPosition = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1");
        var rook = initialPosition.board().pieceAt("a8");

        var move = Move.builder(rook, Coordinate.of("a6")).normal();
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.castlingRights().whiteQueenSide()).isTrue();
        assertThat(afterMove.castlingRights().blackQueenSide()).isFalse();
    }

    @Test
    void keepOtherRookCastlingRights() {
        var initialPosition = FenParser.parse("r3k3/8/8/8/5r2/8/8/4K3 b KQq - 0 1");
        var rook = initialPosition.board().pieceAt("f4");

        var move = Move.builder(rook, Coordinate.of("a4")).normal();
        var afterMove = moveMaker.make(initialPosition, move);

        assertThat(afterMove.castlingRights()).isEqualTo(initialPosition.castlingRights());
    }
}
