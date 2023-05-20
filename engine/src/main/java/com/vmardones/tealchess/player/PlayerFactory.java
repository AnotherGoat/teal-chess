/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;

import static java.util.Collections.emptyList;

import java.util.List;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.generator.AttackGenerator;
import com.vmardones.tealchess.generator.MoveGenerator;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.position.Position;

/**
 * Analyzes a certain position, generates all the possible legal moves for the side to move and checks each player's status.
 */
public final class PlayerFactory {

    private final AttackGenerator attackGenerator;
    private final MoveGenerator moveGenerator;

    public PlayerFactory(AttackGenerator attackGenerator, MoveGenerator moveGenerator) {
        this.attackGenerator = attackGenerator;
        this.moveGenerator = moveGenerator;
    }

    /**
     * Create a player for a new position, which includes all their legal moves if it's their turn to move.
     * Otherwise, the player won't have legal moves.
     * This method also calculates the player's status after the move is done.
     * @param position The position to analyze. Generally, this will be used after a move is made.
     * @param color The player's color.
     * @return The requested player.
     */
    public Player create(Position position, Color color) {
        if (color == position.sideToMove()) {
            var legals = moveGenerator.generate(position);
            return new Player(color, legals, calculateStatus(position, color, legals));
        }

        return new Player(color, emptyList(), PlayerStatus.OK);
    }

    private PlayerStatus calculateStatus(Position position, Color color, List<Move> legals) {

        var attacked = attackGenerator.isKingAttacked(position, color);
        var cantMove = legals.isEmpty();

        if (attacked && cantMove) {
            return PlayerStatus.CHECKMATED;
        }

        if (!attacked && cantMove) {
            return PlayerStatus.STALEMATED;
        }

        if (attacked) {
            return PlayerStatus.CHECKED;
        }

        return PlayerStatus.OK;
    }
}
