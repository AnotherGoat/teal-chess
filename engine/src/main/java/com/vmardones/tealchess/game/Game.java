/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import static java.util.stream.Collectors.toUnmodifiableSet;

import java.util.Set;

import com.vmardones.tealchess.ai.MoveChooser;
import com.vmardones.tealchess.analysis.PositionAnalyzer;
import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.piece.King;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Player;
import com.vmardones.tealchess.player.PlayerStatus;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A game of chess.
 * @see <a href="https://www.chessprogramming.org/Chess_Game">Chess Game</a>
 */
public final class Game {

    private final MoveMaker moveMaker;
    private final GameState state;
    private GameHistory history;
    private @Nullable MoveChooser whiteAi;
    private @Nullable MoveChooser blackAi;

    /**
     * The standard way to create a new game, starting from the initial position.
     */
    public Game() {
        moveMaker = new MoveMaker();
        state = new GameState();
        history = new GameHistory(state.save());
    }

    /* Getters */

    public Position position() {
        return state.position();
    }

    public Board board() {
        return position().board();
    }

    public GameHistory history() {
        return history;
    }

    public Player player() {
        return position().sideToMove().isWhite() ? state.whitePlayer() : state.blackPlayer();
    }

    public Player oppponent() {
        return position().sideToMove().isWhite() ? state.blackPlayer() : state.whitePlayer();
    }

    public King king() {
        return player().king();
    }

    public boolean kingAttacked() {
        return player().status() == PlayerStatus.CHECKED || player().status() == PlayerStatus.CHECKMATED;
    }

    public @Nullable MoveChooser ai() {
        return position().sideToMove().isWhite() ? whiteAi : blackAi;
    }

    /* AI setters */

    public Game whiteAi(MoveChooser value) {
        whiteAi = value;
        return this;
    }

    public Game blackAi(MoveChooser value) {
        blackAi = value;
        return this;
    }

    /* Updating game state */

    /**
     * Make a move on the chessboard and update the position.
     * @param move The legal move, chosen by the player.
     */
    public void makeMove(LegalMove move) {
        state.lastMove(move);

        var nextPosition = moveMaker.make(state.position(), move);
        state.position(nextPosition);

        var positionAnalyzer = new PositionAnalyzer(nextPosition);
        state.whitePlayer(positionAnalyzer.whitePlayer());
        state.blackPlayer(positionAnalyzer.blackPlayer());

        history = history.add(state.save());
    }

    public LegalMove makeAiMove() {
        var currentAi = ai();

        if (currentAi == null) {
            throw new UnsupportedOperationException("The current player doesn't have an AI set");
        }

        var aiMove = currentAi.chooseMove(position(), player().legals());
        makeMove(aiMove);
        return aiMove;
    }

    /* Analysis methods */

    /**
     * Given a piece, find the destinations of its moves for the current position. Mainly used when the user clicks on a piece, to highlight its legal moves.
     * @param piece The piece to move.
     * @return The legal destinations for the piece's moves.
     */
    public Set<Coordinate> findLegalDestinations(Piece piece) {
        return player().legals().stream()
                .filter(legal -> legal.piece().equals(piece))
                .map(LegalMove::destination)
                .collect(toUnmodifiableSet());
    }
}
