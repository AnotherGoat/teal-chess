/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.vmardones.tealchess.move.Move;
import org.jspecify.annotations.Nullable;

public final class GameHistory {

    private final List<GameMemento> history;

    /* equals and hashCode */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var other = (GameHistory) o;
        return history.equals(other.history);
    }

    @Override
    public int hashCode() {
        return Objects.hash(history);
    }

    GameHistory(GameMemento initialState) {
        this(List.of(initialState));
    }

    /* Getters */

    List<Move> moves() {
        return history.stream()
                .map(GameMemento::lastMove)
                .filter(Objects::nonNull)
                .toList();
    }

    GameMemento lastSave() {
        return history.getLast();
    }

    @Nullable Move lastMove() {
        return history.getLast().lastMove();
    }

    GameHistory add(GameMemento state) {
        var newHistory = new ArrayList<>(history);
        newHistory.add(state);
        return new GameHistory(newHistory);
    }

    private GameHistory(List<GameMemento> history) {
        this.history = history;
    }
}
