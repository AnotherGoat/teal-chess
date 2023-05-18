/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import static com.vmardones.tealchess.color.Color.BLACK;
import static com.vmardones.tealchess.color.Color.WHITE;
import static com.vmardones.tealchess.move.Move.*;
import static com.vmardones.tealchess.move.MoveType.*;
import static com.vmardones.tealchess.piece.PieceType.*;
import static com.vmardones.tealchess.square.Square.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.PromotionChoice;
import org.junit.jupiter.api.Test;

final class MoveMakerTest {

    MoveMaker moveMaker = new MoveMaker();

    @Test
    void makeNormalMove() {
        var position = FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - 0 1");
        var move = new Move(NORMAL, e1, e2);
        var postMoveBoard = moveMaker.make(position, move).board();

        assertThat(postMoveBoard.pieceAt(e1)).isNull();
        assertThat(postMoveBoard.pieceAt(e2)).isEqualTo(new Piece(KING, WHITE, e2));
    }

    @Test
    void makeCaptureMove() {
        var position = FenParser.parse("r3k3/8/8/8/8/8/8/B3K3 b - - 0 1");
        var move = new Move(CAPTURE, a8, a1);
        var postMoveBoard = moveMaker.make(position, move).board();

        assertThat(postMoveBoard.pieceAt(a8)).isNull();
        assertThat(postMoveBoard.pieceAt(a1)).isEqualTo(new Piece(ROOK, BLACK, a1));
    }

    @Test
    void makeWhiteKingSideCastle() {
        var position = FenParser.parse("4k3/8/8/8/8/8/8/4K2R w K - 0 1");
        var postMoveBoard =
                moveMaker.make(position, WHITE_KING_SIDE_CASTLE.get(0)).board();

        assertThat(postMoveBoard.pieceAt(e1)).isNull();
        assertThat(postMoveBoard.pieceAt(g1)).isEqualTo(new Piece(KING, WHITE, g1));
        assertThat(postMoveBoard.pieceAt(h1)).isNull();
        assertThat(postMoveBoard.pieceAt(f1)).isEqualTo(new Piece(ROOK, WHITE, f1));
    }

    @Test
    void makeWhiteQueenSideCastle() {
        var position = FenParser.parse("4k3/8/8/8/8/8/8/R3K3 w Q - 0 1");
        var postMoveBoard =
                moveMaker.make(position, WHITE_QUEEN_SIDE_CASTLE.get(0)).board();

        assertThat(postMoveBoard.pieceAt(e1)).isNull();
        assertThat(postMoveBoard.pieceAt(c1)).isEqualTo(new Piece(KING, WHITE, c1));
        assertThat(postMoveBoard.pieceAt(a1)).isNull();
        assertThat(postMoveBoard.pieceAt(d1)).isEqualTo(new Piece(ROOK, WHITE, d1));
    }

    @Test
    void makeBlackKingSideCastle() {
        var position = FenParser.parse("4k2r/8/8/8/8/8/8/4K3 b k - 0 1");
        var postMoveBoard =
                moveMaker.make(position, BLACK_KING_SIDE_CASTLE.get(0)).board();

        assertThat(postMoveBoard.pieceAt(e8)).isNull();
        assertThat(postMoveBoard.pieceAt(g8)).isEqualTo(new Piece(KING, BLACK, g8));
        assertThat(postMoveBoard.pieceAt(h8)).isNull();
        assertThat(postMoveBoard.pieceAt(f8)).isEqualTo(new Piece(ROOK, BLACK, f8));
    }

    @Test
    void makeBlackQueenSideCastle() {
        var position = FenParser.parse("r3k3/8/8/8/8/8/8/4K3 b q - 0 1");
        var postMoveBoard =
                moveMaker.make(position, WHITE_QUEEN_SIDE_CASTLE.get(0)).board();

        assertThat(postMoveBoard.pieceAt(e8)).isNull();
        assertThat(postMoveBoard.pieceAt(c8)).isEqualTo(new Piece(KING, BLACK, c8));
        assertThat(postMoveBoard.pieceAt(a8)).isNull();
        assertThat(postMoveBoard.pieceAt(d8)).isEqualTo(new Piece(ROOK, BLACK, d8));
    }

    @Test
    void makeNormalPromotion() {
        var position = FenParser.parse("4k3/P7/8/8/8/8/8/4K3 w - - 0 1");
        var move = new Move(PAWN_PUSH, a7, a8, PromotionChoice.BISHOP);
        var postMoveBoard = moveMaker.make(position, move).board();

        assertThat(postMoveBoard.pieceAt(a7)).isNull();
        assertThat(postMoveBoard.pieceAt(a8)).isEqualTo(new Piece(BISHOP, WHITE, a8));
    }

    @Test
    void makeCapturePromotion() {
        var position = FenParser.parse("1b2k3/2P5/8/8/8/8/8/4K3 w - - 0 1");
        var move = new Move(PAWN_CAPTURE, c7, b8, PromotionChoice.QUEEN);
        var postMoveBoard = moveMaker.make(position, move).board();

        assertThat(postMoveBoard.pieceAt(c7)).isNull();
        assertThat(postMoveBoard.pieceAt(b8)).isEqualTo(new Piece(QUEEN, WHITE, b8));
    }

    // TODO: Test making en passant moves
    @Test
    void markWhiteEnPassantTarget() {
        var position = FenParser.parse("4k3/8/8/8/8/8/P7/4K3 w - - 0 1");
        var move = new Move(DOUBLE_PUSH, a2, a4);
        var postMove = moveMaker.make(position, move);

        assertThat(postMove.board().pieceAt(a2)).isNull();
        assertThat(postMove.board().pieceAt(a4).isPawn()).isTrue();
        assertThat(postMove.enPassantTarget()).isNotNull().isEqualTo(a3);
    }

    @Test
    void markBlackEnPassantTarget() {
        var position = FenParser.parse("4k3/p7/8/8/8/8/8/4K3 b - - 0 1");
        var move = new Move(DOUBLE_PUSH, a7, a5);
        var postMove = moveMaker.make(position, move);

        assertThat(postMove.board().pieceAt(a7)).isNull();
        assertThat(postMove.board().pieceAt(a5).isPawn()).isTrue();
        assertThat(postMove.enPassantTarget()).isNotNull().isEqualTo(a6);
    }

    @Test
    void keepCastlingRights() {
        var position = FenParser.parse("4k3/8/8/8/8/3N4/8/4K3 w KQkq - 0 1");
        var move = new Move(NORMAL, d3, b4);
        var postMove = moveMaker.make(position, move);

        assertThat(postMove.castlingRights()).isEqualTo(position.castlingRights());
    }

    @Test
    void loseWhiteCastles() {
        var position = FenParser.parse("4k3/8/8/8/8/8/8/4K3 w KQkq - 0 1");
        var move = new Move(NORMAL, e1, e2);
        var postMove = moveMaker.make(position, move);

        assertThat(postMove.castlingRights().whiteKingSide()).isFalse();
        assertThat(postMove.castlingRights().whiteQueenSide()).isFalse();
    }

    @Test
    void loseBlackCastles() {
        var position = FenParser.parse("4k3/8/8/8/8/8/8/4K3 b KQkq - 0 1");
        var move = new Move(NORMAL, e8, e7);
        var postMove = moveMaker.make(position, move);

        assertThat(postMove.castlingRights().blackKingSide()).isFalse();
        assertThat(postMove.castlingRights().blackQueenSide()).isFalse();
    }

    @Test
    void loseWhiteKingSideCastle() {
        var position = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        var move = new Move(NORMAL, h1, h3);
        var postMove = moveMaker.make(position, move);

        assertThat(postMove.castlingRights().whiteKingSide()).isFalse();
        assertThat(postMove.castlingRights().blackKingSide()).isTrue();
    }

    @Test
    void loseWhiteQueenSideCastle() {
        var position = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        var move = new Move(NORMAL, a1, d1);
        var postMove = moveMaker.make(position, move);

        assertThat(postMove.castlingRights().whiteQueenSide()).isFalse();
        assertThat(postMove.castlingRights().blackQueenSide()).isTrue();
    }

    @Test
    void loseBlackKingSideCastle() {
        var position = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1");
        var move = new Move(NORMAL, h8, g8);
        var postMove = moveMaker.make(position, move);

        assertThat(postMove.castlingRights().whiteKingSide()).isTrue();
        assertThat(postMove.castlingRights().blackKingSide()).isFalse();
    }

    @Test
    void loseBlackQueenSideCastle() {
        var position = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1");
        var move = new Move(NORMAL, a8, a6);
        var postMove = moveMaker.make(position, move);

        assertThat(postMove.castlingRights().whiteQueenSide()).isTrue();
        assertThat(postMove.castlingRights().blackQueenSide()).isFalse();
    }

    @Test
    void keepOtherRookCastlingRights() {
        var position = FenParser.parse("r3k3/8/8/8/5r2/8/8/4K3 b KQq - 0 1");
        var move = new Move(NORMAL, f4, a4);
        var postMove = moveMaker.make(position, move);

        assertThat(postMove.castlingRights()).isEqualTo(position.castlingRights());
    }
}
