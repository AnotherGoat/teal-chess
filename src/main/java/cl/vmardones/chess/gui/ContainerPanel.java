/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.*;

class ContainerPanel<T extends JPanel> extends JPanel {

    private final T squarePanel;

    ContainerPanel(T squarePanel) {
        this.squarePanel = squarePanel;

        addComponentListener(resizeListener());
        add(this.squarePanel);
    }

    private ComponentListener resizeListener() {
        return new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resize();
                revalidate();
            }
        };
    }

    // TODO: Resize every component along with the panel
    private void resize() {
        squarePanel.setPreferredSize(new SquareDimension(Math.min(getHeight(), getWidth())));
    }

    private static final class SquareDimension extends Dimension {
        public SquareDimension(int side) {
            super(side, side);
        }
    }
}
