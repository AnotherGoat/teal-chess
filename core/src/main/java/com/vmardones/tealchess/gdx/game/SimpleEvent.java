/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import com.badlogic.gdx.scenes.scene2d.Event;

final class SimpleEvent extends Event {

    private final EventType type;

    SimpleEvent(EventType type) {
        this.type = type;
    }

    EventType type() {
        return type;
    }
}
