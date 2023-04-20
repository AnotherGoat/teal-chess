/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import com.vmardones.tealchess.game.GameHistory;
import com.vmardones.tealchess.io.PieceIconLoader;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;

class CapturedPiecesPanel extends JPanel {

    private static final Dimension INITIAL_SIZE = new Dimension(40, 80);
    private static final EtchedBorder BORDER = new EtchedBorder(EtchedBorder.RAISED);

    private final JPanel northPanel;
    private final JPanel southPanel;
    private transient GameHistory gameHistory;

    CapturedPiecesPanel(GameHistory gameHistory) {
        super(new BorderLayout());
        this.gameHistory = gameHistory;

        setBorder(BORDER);

        northPanel = new JPanel(new GridLayout(8, 2));
        add(northPanel, BorderLayout.NORTH);

        southPanel = new JPanel(new GridLayout(8, 2));
        add(southPanel, BorderLayout.SOUTH);

        setPreferredSize(INITIAL_SIZE);
    }

    void draw(GameHistory newGameHistory) {

        if (!gameHistory.equals(newGameHistory)) {
            gameHistory = newGameHistory;

            northPanel.removeAll();
            southPanel.removeAll();

            for (var color : Color.values()) {
                var panel = color == Color.WHITE ? southPanel : northPanel;
                var takenPieces = getTakenPieces(gameHistory, color);

                for (var piece : takenPieces) {
                    var icon = PieceIconLoader.load(piece, INITIAL_SIZE.width / 2, INITIAL_SIZE.width / 2);
                    panel.add(new JLabel(icon));
                }
            }

            validate();
        }
    }

    void reset(GameHistory emptyHistory) {
        gameHistory = emptyHistory;
        northPanel.removeAll();
        southPanel.removeAll();
        validate();
    }

    private List<Piece> getTakenPieces(GameHistory gameHistory, Color color) {
        return gameHistory.moves().stream()
                .filter(Move::isCapture)
                .map(Move::otherPiece)
                .filter(capturedPiece -> capturedPiece != null && capturedPiece.color() == color)
                .toList();
    }
}
