/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.board.Tile;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Alliance;
import cl.vmardones.chess.io.PieceIconLoader;
import cl.vmardones.chess.io.SvgLoader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jdt.annotation.Nullable;

@Slf4j
class TilePanel extends JPanel {

  private static final Dimension INITIAL_SIZE = new Dimension(60, 60);
  private static final Color LIGHT_COLOR = Color.decode("#FFCE9E");
  private static final Color DARK_COLOR = Color.decode("#D18B47");

  private static final Integer PIECE_LAYER = 1;
  private static final Integer HIGHLIGHT_LAYER = 2;

  private final transient Table table;
  private JLayeredPane layeredPane;
  private final transient Coordinate coordinate;

  TilePanel(final Table table, final Coordinate coordinate) {
    super(new BorderLayout());

    this.table = table;
    this.coordinate = coordinate;

    setOpaque(true);
    setPreferredSize(INITIAL_SIZE);

    layeredPane = createLayeredPane();

    assignTileColor();
    assignPieceIcon(table.getTileAt(coordinate));
    add(layeredPane, BorderLayout.CENTER);

    validate();

    addMouseListener(clickListener());
  }

  private JLayeredPane createLayeredPane() {
    final var layeredPane = new JLayeredPane();

    layeredPane.setLayout(new BorderLayout());
    layeredPane.setOpaque(true);

    return layeredPane;
  }

  private MouseListener clickListener() {

    return new MouseListener() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        if (isLeftMouseButton(e)) {
          if (table.getSourceTile() == null) {
            firstLeftClick();
          } else {
            secondLeftClick();
          }
        } else if (isRightMouseButton(e)) {
          table.resetSelection();
          log.debug("Pressed right click, unselecting");
        }

        SwingUtilities.invokeLater(table::update);
      }

      @Override
      public void mousePressed(final MouseEvent e) {
        // Do nothing
      }

      @Override
      public void mouseReleased(final MouseEvent e) {
        // Do nothing
      }

      @Override
      public void mouseEntered(final MouseEvent e) {
        // Do nothing
      }

      @Override
      public void mouseExited(final MouseEvent e) {
        // Do nothing
      }
    };
  }

  private void firstLeftClick() {
    log.debug("Selected the tile {}", coordinate);
    table.setSourceTile(table.getTileAt(coordinate));

    if (getSelectedPiece() != null) {
      table.setSelectedPiece(getSelectedPiece());
      log.debug("The tile contains {}", table.getSelectedPiece());
      log.debug("Highlighting legal moves");
    } else {
      log.debug("The tile is unoccupied, unselecting");
      table.resetSelection();
    }
  }

  private @Nullable Piece getSelectedPiece() {
    return table.getSourceTile().getPiece();
  }

  private void secondLeftClick() {
    // TODO: Replace all these long method calls with forwarding methods
    log.debug("Selected the destination {}", coordinate);
    table.setDestinationTile(table.getTileAt(coordinate));

    final var move =
        Move.MoveFactory.create(
            table.getGame().getCurrentPlayer().getLegals(),
            table.getSourceTile().getCoordinate(),
            table.getDestinationTile().getCoordinate());

    log.debug("Is there a move that can get to the destination? {}", move != null);

    if (move != null) {
      final var moveTransition = table.makeMove(move);

      if (moveTransition.getMoveStatus().isDone()) {
        table.getGame().createNextTurn(move);
        table.addToLog(move);
      }
    }

    table.resetSelection();
  }

  void drawTile(final Board board) {
    layeredPane = createLayeredPane();
    // assignTileColor();
    assignPieceIcon(table.getTileAt(coordinate));
    highlightLegals(board);
    add(layeredPane, BorderLayout.CENTER);
    validate();
    repaint();
  }

  private void assignTileColor() {
    setBackground(coordinate.getColor() == Alliance.WHITE ? LIGHT_COLOR : DARK_COLOR);
  }

  private void assignPieceIcon(final Tile tile) {
    removeAll();

    if (tile.getPiece() != null) {
      final var icon =
          PieceIconLoader.load(tile.getPiece(), INITIAL_SIZE.width, INITIAL_SIZE.height);

      if (icon != null) {
        addImage(icon, PIECE_LAYER);
      }
    }
  }

  private void addImage(final BufferedImage bufferedImage, final Integer layer) {
    final var image = new JLabel(new ImageIcon(bufferedImage));

    layeredPane.add(image, BorderLayout.CENTER, layer);
  }

  private void highlightLegals(final Board board) {
    if (table.isHighlightLegals()) {
      selectedPieceLegals(board).stream()
          .filter(move -> move.getDestination() == coordinate)
          .forEach(
              move -> {
                final var greenDot =
                    SvgLoader.load(
                        "art/misc/green_dot.svg", INITIAL_SIZE.width / 2, INITIAL_SIZE.height / 2);

                if (greenDot != null) {
                  addImage(greenDot, HIGHLIGHT_LAYER);
                }
              });
    }
  }

  private List<Move> selectedPieceLegals(final Board board) {
    if (table.getSelectedPiece() == null || isOpponentPieceSelected()) {
      return Collections.emptyList();
    }

    return table.getSelectedPiece().calculateLegals(board);
  }

  private boolean isOpponentPieceSelected() {
    return table.getSelectedPiece().getAlliance()
        != table.getGame().getCurrentPlayer().getAlliance();
  }
}
