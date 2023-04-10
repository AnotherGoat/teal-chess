/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class GameHistory {

    private final List<TurnMemento> history;

    /* Getters */

    public List<TurnMemento> history() {
        return history;
    }

    public TurnMemento get(int index) {
        return history.get(index);
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
        history = new ArrayList<>();
    }

    GameHistory add(TurnMemento state) {
        var newHistory = new ArrayList<>(history);
        newHistory.add(state);
        return new GameHistory(newHistory);
    }

    private GameHistory(List<TurnMemento> history) {
        this.history = history;
    }
}
