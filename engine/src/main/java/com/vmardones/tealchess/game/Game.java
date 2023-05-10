/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import static java.util.stream.Collectors.toUnmodifiableSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vmardones.tealchess.ai.MoveChooser;
import com.vmardones.tealchess.analysis.PositionAnalyzer;
import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.move.MoveFinder;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.parser.fen.Fen;
import com.vmardones.tealchess.parser.fen.FenSerializer;
import com.vmardones.tealchess.parser.pgn.Pgn;
import com.vmardones.tealchess.parser.pgn.PgnSerializer;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;
import com.vmardones.tealchess.player.Player;
import com.vmardones.tealchess.player.PlayerStatus;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A game of chess.
 * @see <a href="https://www.chessprogramming.org/Chess_Game">Chess Game</a>
 */
public final class Game implements Fen, Pgn {

    private final MoveMaker moveMaker;
    private final MoveFinder moveFinder;
    private final GameState state;
    private GameHistory history;
    private final Map<String, String> tags;
    private @Nullable MoveChooser whiteAi;
    private @Nullable MoveChooser blackAi;

    /**
     * The standard way to create a new game, starting from the initial position.
     * Also used when loading a PGN file.
     * @param tags Map containing the PGN tag-value pairs.
     */
    public Game(MoveMaker moveMaker, MoveFinder moveFinder, Map<String, String> tags) {
        this.moveMaker = moveMaker;
        this.moveFinder = moveFinder;
        this.tags = tags;
        state = new GameState();
        history = new GameHistory(state.save());
    }

    /* Getters */

    public Board board() {
        return position().board();
    }

    public CastlingRights castlingRights() {
        return position().castlingRights();
    }

    public @Nullable Coordinate enPassantTarget() {
        return position().enPassantTarget();
    }

    public Color sideToMove() {
        return position().sideToMove();
    }

    /**
     * Find all the moves made in this game so far, alternating sides.
     * @return The move list.
     * @see <a href="https://www.chessprogramming.org/Move_List">Move List</a>
     */
    public List<LegalMove> moveHistory() {
        return history.moves();
    }

    public Map<String, String> tags() {
        return tags;
    }

    public String playerInfo() {
        return player().toString();
    }

    public PlayerStatus playerStatus() {
        return player().status();
    }

    public List<LegalMove> legalMoves() {
        return player().legals();
    }

    public String opponentInfo() {
        return oppponent().toString();
    }

    public Coordinate kingCoordinate() {
        return player().king().coordinate();
    }

    public boolean isKingAttacked() {
        return player().status() == PlayerStatus.CHECKED || player().status() == PlayerStatus.CHECKMATED;
    }

    public boolean hasLegalMoves() {
        return !player().legals().isEmpty();
    }

    public boolean isAiTurn() {
        return ai() != null;
    }

    public boolean isOpponentPiece(Piece piece) {
        return piece.color() == oppponent().color();
    }

    public boolean isCastling(LegalMove move) {
        return move.isCastling();
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

    public List<LegalMove> findLegalMoves(Coordinate source, Coordinate destination) {
        return moveFinder.find(player().legals(), source, destination);
    }

    /* Serialization methods */

    @Override
    public String pgn() {
        return PgnSerializer.serialize(this);
    }

    @Override
    public String fen() {
        return FenSerializer.serialize(position());
    }

    private Position position() {
        return state.position();
    }

    private Player player() {
        return sideToMove().isWhite() ? state.whitePlayer() : state.blackPlayer();
    }

    private Player oppponent() {
        return sideToMove().isWhite() ? state.blackPlayer() : state.whitePlayer();
    }

    private @Nullable MoveChooser ai() {
        return sideToMove().isWhite() ? whiteAi : blackAi;
    }
}
