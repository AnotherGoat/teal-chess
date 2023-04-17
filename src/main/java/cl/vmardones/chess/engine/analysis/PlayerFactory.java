/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import java.util.Collections;
import java.util.List;

import cl.vmardones.chess.engine.game.Position;
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
    private final Color sideToMove;
    private final King king;
    private final List<Piece> pieces;
    private final List<Move> legals;
    private final King opponentKing;
    private final List<Piece> opponentPieces;

    PlayerFactory(Position position, MoveTester moveTester, List<Move> legals) {
        sideToMove = position.sideToMove();
        king = position.board().king(sideToMove);
        pieces = position.board().pieces(sideToMove);
        opponentKing = position.board().king(sideToMove.opposite());
        opponentPieces = position.board().pieces(sideToMove.opposite());
        this.moveTester = moveTester;
        this.legals = legals;
    }

    Player create(Color color) {
        if (color == sideToMove) {
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
