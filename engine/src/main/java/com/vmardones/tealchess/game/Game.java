/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vmardones.tealchess.board.BitboardManipulator;
import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.generator.AttackGenerator;
import com.vmardones.tealchess.generator.LegalGenerator;
import com.vmardones.tealchess.generator.MoveGenerator;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveFinder;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.parser.fen.Fen;
import com.vmardones.tealchess.parser.fen.FenSerializer;
import com.vmardones.tealchess.parser.pgn.Pgn;
import com.vmardones.tealchess.parser.pgn.PgnSerializer;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Player;
import com.vmardones.tealchess.player.PlayerFactory;
import com.vmardones.tealchess.player.PlayerStatus;
import com.vmardones.tealchess.position.CastlingRights;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.search.MoveChooser;
import com.vmardones.tealchess.square.Coordinate;
import org.jspecify.annotations.Nullable;

/**
 * A game of chess.
 * @see <a href="https://www.chessprogramming.org/Chess_Game">Chess Game</a>
 */
public final class Game implements Fen, Pgn {

    /* Injected dependencies */
    private final MoveMaker moveMaker;
    private final MoveFinder moveFinder;
    private final AttackGenerator attackGenerator;
    private final PlayerFactory playerFactory;

    private final GameState state;
    private GameHistory history;
    private final Map<String, String> tags;
    private @Nullable MoveChooser whiteAi;
    private @Nullable MoveChooser blackAi;

    /**
     * The easiest way to create a new game, starting from the initial position.
     * @param tags Map containing the PGN tag-value pairs.
     */
    public Game(Map<String, String> tags) {
        this(new MoveMaker(), new MoveFinder(), new AttackGenerator(), new LegalGenerator(), tags);
    }

    public Game(Map<String, String> tags, Position initialPosition) {
        this(new MoveMaker(), new MoveFinder(), new AttackGenerator(), new LegalGenerator(), tags, initialPosition);
    }

    /**
     * A more complete option to create a new game, starting from the initial position.
     * This alternative allows usage of dependency injection.
     * The player factory will be created with the injected dependencies.
     * @param tags Map containing the PGN tag-value pairs.
     */
    public Game(
            MoveMaker moveMaker,
            MoveFinder moveFinder,
            AttackGenerator attackGenerator,
            MoveGenerator moveGenerator,
            Map<String, String> tags) {
        this(moveMaker, moveFinder, attackGenerator, moveGenerator, tags, Position.INITIAL_POSITION);
    }

    public Game(
            MoveMaker moveMaker,
            MoveFinder moveFinder,
            AttackGenerator attackGenerator,
            MoveGenerator moveGenerator,
            Map<String, String> tags,
            Position startingPosition) {
        this.moveMaker = moveMaker;
        this.moveFinder = moveFinder;
        this.attackGenerator = attackGenerator;
        this.tags = tags;

        playerFactory = new PlayerFactory(attackGenerator, moveGenerator);
        var whitePlayer = playerFactory.create(startingPosition, Color.WHITE);
        var blackPlayer = playerFactory.create(startingPosition, Color.BLACK);

        state = new GameState(startingPosition, whitePlayer, blackPlayer);
        history = new GameHistory(state.save());
    }

    /* Getters */

    public Board board() {
        return position().board();
    }

    public Color sideToMove() {
        return position().sideToMove();
    }

    public CastlingRights castlingRights() {
        return position().castlingRights();
    }

    public @Nullable Integer enPassantTarget() {
        return position().enPassantTarget();
    }

    public int halfmoveClock() {
        return position().halfmoveClock();
    }

    public int fullmoveCounter() {
        return position().fullmoveCounter();
    }

    /**
     * Find all the moves made in this game so far, alternating sides.
     * @return The move list.
     * @see <a href="https://www.chessprogramming.org/Move_List">Move List</a>
     */
    public List<Move> moveHistory() {
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

    public List<Move> legalMoves() {
        return player().legals();
    }

    public String opponentInfo() {
        return oppponent().toString();
    }

    public Coordinate kingCoordinate() {
        var king = board().kings(sideToMove());
        var kingSquare = BitboardManipulator.firstBit(king);
        return Coordinate.forSquare(kingSquare);
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

    public @Nullable Move castlingStep(Move move) {
        if (move.equals(Move.WHITE_SHORT_CASTLE_STEPS.get(0))) {
            return Move.WHITE_SHORT_CASTLE_STEPS.get(1);
        }

        if (move.equals(Move.WHITE_LONG_CASTLE_STEPS.get(0))) {
            return Move.WHITE_LONG_CASTLE_STEPS.get(1);
        }

        if (move.equals(Move.BLACK_SHORT_CASTLE_STEPS.get(0))) {
            return Move.BLACK_SHORT_CASTLE_STEPS.get(1);
        }

        if (move.equals(Move.BLACK_LONG_CASTLE_STEPS.get(0))) {
            return Move.BLACK_LONG_CASTLE_STEPS.get(1);
        }

        return null;
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
    public void makeMove(Move move) {
        state.lastMove(move);

        var nextPosition = moveMaker.make(position(), move);
        state.position(nextPosition);

        state.whitePlayer(playerFactory.create(nextPosition, Color.WHITE));
        state.blackPlayer(playerFactory.create(nextPosition, Color.BLACK));

        history = history.add(state.save());
    }

    public Move chooseAiMove() {
        var currentAi = ai();

        if (currentAi == null) {
            throw new UnsupportedOperationException("The current player doesn't have an AI set");
        }

        return currentAi.chooseMove(history.lastSave());
    }

    /* Analysis methods */

    /**
     * Given a source, find the destinations of its moves for the current position.
     * Mainly used when the user clicks on a piece, to highlight its legal moves.
     * @param source The source coordinate.
     * @return The legal destinations for the piece's moves.
     */
    public Set<Coordinate> findLegalDestinations(Coordinate source) {
        return moveFinder.findDestinations(player().legals(), source);
    }

    public List<Move> findLegalMoves(Coordinate source, Coordinate destination) {
        return moveFinder.find(player().legals(), source, destination);
    }

    public Set<Coordinate> findOpponentAttacks() {
        var attacks = attackGenerator.generate(position(), sideToMove().opposite());

        return BitboardManipulator.bits(attacks).map(Coordinate::forSquare).collect(toSet());
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
