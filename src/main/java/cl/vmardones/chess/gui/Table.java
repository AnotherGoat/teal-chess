/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.gui;

import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Frame.NORMAL;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.engine.game.Game;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveResult;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.io.FontLoader;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

// TODO: Many methods used in the GUI should be moved to the game
// TODO: Many methods can take simpler arguments and not every class needs access to the board
public class Table {

    private static final Logger LOG = LogManager.getLogger(Table.class);
    private static final Dimension INITIAL_SIZE = new Dimension(700, 600);
    private static final String UI_FONT = "NotoSans-Regular.ttf";

    private final JFrame frame;
    private final BoardPanel boardPanel;
    private final CapturedPiecesPanel capturedPiecesPanel;
    private final GameHistoryPanel gameHistoryPanel;

    private Game game;
    private SelectionState selectionState = new SelectionState.NoSelectionState();
    private boolean highlightLegals;
    private boolean darkTheme;
    private BoardDirection boardDirection;

    public Table(boolean darkTheme, boolean highlightLegals, boolean flipBoard) {
        this.darkTheme = darkTheme;
        this.highlightLegals = highlightLegals;
        boardDirection = flipBoard ? BoardDirection.FLIPPED : BoardDirection.NORMAL;

        setUIFont(FontLoader.load(UI_FONT));

        frame = new JFrame("Chess game, made in Java");
        frame.setLayout(new BorderLayout());
        frame.setSize(INITIAL_SIZE);

        frame.setJMenuBar(new MenuBar());

        game = new Game();
        boardPanel = new BoardPanel(this, game.board());
        frame.add(new ContainerPanel<>(boardPanel), BorderLayout.CENTER);

        capturedPiecesPanel = new CapturedPiecesPanel(game.history());
        frame.add(capturedPiecesPanel, BorderLayout.WEST);

        gameHistoryPanel = new GameHistoryPanel();
        frame.add(gameHistoryPanel, BorderLayout.EAST);

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.addWindowStateListener(new MaximizeListener());

        reloadTheme();
        frame.setVisible(true);
        frame.pack();
    }

    void update() {
        gameHistoryPanel.draw(game.history().lastMove());
        capturedPiecesPanel.draw(game.history());

        boardPanel.setBoard(game.board());
        boardPanel.draw();
    }

    MoveResult testMove(Move move) {
        return game().testMove(move);
    }

    /* Getters and setters */

    Game game() {
        return game;
    }

    SelectionState selectionState() {
        return selectionState;
    }

    void selectionState(SelectionState value) {
        selectionState = value;
    }

    boolean isHighlightLegals() {
        return highlightLegals;
    }

    BoardDirection boardDirection() {
        return boardDirection;
    }

    private void setUIFont(Font font) {
        UIManager.getLookAndFeel().getDefaults().keySet().forEach(key -> {
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

        SwingUtilities.updateComponentTreeUI(frame);
    }

    void drawLegals(Piece selectedPiece) {
        var legals = game.findLegalMoves(selectedPiece);
        var legalDestinations = legals.stream().map(Move::destination).toList();

        boardPanel.highlightSquares(legalDestinations);
    }

    void hideHighlights() {
        boardPanel.hideHighlights();
    }

    enum BoardDirection {
        NORMAL,
        FLIPPED;

        List<SquarePanel> traverse(List<SquarePanel> boardSquares) {
            return switch (this) {
                case NORMAL -> boardSquares;
                case FLIPPED -> {
                    var reversedSquares = new ArrayList<>(boardSquares);
                    Collections.reverse(reversedSquares);
                    yield Collections.unmodifiableList(reversedSquares);
                }
            };
        }

        BoardDirection opposite() {
            return switch (this) {
                case NORMAL -> FLIPPED;
                case FLIPPED -> NORMAL;
            };
        }
    }

    private final class MenuBar extends JMenuBar {
        private MenuBar() {
            add(new FileMenu());
            add(new PreferencesMenu());
        }
    }

    private final class FileMenu extends JMenu {
        private FileMenu() {
            super("File");

            var newGame = new JMenuItem("New Game");
            newGame.addActionListener(e -> {
                LOG.info("Starting new game!");
                startNewGame();
            });
            add(newGame);

            var loadPgn = new JMenuItem("Load PGN file");
            loadPgn.addActionListener(e -> LOG.debug("Open PGN file!"));
            add(loadPgn);

            var exit = new JMenuItem("Exit");
            exit.addActionListener(e -> {
                var closeEvent = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
            });
            add(exit);
        }

        private void startNewGame() {
            game = new Game();
            boardPanel.setBoard(game.board());
            boardPanel.draw();

            if (isHighlightLegals()) {
                boardPanel.hideHighlights();
            }

            capturedPiecesPanel.reset(game.history());
            gameHistoryPanel.reset();
            selectionState = new SelectionState.NoSelectionState();
        }
    }

    private final class PreferencesMenu extends JMenu {
        private PreferencesMenu() {
            super("Preferences");

            var flipBoard = new JMenuItem("Flip Board");
            flipBoard.addActionListener(e -> {
                boardDirection = boardDirection.opposite();

                boardPanel.setBoard(game.board());
                boardPanel.draw();
            });
            add(flipBoard);

            var highlightCheckbox = new JCheckBoxMenuItem("Highlight Legal Moves", highlightLegals);
            highlightCheckbox.addActionListener(e -> highlightLegals = highlightCheckbox.isSelected());
            add(highlightCheckbox);

            var darkThemeCheckbox = new JCheckBoxMenuItem("Dark Theme", darkTheme);
            darkThemeCheckbox.addActionListener(e -> {
                darkTheme = darkThemeCheckbox.isSelected();
                reloadTheme();
            });
            add(darkThemeCheckbox);
        }
    }

    private final class MaximizeListener implements WindowStateListener {
        @Override
        public void windowStateChanged(WindowEvent e) {
            if (e.getOldState() == MAXIMIZED_BOTH && e.getNewState() == NORMAL) {
                boardPanel.setPreferredSize(BoardPanel.INITIAL_SIZE);
                frame.pack();
            }
        }
    }
}
