/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.parser.FenParser;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Rook;
import org.junit.jupiter.api.Test;

class CastleGeneratorTest {

    @Test
    void noWhiteCastlingRights() {
        var position = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R w kq - 0 1");
        var opponentAttacks = new AttackGenerator(position).calculateAttacks(true);
        var moveTester = new MoveTester(position, opponentAttacks.toList());
        var generator = new CastleGenerator(position, moveTester);

        assertThat(generator.calculateCastles()).isEmpty();
    }

    @Test
    void noBlackCastlingRights() {
        var position = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R b KQ - 0 1");
        var opponentAttacks = new AttackGenerator(position).calculateAttacks(true);
        var moveTester = new MoveTester(position, opponentAttacks.toList());
        var generator = new CastleGenerator(position, moveTester);

        assertThat(generator.calculateCastles()).isEmpty();
    }

    @Test
    void verySpecificQueenCastle() {
        // Castling can be done queen side even if the square beside the rook is being attacked
        var position = FenParser.parse("1r5k/8/8/8/8/8/8/R3K3 w Q - 0 1");
        var opponentAttacks = new AttackGenerator(position).calculateAttacks(true);
        var moveTester = new MoveTester(position, opponentAttacks.toList());
        var generator = new CastleGenerator(position, moveTester);

        var board = position.board();
        var king = (King) board.pieceAt("e1");
        var rook = (Rook) board.pieceAt("a1");

        var expectedCastles =
                new Move[] {Move.createCastle(false, king, Coordinate.of("c1"), rook, Coordinate.of("d1"))};

        assertThat(generator.calculateCastles()).hasSize(1).containsOnlyOnce(expectedCastles);
    }
}
