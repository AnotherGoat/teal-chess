/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import cl.vmardones.chess.engine.move.Move;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.annotation.Nullable;

class MoveLog {

  private final List<Move> moves = new ArrayList<>();

  private @Nullable Move lastMove;

  MoveLog() {}

  void add(Move move) {
    moves.add(move);
    lastMove = move;
  }

  List<Move> moves() {
    return moves;
  }

  int size() {
    return moves.size();
  }

  void clear() {
    moves.clear();
  }

  Move remove(int index) {
    return moves.remove(index);
  }

  boolean remove(Move move) {
    return moves.remove(move);
  }

  @Nullable
  Move getLastMove() {
    var move = lastMove;
    lastMove = null;
    return move;
  }
}
