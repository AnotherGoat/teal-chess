/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser.fen;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.position.Position;
import org.junit.jupiter.api.Test;

final class FenSerializerTest {

    @Test
    void initialPosition() {
        var initialPosition = Position.INITIAL_POSITION;
        var initialPositionFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        assertThat(FenSerializer.serialize(initialPosition)).isEqualTo(initialPositionFen);
    }

    @Test
    void serializePosition() {
        // Board from the explained Chess.com example at https://www.chess.com/terms/fen-chess
        var fen = "r1bk3r/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1 w - - 0 1";
        var position = FenParser.parse(fen);

        assertThat(FenSerializer.serialize(position)).isEqualTo(fen);
    }

    @Test
    void longestFen() {
        // Theoretical longest possible FEN example at
        // https://chess.stackexchange.com/questions/30004/longest-possible-fen#comment47908_30006
        var fen = "r1b1k2r/1n1q1p1p/p1p1p1p1/1p1pP1b1/1N1P1P1P/P1P1Q1P1/1P1nN1B1/R1B1K2R w KQkq - 100 1000";
        var position = FenParser.parse(fen);

        assertThat(FenSerializer.serialize(position)).isEqualTo(fen);
    }

    @Test
    void whiteEnPassantTarget() {
        // From PGN specification example at http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c16.1.4
        var fen = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
        var position = FenParser.parse(fen);

        assertThat(FenSerializer.serialize(position)).isEqualTo(fen);
    }

    @Test
    void blackEnPassantTarget() {
        // From PGN specification example at http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c16.1.4
        var fen = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";
        var position = FenParser.parse(fen);

        assertThat(FenSerializer.serialize(position)).isEqualTo(fen);
    }
}
