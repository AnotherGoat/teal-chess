/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Set;

import com.vmardones.tealchess.game.Position;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.piece.King;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;
import com.vmardones.tealchess.player.Player;
import com.vmardones.tealchess.player.PlayerStatus;

final class PlayerFactory {

    private final AttackTester attackTester;
    private final Color sideToMove;
    private final King king;
    private final Set<Piece> pieces;
    private final List<LegalMove> legals;
    private final King opponentKing;
    private final Set<Piece> opponentPieces;

    PlayerFactory(Position position, List<LegalMove> legals) {
        sideToMove = position.sideToMove();
        king = position.board().king(sideToMove);
        pieces = position.board().pieces(sideToMove);
        opponentKing = position.board().king(sideToMove.opposite());
        opponentPieces = position.board().pieces(sideToMove.opposite());

        var opponentAttacks = new AttackGenerator(position).calculateAttacks(true);
        attackTester = new AttackTester(position, opponentAttacks);

        this.legals = legals;
    }

    Player create(Color color) {
        if (color == sideToMove) {
            return new Player(color, king, pieces, legals, calculateStatus());
        }

        return new Player(color, opponentKing, opponentPieces, emptyList(), PlayerStatus.NORMAL);
    }

    private PlayerStatus calculateStatus() {

        var attacked = attackTester.isKingAttacked();
        var cantMove = legals.isEmpty();

        if (attacked && cantMove) {
            return PlayerStatus.CHECKMATED;
        }

        if (!attacked && cantMove) {
            return PlayerStatus.STALEMATED;
        }

        if (attacked) {
            return PlayerStatus.CHECKED;
        }

        return PlayerStatus.NORMAL;
    }
}
