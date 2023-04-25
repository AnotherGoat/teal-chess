/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.vmardones.tealchess.move.LegalMove;
import org.eclipse.jdt.annotation.Nullable;

public final class GameHistory {

    private final List<GameMemento> history;

    /* Getters */

    public List<LegalMove> moves() {
        return history.stream()
                .map(GameMemento::lastMove)
                .filter(Objects::nonNull)
                .toList();
    }

    public @Nullable LegalMove lastMove() {
        if (history.isEmpty()) {
            return null;
        }

        return history.get(history.size() - 1).lastMove();
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

    GameHistory add(GameMemento state) {
        var newHistory = new ArrayList<>(history);
        newHistory.add(state);
        return new GameHistory(newHistory);
    }

    private GameHistory(List<GameMemento> history) {
        this.history = history;
    }
}
