/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import java.util.ArrayList;
import java.util.List;

final class GameHistory {

    private final List<TurnMemento> history = new ArrayList<>();

    void add(TurnMemento state) {
        history.add(state);
    }

    TurnMemento get(int index) {
        return history.get(index);
    }
}
