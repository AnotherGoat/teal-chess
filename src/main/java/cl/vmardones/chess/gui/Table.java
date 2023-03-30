/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Frame.NORMAL;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.board.Tile;
import cl.vmardones.chess.engine.game.Game;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveTransition;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.io.FontLoader;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

// TODO: Many methods used in the GUI should be moved to the game
// TODO: Many methods can take simpler arguments and not every class needs access to the board
@Slf4j
public class Table {

  private static final Dimension INITIAL_SIZE = new Dimension(700, 600);
  private static final String FONT_NAME = "NotoSans-Regular.ttf";

  private final JFrame gameFrame;
  private final BoardPanel boardPanel;

  private final TakenPiecesPanel takenPiecesPanel;

  private final GameHistoryPanel gameHistoryPanel;

  @Getter @Setter private Game game;

  // TODO: Replace move log with game history
  private final MoveLog moveLog;

  // TODO: Group these 3 in a "PlayerSelection" class
  @Getter @Setter private Tile sourceTile;

  @Getter @Setter private Tile destinationTile;

  @Getter @Setter private Piece selectedPiece;

  @Getter private boolean highlightLegals;

  private boolean darkTheme;

  @Getter private BoardDirection boardDirection;

  public Table(boolean darkTheme, boolean highlightLegals, boolean flipBoard) {
    this.darkTheme = darkTheme;
    this.highlightLegals = highlightLegals;
    boardDirection = flipBoard ? BoardDirection.FLIPPED : BoardDirection.NORMAL;

    reloadTheme();
    setUIFont(FontLoader.load(FONT_NAME));

    game = new Game();

    gameFrame = new JFrame("Chess game, made in Java");
    gameFrame.setLayout(new BorderLayout());
    gameFrame.setSize(INITIAL_SIZE);

    gameFrame.setJMenuBar(createMenuBar());

    boardPanel = new BoardPanel(this);
    takenPiecesPanel = new TakenPiecesPanel();
    gameHistoryPanel = new GameHistoryPanel();

    moveLog = new MoveLog();

    gameFrame.add(new SquarePanel<>(boardPanel), BorderLayout.CENTER);

    gameFrame.add(takenPiecesPanel, BorderLayout.WEST);
    gameFrame.add(gameHistoryPanel, BorderLayout.EAST);

    gameFrame.setLocationRelativeTo(null);
    gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    gameFrame.addWindowStateListener(maximizeListener());

    gameFrame.setVisible(true);
    gameFrame.pack();
  }

  private WindowStateListener maximizeListener() {
    return e -> {
      if (e.getOldState() == MAXIMIZED_BOTH && e.getNewState() == NORMAL) {
        boardPanel.setPreferredSize(BoardPanel.INITIAL_SIZE);
        gameFrame.pack();
      }
    };
  }

  private void setUIFont(Font font) {
    UIManager.getLookAndFeel()
        .getDefaults()
        .keys()
        .asIterator()
        .forEachRemaining(
            key -> {
              if (UIManager.get(key) instanceof Font) {
                UIManager.put(key, font);
              }
            });
  }

  private void reloadTheme() {
    if (darkTheme) {
      FlatDarkLaf.setup();
    } else {
      FlatLightLaf.setup();
    }

    if (gameFrame != null) {
      SwingUtilities.updateComponentTreeUI(gameFrame);
    }
  }

  private JMenuBar createMenuBar() {
    var menuBar = new JMenuBar();
    menuBar.add(createFileMenu());
    menuBar.add(createPreferencesMenu());
    return menuBar;
  }

  private JMenu createFileMenu() {
    var fileMenu = new JMenu("File");

    var openPGN = new JMenuItem("Load PGN file");
    openPGN.addActionListener(e -> log.debug("Open PGN file!"));
    fileMenu.add(openPGN);

    var exit = new JMenuItem("Exit");
    exit.addActionListener(e -> System.exit(0));
    fileMenu.add(exit);

    return fileMenu;
  }

  private JMenu createPreferencesMenu() {
    var preferencesMenu = new JMenu("Preferences");

    var flipBoard = new JMenuItem("Flip Board");
    flipBoard.addActionListener(
        e -> {
          boardDirection = boardDirection.getOpposite();
          boardPanel.drawBoard(game.getBoard());
        });

    var highlightCheckbox = new JCheckBoxMenuItem("Highlight Legal Moves", highlightLegals);
    highlightCheckbox.addActionListener(e -> highlightLegals = highlightCheckbox.isSelected());

    var darkThemeCheckbox = new JCheckBoxMenuItem("Dark Theme", darkTheme);
    darkThemeCheckbox.addActionListener(
        e -> {
          darkTheme = darkThemeCheckbox.isSelected();
          reloadTheme();
        });

    preferencesMenu.add(flipBoard);
    preferencesMenu.add(highlightCheckbox);
    preferencesMenu.add(darkThemeCheckbox);
    return preferencesMenu;
  }

  void resetSelection() {
    sourceTile = null;
    destinationTile = null;
    selectedPiece = null;
  }

  void update() {
    gameHistoryPanel.redo(game.getCurrentPlayer(), moveLog);
    takenPiecesPanel.redo(moveLog);
    boardPanel.drawBoard(game.getBoard());
  }

  void addToLog(Move move) {
    moveLog.add(move);
  }

  Tile getTileAt(Coordinate coordinate) {
    return getGame().getBoard().getTile(coordinate);
  }

  MoveTransition makeMove(Move move) {
    return getGame().performMove(move);
  }

  enum BoardDirection {
    NORMAL,
    FLIPPED;

    List<TilePanel> traverse(List<TilePanel> boardTiles) {
      return switch (this) {
        case NORMAL -> boardTiles;
        case FLIPPED -> {
          var reversedTiles = new ArrayList<>(boardTiles);
          Collections.reverse(reversedTiles);
          yield Collections.unmodifiableList(reversedTiles);
        }
      };
    }

    BoardDirection getOpposite() {
      return switch (this) {
        case NORMAL -> FLIPPED;
        case FLIPPED -> NORMAL;
      };
    }
  }
}
