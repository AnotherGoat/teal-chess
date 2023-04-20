/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.vmardones.tealchess.move.Move;
import org.eclipse.jdt.annotation.Nullable;

public final class GameHistory {

    private final List<PositionMemento> history;

    /* Getters */

    public List<Move> moves() {
        return history.stream()
                .map(PositionMemento::state)
                .map(Position::lastMove)
                .filter(Objects::nonNull)
                .toList();
    }

    public @Nullable Move lastMove() {
        if (history.isEmpty()) {
            return null;
        }

        return history.get(history.size() - 1).state().lastMove();
    }

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

    GameHistory() {
        this(new ArrayList<>());
    }

    GameHistory add(PositionMemento state) {
        var newHistory = new ArrayList<>(history);
        newHistory.add(state);
        return new GameHistory(newHistory);
    }

    private GameHistory(List<PositionMemento> history) {
        this.history = history;
    }
}
