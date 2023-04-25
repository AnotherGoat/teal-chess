/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.player.Color;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class GameHistoryTest {

    @Test
    void startWithNullMove() {
        var history = new GameHistory();

        assertThat(history.lastMove()).isNull();
        assertThat(history.moves()).isEmpty();
    }

    @Test
    void addToHistory() {
        var initialHistory = new GameHistory();

        var initialPosition = Position.INITIAL_POSITION;
        var history = initialHistory.add(new GameMemento(initialPosition));

        assertThat(history).isNotSameAs(initialHistory);
        assertThat(history.lastMove()).isNull();
    }

    @Test
    void moveHistory() {
        var initialHistory = new GameHistory();

        var position1 = Position.INITIAL_POSITION;
        var move = Move.createNormal(new Pawn("e2", Color.WHITE), Coordinate.of("e3"));
        var position2 = new Position(position1.board(), Color.BLACK, position1.castlingRights(), null, 0, 1, move);

        var history1 = initialHistory.add(new GameMemento(position1));
        var history2 = history1.add(new GameMemento(position2));

        assertThat(history2.moves()).isNotSameAs(history1.moves()).hasSize(1);
    }

    @Test
    void unmodifiableMoves() {
        var moves = new GameHistory().moves();

        assertThatThrownBy(moves::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void lastMove() {
        var initialHistory = new GameHistory();

        var position1 = Position.INITIAL_POSITION;
        var move = Move.createNormal(new Pawn("c2", Color.WHITE), Coordinate.of("c3"));
        var position2 = new Position(position1.board(), Color.BLACK, position1.castlingRights(), null, 0, 1, move);

        var finalHistory = initialHistory.add(new GameMemento(position1)).add(new GameMemento(position2));

        assertThat(finalHistory.lastMove()).isEqualTo(move);
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(GameHistory.class).withNonnullFields("history").verify();
    }
}
