/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.MoveFinder;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class GameTest {

    @Mock
    MoveMaker moveMaker;

    @Mock
    MoveFinder moveFinder;

    @Test
    void initialPosition() {
        var game = new Game(moveMaker, moveFinder, emptyMap());

        assertThat(game.board()).isEqualTo(Position.INITIAL_POSITION.board());
        assertThat(game.sideToMove()).isEqualTo(Color.WHITE);
    }

    @Test
    void secondPosition() {
        var position1 = Position.INITIAL_POSITION;
        var initialBoard = position1.board();

        var moveMaker = mock(MoveMaker.class);
        var legalMove = mock(LegalMove.class);
        var position2 = FenParser.parse("rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1");

        when(moveMaker.make(position1, legalMove)).thenReturn(position2);

        var game = new Game(moveMaker, moveFinder, emptyMap());
        game.makeMove(legalMove);

        assertThat(game.board()).isNotEqualTo(initialBoard);
        assertThat(game.sideToMove()).isEqualTo(Color.BLACK);
    }

    @Test
    void findlegalDestinations() {
        var game = new Game(moveMaker, moveFinder, emptyMap());
        var knight = game.board().pieceAt(Coordinate.of("g1"));

        var expectedDestinations = new Coordinate[] {Coordinate.of("f3"), Coordinate.of("h3")};

        assertThat(game.findLegalDestinations(knight)).hasSize(2).containsOnlyOnce(expectedDestinations);
    }

    // TODO: Add a method that starts the game from a FEN position, then test finding the black king
    @Test
    void findWhiteKingCoordinate() {
        var game = new Game(moveMaker, moveFinder, emptyMap());
        assertThat(game.kingCoordinate()).isEqualTo(Coordinate.of("e1"));
    }
}
