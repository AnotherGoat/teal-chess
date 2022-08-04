/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece.vector;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Vertical implements Vector {
  UP(new int[] {0, 1}),
  DOWN(new int[] {0, -1});

  private final int[] vector;
}
