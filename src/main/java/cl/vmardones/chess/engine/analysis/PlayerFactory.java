/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import java.util.Collections;
import java.util.List;

import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveResult;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Color;
import cl.vmardones.chess.engine.player.HumanPlayer;
import cl.vmardones.chess.engine.player.Player;
import cl.vmardones.chess.engine.player.PlayerStatus;

final class PlayerFactory {

    private final MoveTester moveTester;
    private final Color activeColor;
    private final King king;
    private final List<Piece> pieces;
    private final List<Move> legals;
    private final King opponentKing;
    private final List<Piece> opponentPieces;

    PlayerFactory(
            MoveTester moveTester,
            Color activeColor,
            King king,
            List<Piece> pieces,
            List<Move> legals,
            King opponentKing,
            List<Piece> opponentPieces) {
        this.moveTester = moveTester;
        this.activeColor = activeColor;

        this.king = king;
        this.pieces = pieces;
        this.legals = legals;

        this.opponentKing = opponentKing;
        this.opponentPieces = opponentPieces;
    }

    Player create(Color color) {
        if (color == activeColor) {
            return new HumanPlayer(color, king, pieces, legals, calculateStatus());
        }

        return new HumanPlayer(color, opponentKing, opponentPieces, Collections.emptyList(), PlayerStatus.NORMAL);
    }

    private PlayerStatus calculateStatus() {

        var checked = !moveTester.attacksOnKing().isEmpty();
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
                .map(move -> moveTester.testMove(move, legals))
                .noneMatch(result -> result == MoveResult.CONTINUE
                        || result == MoveResult.CHECKS
                        || result == MoveResult.CHECKMATES);
    }
}
