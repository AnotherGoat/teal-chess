/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import static org.assertj.core.api.Assertions.*;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveResult;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class GameTest {

    @Test
    void initialPosition() {
        var game = new Game();

        assertThat(game.board()).isEqualTo(Position.INITIAL_POSITION.board());
        assertThat(game.player().color()).isEqualTo(Color.WHITE);
        assertThat(game.oppponent().color()).isEqualTo(Color.BLACK);
        assertThat(game.history().lastMove()).isNull();
    }

    @Test
    void secondPosition() {
        var game = new Game();

        var initialBoard = game.board();

        var piece = new Pawn("d4", Color.WHITE);
        var move = Move.builder(piece, Coordinate.of("d5")).normal().makeLegal(MoveResult.CONTINUE);

        game.updatePosition(move);

        assertThat(game.board()).isNotEqualTo(initialBoard);
        assertThat(game.player().color()).isEqualTo(Color.BLACK);
        assertThat(game.oppponent().color()).isEqualTo(Color.WHITE);
        assertThat(game.history().lastMove()).isEqualTo(move);
    }

    @Test
    void findlegalDestinations() {
        var game = new Game();
        var knight = game.board().pieceAt("g1");

        var expectedDestinations = new Coordinate[] {Coordinate.of("f3"), Coordinate.of("h3")};

        assertThat(game.findLegalDestinations(knight)).hasSize(2).containsOnlyOnce(expectedDestinations);
    }
}
