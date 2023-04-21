/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static java.util.Collections.emptyList;

import java.util.List;

import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveResult;
import com.vmardones.tealchess.piece.King;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;
import com.vmardones.tealchess.player.HumanPlayer;
import com.vmardones.tealchess.player.Player;
import com.vmardones.tealchess.player.PlayerStatus;

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

        return new HumanPlayer(color, opponentKing, opponentPieces, emptyList(), PlayerStatus.NORMAL);
    }

    private PlayerStatus calculateStatus() {

        var checked = moveTester.isKingAttacked();
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

    // TODO: Implement this method properly
    private boolean calculateEscapeMoves() {
        return legals.stream()
                .map(move -> moveTester.testMove(move, legals))
                .noneMatch(result -> result == MoveResult.CONTINUE
                        || result == MoveResult.CHECKS
                        || result == MoveResult.CHECKMATES);
    }
}
