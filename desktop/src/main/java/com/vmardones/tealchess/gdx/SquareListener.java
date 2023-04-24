/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

class SquareListener implements EventListener {
    @Override
    public boolean handle(Event event) {
        if (event instanceof SquareEvent squareEvent) {
            Gdx.app.log("Square", "Clicked " + squareEvent.square().info());
        }

        return false;
    }
}
