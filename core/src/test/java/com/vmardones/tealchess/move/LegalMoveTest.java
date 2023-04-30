/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.piece.Knight;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.piece.Queen;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class LegalMoveTest {

    @Test
    void checkmateHash() {
        var queen = new Queen("a3", Color.WHITE);
        var move = Move.builder(queen, Coordinate.of("e7")).normal();
        var legalMove = move.makeLegal(MoveResult.CHECKMATES);

        assertThat(legalMove.san()).isEqualTo("Qe7#");
    }

    @Test
    void sanIsToString() {
        var piece = new Pawn("c7", Color.BLACK);
        var legalMove = Move.builder(piece, Coordinate.of("c6")).normal().makeLegal(MoveResult.CONTINUE);

        assertThat(legalMove.san()).isEqualTo(legalMove.toString());
    }

    @Test
    void fileDisambiguation() {
        var piece = new Knight("b3", Color.WHITE);
        var legalMove =
                Move.builder(piece, Coordinate.of("c5")).normal().makeLegal(MoveResult.CONTINUE, Disambiguation.FILE);

        assertThat(legalMove.san()).isEqualTo("Nbc5");
    }

    @Test
    void rankDisambiguation() {
        var piece = new Knight("b3", Color.WHITE);
        var legalMove =
                Move.builder(piece, Coordinate.of("c5")).normal().makeLegal(MoveResult.CONTINUE, Disambiguation.RANK);

        assertThat(legalMove.san()).isEqualTo("N3c5");
    }

    @Test
    void fullDisambiguation() {
        var piece = new Knight("b3", Color.WHITE);
        var legalMove =
                Move.builder(piece, Coordinate.of("c5")).normal().makeLegal(MoveResult.CONTINUE, Disambiguation.FULL);

        assertThat(legalMove.san()).isEqualTo("Nb3c5");
    }
}
