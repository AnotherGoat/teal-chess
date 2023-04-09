/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Alliance;
import cl.vmardones.chess.io.PieceIconLoader;

class CapturedPiecesPanel extends JPanel {

    private static final Dimension INITIAL_SIZE = new Dimension(40, 80);
    private static final EtchedBorder BORDER = new EtchedBorder(EtchedBorder.RAISED);

    private final JPanel northPanel;
    private final JPanel southPanel;

    CapturedPiecesPanel() {
        super(new BorderLayout());

        setBorder(BORDER);

        northPanel = new JPanel(new GridLayout(8, 2));
        add(northPanel, BorderLayout.NORTH);

        southPanel = new JPanel(new GridLayout(8, 2));
        add(southPanel, BorderLayout.SOUTH);

        setPreferredSize(INITIAL_SIZE);
    }

    void draw(MoveLog moveLog) {
        northPanel.removeAll();
        southPanel.removeAll();

        for (var alliance : Alliance.values()) {
            var panel = alliance == Alliance.WHITE ? southPanel : northPanel;
            var takenPieces = getTakenPieces(moveLog, alliance);

            for (var piece : takenPieces) {
                var icon = PieceIconLoader.load(piece, INITIAL_SIZE.width / 2, INITIAL_SIZE.width / 2);
                panel.add(new JLabel(icon));
            }
        }

        validate();
    }

    private List<Piece> getTakenPieces(MoveLog moveLog, Alliance alliance) {
        return moveLog.moves().stream()
                .filter(Move::isCapture)
                .map(Move::otherPiece)
                .filter(capturedPiece -> capturedPiece != null && capturedPiece.alliance() == alliance)
                .toList();
    }
}
