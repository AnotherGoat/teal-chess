/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class GameTest {

    @Test
    void initialPosition() {
        var game = new Game(mock(MoveMaker.class));

        assertThat(game.board()).isEqualTo(Position.INITIAL_POSITION.board());
        assertThat(game.player().color()).isEqualTo(Color.WHITE);
        assertThat(game.oppponent().color()).isEqualTo(Color.BLACK);
        assertThat(game.history().lastMove()).isNull();
    }

    @Test
    void secondPosition() {
        var position1 = Position.INITIAL_POSITION;
        var initialBoard = position1.board();

        var moveMaker = mock(MoveMaker.class);
        var legalMove = mock(LegalMove.class);
        var position2 = FenParser.parse("rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1");

        when(moveMaker.make(position1, legalMove)).thenReturn(position2);

        var game = new Game(moveMaker);
        game.makeMove(legalMove);

        assertThat(game.board()).isNotEqualTo(initialBoard);
        assertThat(game.player().color()).isEqualTo(Color.BLACK);
        assertThat(game.oppponent().color()).isEqualTo(Color.WHITE);
        assertThat(game.history().lastMove()).isEqualTo(legalMove);
    }

    @Test
    void findlegalDestinations() {
        var game = new Game(mock(MoveMaker.class));
        var knight = game.board().pieceAt(Coordinate.of("g1"));

        var expectedDestinations = new Coordinate[] {Coordinate.of("f3"), Coordinate.of("h3")};

        assertThat(game.findLegalDestinations(knight)).hasSize(2).containsOnlyOnce(expectedDestinations);
    }

    // TODO: Add a method that starts the game from a FEN position, then test finding the black king
    @Test
    void findWhiteKing() {
        var game = new Game(mock(MoveMaker.class));
        assertThat(game.king()).isEqualTo(game.board().pieceAt(Coordinate.of("e1")));
    }
}
