/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

import cl.vmardones.chess.engine.board.Board;

// TODO: Move is probably not needed here
/** The transition from one board to another, triggered when a move is made or checked. */
public record MoveTransition(Board board, Move move, MoveStatus moveStatus) {}
