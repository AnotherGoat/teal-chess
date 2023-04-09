/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;

class MoveMakerTest {

    @Test
    void noDuplicateKings() {
        var whiteKing = new King("e1", Alliance.WHITE);
        var blackKing = new King("e8", Alliance.BLACK);

        var initialBoard = Board.builder(whiteKing, blackKing).build();
        var move = Move.createNormal(whiteKing, whiteKing.position().up(1));

        var boardAfterMove = MoveMaker.make(initialBoard, move);

        assertThat(boardAfterMove.whiteKing()).isNotEqualTo(initialBoard.whiteKing());
        assertThat(boardAfterMove.blackKing()).isEqualTo(initialBoard.blackKing());
        assertThat(boardAfterMove.whitePieces())
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(King.class));
        assertThat(boardAfterMove.blackPieces())
                .satisfiesOnlyOnce(piece -> assertThat(piece).isInstanceOf(King.class));
    }
}
