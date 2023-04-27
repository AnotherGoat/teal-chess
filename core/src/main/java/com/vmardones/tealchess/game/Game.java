/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import static java.util.stream.Collectors.toUnmodifiableSet;

import java.util.List;
import java.util.Set;

import com.vmardones.tealchess.analysis.PositionAnalyzer;
import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Player;

public final class Game {

    private final MoveMaker moveMaker;
    private final GameState state;
    private PositionAnalyzer positionAnalyzer;
    private GameHistory history;
    private Player whitePlayer;
    private Player blackPlayer;

    public Game() {
        moveMaker = new MoveMaker();
        state = new GameState();
        history = new GameHistory();
        positionAnalyzer = new PositionAnalyzer(state.position());
        whitePlayer = positionAnalyzer.newWhitePlayer();
        blackPlayer = positionAnalyzer.newBlackPlayer();

        registerPosition(state.position());
    }

    /* Getters */

    public Position position() {
        return state.position();
    }

    public Board board() {
        return state.position().board();
    }

    public GameHistory history() {
        return history;
    }

    public Player player() {
        return state.position().sideToMove().player(players());
    }

    public Player oppponent() {
        return state.position().sideToMove().opponent(players());
    }

    /* Updating game state */

    public void updatePosition(LegalMove move) {
        state.lastMove(move);

        var nextPosition = moveMaker.make(state.position(), move);

        positionAnalyzer = new PositionAnalyzer(nextPosition);
        whitePlayer = positionAnalyzer.newWhitePlayer();
        blackPlayer = positionAnalyzer.newBlackPlayer();

        registerPosition(nextPosition);
    }

    /* Analysis methods */

    /**
     * Given a piece, find the legal moves it has for this position. Mainly used when the user clicks on a piece, to highlight its legal moves.
     * @param piece The piece to move.
     * @return The legal moves of the piece.
     */
    public Set<Coordinate> findLegalDestinations(Piece piece) {
        return positionAnalyzer.findLegalMoves(piece).stream()
                .map(LegalMove::destination)
                .collect(toUnmodifiableSet());
    }

    private List<Player> players() {
        return List.of(whitePlayer, blackPlayer);
    }

    private void registerPosition(Position position) {
        state.position(position);
        history = history.add(state.save());
    }
}
