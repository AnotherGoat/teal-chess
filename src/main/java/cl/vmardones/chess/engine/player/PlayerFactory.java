/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.player;

import java.util.List;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.BoardChecker;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveResult;
import cl.vmardones.chess.engine.move.MoveTester;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Piece;

// TODO: Possibly rename this class if it isn't the factory design pattern
public final class PlayerFactory {

    private final Alliance alliance;
    private final King king;
    private final List<Piece> pieces;
    private final List<Move> legals;
    private final List<Move> opponentLegals;

    public PlayerFactory(Board board, Alliance alliance) {
        this.alliance = alliance;
        king = board.king(alliance);
        pieces = board.pieces(Alliance.WHITE);
        legals = BoardChecker.calculateLegals(board, alliance);
        opponentLegals = BoardChecker.calculateLegals(board, alliance.opposite());
    }

    public Player create() {
        return new HumanPlayer(alliance, king, pieces, legals, calculateStatus());
    }

    private PlayerStatus calculateStatus() {

        var checked =
                !BoardChecker.isUnderAttack(king.position(), opponentLegals).isEmpty();
        var noEscape = calculateEscapeMoves();

        if (checked && noEscape) {
            return PlayerStatus.CHECKMATED;
        }

        if (!checked && noEscape) {
            return PlayerStatus.STALEMATED;
        }

        if (checked) {
            return PlayerStatus.CHECKED;
        }

        return PlayerStatus.NORMAL;
    }

    private boolean calculateEscapeMoves() {
        return legals.stream()
                .map(move -> MoveTester.testMove(move, king, legals, opponentLegals))
                .noneMatch(result -> result == MoveResult.NORMAL);
    }
}
