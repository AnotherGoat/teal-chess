/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import javax.swing.*;

import org.eclipse.jdt.annotation.Nullable;

class IconLabel extends JLabel {

    IconLabel(@Nullable Icon icon) {
        setIcon(icon);

        // TODO: Centering the icon doesn't work here for some reason
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    @Override
    public void setIcon(@Nullable Icon icon) {
        super.setIcon(icon);

        if (icon != null) {
            setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
        }
    }
}
