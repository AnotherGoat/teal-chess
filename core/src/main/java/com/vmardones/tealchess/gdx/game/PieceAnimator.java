/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.vmardones.tealchess.io.settings.SettingManager;
import com.vmardones.tealchess.move.Move;
import org.eclipse.jdt.annotation.Nullable;

final class PieceAnimator extends Actor {

    private static final Event NEXT_TURN = new SimpleEvent(EventType.NEXT_TURN);
    private final SettingManager settings;
    private final Chessboard board;
    private @Nullable Image moveAnimation;
    private @Nullable Image castleAnimation;

    PieceAnimator(SettingManager settings, Chessboard board) {
        this.settings = settings;
        this.board = board;
    }

    @Nullable Image animateMove(Move move) {
        var sourceSquare = board.squareAt(move.source());
        var sprite = sourceSquare.sprite();

        if (sprite == null) {
            return null;
        }

        sourceSquare.removeSprite();
        var x1 = board.getX() + sourceSquare.getX();
        var y1 = board.getY() + sourceSquare.getY();

        var destinationSquare = board.squareAt(move.destination());
        var x2 = board.getX() + destinationSquare.getX();
        var y2 = board.getY() + destinationSquare.getY();

        moveAnimation = new Image(sprite);
        moveAnimation.setPosition(x1, y1);

        var slide = Actions.moveTo(x2, y2, settings.animationDuration(), Interpolation.smoother);
        var resumeGame = Actions.run(() -> {
            moveAnimation = null;
            fire(NEXT_TURN);
        });
        var fullAction = Actions.sequence(slide, resumeGame, new RemoveActorAction());

        moveAnimation.addAction(fullAction);
        return moveAnimation;
    }

    @Nullable Image animateCastle(Move castle) {
        var sourceSquare = board.squareAt(castle.source());
        var sprite = sourceSquare.sprite();

        if (sprite == null) {
            return null;
        }

        sourceSquare.removeSprite();
        var x1 = board.getX() + sourceSquare.getX();
        var y1 = board.getY() + sourceSquare.getY();

        var destinationSquare = board.squareAt(castle.destination());
        var x2 = board.getX() + destinationSquare.getX();
        var y2 = board.getY() + destinationSquare.getY();

        castleAnimation = new Image(sprite);
        castleAnimation.setPosition(x1, y1);

        var slide = Actions.moveTo(x2, y2, settings.animationDuration(), Interpolation.smoother);
        var fullAction = Actions.sequence(slide, Actions.run(() -> castleAnimation = null), new RemoveActorAction());

        castleAnimation.addAction(fullAction);
        return castleAnimation;
    }

    boolean hasAnimations() {
        return moveAnimation != null || castleAnimation != null;
    }

    void stopAnimations() {
        if (moveAnimation != null) {
            moveAnimation.remove();
            moveAnimation = null;
        }

        if (castleAnimation != null) {
            castleAnimation.remove();
            castleAnimation = null;
        }
    }
}
