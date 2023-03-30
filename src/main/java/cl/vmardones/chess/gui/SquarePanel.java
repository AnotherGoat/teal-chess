/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.*;

class SquarePanel<T extends JPanel> extends JPanel {

  private final T square;

  SquarePanel(T square) {
    this.square = square;

    addComponentListener(resizeListener());
    add(this.square);
  }

  private ComponentListener resizeListener() {

    return new ComponentListener() {

      public void componentResized(ComponentEvent e) {
        resize();
        revalidate();
      }

      public void componentMoved(ComponentEvent e) {
        // Do nothing
      }

      public void componentShown(ComponentEvent e) {
        // Do nothing
      }

      public void componentHidden(ComponentEvent e) {
        // Do nothing
      }
    };
  }

  // TODO: Resize every component along with the panel
  private void resize() {
    square.setPreferredSize(new SquareDimension(Math.min(getHeight(), getWidth())));
  }

  private static class SquareDimension extends Dimension {
    public SquareDimension(int side) {
      super(side, side);
    }
  }
}
