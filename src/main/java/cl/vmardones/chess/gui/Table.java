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
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Many methods used in the GUI should be moved to the game
// TODO: Many methods can take simpler arguments and not every class needs access to the board
public class Table {

  private static final Logger LOG = LoggerFactory.getLogger(Table.class);
  private static final Dimension INITIAL_SIZE = new Dimension(700, 600);
  private static final String UI_FONT = "NotoSans-Regular.ttf";

  private final JFrame frame;
  private final BoardPanel boardPanel;

  private final TakenPiecesPanel takenPiecesPanel;

  private final GameHistoryPanel gameHistoryPanel;

  private Game game;

  // TODO: Replace move log with game history
  private final MoveLog moveLog;

  // TODO: Group these 3 in a "PlayerSelection" class
  @Nullable private Tile sourceTile;

  @Nullable private Tile destinationTile;

  @Nullable private Piece selectedPiece;

  private boolean highlightLegals;

  private boolean darkTheme;

  private BoardDirection boardDirection;

  public Table(boolean darkTheme, boolean highlightLegals, boolean flipBoard) {
    this.darkTheme = darkTheme;
    this.highlightLegals = highlightLegals;
    boardDirection = flipBoard ? BoardDirection.FLIPPED : BoardDirection.NORMAL;

    reloadTheme();
    setUIFont(FontLoader.load(UI_FONT));

    game = new Game();

    frame = new JFrame("Chess game, made in Java");
    frame.setLayout(new BorderLayout());
    frame.setSize(INITIAL_SIZE);

    frame.setJMenuBar(createMenuBar());

    boardPanel = new BoardPanel(this);
    takenPiecesPanel = new TakenPiecesPanel();
    gameHistoryPanel = new GameHistoryPanel();

    moveLog = new MoveLog();

    frame.add(new SquarePanel<>(boardPanel), BorderLayout.CENTER);

    frame.add(takenPiecesPanel, BorderLayout.WEST);
    frame.add(gameHistoryPanel, BorderLayout.EAST);

    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    frame.addWindowStateListener(maximizeListener());

    frame.setVisible(true);
    frame.pack();
  }

  private WindowStateListener maximizeListener() {
    return e -> {
      if (e.getOldState() == MAXIMIZED_BOTH && e.getNewState() == NORMAL) {
        boardPanel.setPreferredSize(BoardPanel.INITIAL_SIZE);
        frame.pack();
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

    if (frame != null) {
      SwingUtilities.updateComponentTreeUI(frame);
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

    var openPgn = new JMenuItem("Load PGN file");
    openPgn.addActionListener(e -> LOG.debug("Open PGN file!"));
    fileMenu.add(openPgn);

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
    return getGame().getBoard().tileAt(coordinate);
  }

  MoveTransition makeMove(Move move) {
    return getGame().performMove(move);
  }

  /* Getters and setters */

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public @Nullable Tile getSourceTile() {
    return sourceTile;
  }

  public void setSourceTile(Tile sourceTile) {
    this.sourceTile = sourceTile;
  }

  public @Nullable Tile getDestinationTile() {
    return destinationTile;
  }

  public void setDestinationTile(Tile destinationTile) {
    this.destinationTile = destinationTile;
  }

  public @Nullable Piece getSelectedPiece() {
    return selectedPiece;
  }

  public void setSelectedPiece(Piece selectedPiece) {
    this.selectedPiece = selectedPiece;
  }

  public boolean isHighlightLegals() {
    return highlightLegals;
  }

  public BoardDirection getBoardDirection() {
    return boardDirection;
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
