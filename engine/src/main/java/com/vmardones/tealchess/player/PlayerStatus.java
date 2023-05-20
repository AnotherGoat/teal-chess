/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;

/** Tells the status of a player. It should be updated after every turn. */
public enum PlayerStatus {
    /** Can play like normal, the most common state during most of the game. */
    OK,
    /** The player is in check and must protect their king, either by capturing the attacker or by blocking the attack with another piece. */
    CHECKED,
    /** The player is in check but they can't protect their king. This means this player lost and the opponent wins. */
    CHECKMATED,
    /** The player's king isn't in checkmate but all of its pieces can't escape from the current situation, so the game ends in a tie. In other words, the opponent failed when trying to checkmate the player. */
    STALEMATED
}
