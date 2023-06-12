/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.integration;

import static com.vmardones.tealchess.move.Move.BLACK_LONG_CASTLE_STEPS;
import static com.vmardones.tealchess.move.Move.WHITE_LONG_CASTLE_STEPS;
import static com.vmardones.tealchess.move.MoveType.*;
import static com.vmardones.tealchess.parser.fen.FenParser.parse;
import static com.vmardones.tealchess.player.PlayerStatus.*;
import static com.vmardones.tealchess.position.Position.INITIAL_POSITION;
import static com.vmardones.tealchess.square.Square.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.player.PlayerStatus;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.square.Coordinate;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class FullGameTest {

    // Kasparov vs. Topalov, Wijk aan Zee 1999
    // https://www.chess.com/article/view/the-best-chess-games-of-all-time#Kasparov_Topalov
    @Test
    void fullGame() {
        var tags = new HashMap<String, String>();
        tags.put("Event", "It (cat.17)");
        tags.put("Site", "Wijk aan Zee (Netherlands)");
        tags.put("Date", "1999.??.??");
        tags.put("Round", "?");
        tags.put("White", "Garry Kasparov");
        tags.put("Black", "Veselin Topalov");
        tags.put("Result", "1-0");

        var game = new Game(tags);

        for (var turn : turns()) {
            var position = turn.position;
            assertThat(game.board()).isEqualTo(position.board());
            assertThat(game.sideToMove()).isEqualTo(position.sideToMove());
            assertThat(game.castlingRights()).isEqualTo(position.castlingRights());
            assertThat(game.enPassantTarget()).isEqualTo(position.enPassantTarget());
            assertThat(game.halfmoveClock()).isEqualTo(position.halfmoveClock());
            assertThat(game.fullmoveCounter()).isEqualTo(position.fullmoveCounter());

            var playerStatus = turn.playerStatus;
            assertThat(game.playerStatus()).isEqualTo(playerStatus);

            var selectedSquare = turn.selectedPiece;

            if (selectedSquare != -1) {
                var selectedPiece = Coordinate.forSquare(selectedSquare);
                var destinations = Arrays.stream(turn.destinations)
                        .mapToObj(Coordinate::forSquare)
                        .toArray(Coordinate[]::new);
                assertThat(game.findLegalDestinations(selectedPiece))
                        .hasSize(destinations.length)
                        .containsOnlyOnce(destinations);

                var move = turn.move;
                game.makeMove(move);
            }
        }
    }

    record TurnData(Position position, PlayerStatus playerStatus, int selectedPiece, int[] destinations, Move move) {}

    List<TurnData> turns() {
        return List.of(
                new TurnData(INITIAL_POSITION, OK, e2, new int[] {e3, e4}, new Move(DOUBLE_PUSH, e2, e4)),
                new TurnData(
                        parse("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"),
                        OK,
                        d7,
                        new int[] {d6, d5},
                        new Move(PAWN_PUSH, d7, d6)),
                new TurnData(
                        parse("rnbqkbnr/ppp1pppp/3p4/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2"),
                        OK,
                        d2,
                        new int[] {d3, d4},
                        new Move(DOUBLE_PUSH, d2, d4)),
                new TurnData(
                        parse("rnbqkbnr/ppp1pppp/3p4/8/3PP3/8/PPP2PPP/RNBQKBNR b KQkq d3 0 2"),
                        OK,
                        g8,
                        new int[] {f6, h6},
                        new Move(NORMAL, g8, f6)),
                new TurnData(
                        parse("rnbqkb1r/ppp1pppp/3p1n2/8/3PP3/8/PPP2PPP/RNBQKBNR w KQkq - 1 3"),
                        OK,
                        b1,
                        new int[] {a3, c3, d2},
                        new Move(NORMAL, b1, c3)),
                new TurnData(
                        parse("rnbqkb1r/ppp1pppp/3p1n2/8/3PP3/2N5/PPP2PPP/R1BQKBNR b KQkq - 2 3"),
                        OK,
                        g7,
                        new int[] {g6, g5},
                        new Move(PAWN_PUSH, g7, g6)),
                new TurnData(
                        parse("rnbqkb1r/ppp1pp1p/3p1np1/8/3PP3/2N5/PPP2PPP/R1BQKBNR w KQkq - 0 4"),
                        OK,
                        c1,
                        new int[] {d2, e3, f4, g5, h6},
                        new Move(NORMAL, c1, e3)),
                new TurnData(
                        parse("rnbqkb1r/ppp1pp1p/3p1np1/8/3PP3/2N1B3/PPP2PPP/R2QKBNR b KQkq - 1 4"),
                        OK,
                        f8,
                        new int[] {g7, h6},
                        new Move(NORMAL, f8, g7)),
                new TurnData(
                        parse("rnbqk2r/ppp1ppbp/3p1np1/8/3PP3/2N1B3/PPP2PPP/R2QKBNR w KQkq - 2 5"),
                        OK,
                        d1,
                        new int[] {b1, c1, d2, d3, e2, f3, g4, h5},
                        new Move(NORMAL, d1, d2)),
                new TurnData(
                        parse("rnbqk2r/ppp1ppbp/3p1np1/8/3PP3/2N1B3/PPPQ1PPP/R3KBNR b KQkq - 3 5"),
                        OK,
                        c7,
                        new int[] {c6, c5},
                        new Move(PAWN_PUSH, c7, c6)),
                new TurnData(
                        parse("rnbqk2r/pp2ppbp/2pp1np1/8/3PP3/2N1B3/PPPQ1PPP/R3KBNR w KQkq - 0 6"),
                        OK,
                        f2,
                        new int[] {f3, f4},
                        new Move(PAWN_PUSH, f2, f3)),
                new TurnData(
                        parse("rnbqk2r/pp2ppbp/2pp1np1/8/3PP3/2N1BP2/PPPQ2PP/R3KBNR b KQkq - 0 6"),
                        OK,
                        b7,
                        new int[] {b6, b5},
                        new Move(DOUBLE_PUSH, b7, b5)),
                new TurnData(
                        parse("rnbqk2r/p3ppbp/2pp1np1/1p6/3PP3/2N1BP2/PPPQ2PP/R3KBNR w KQkq b6 0 7"),
                        OK,
                        g1,
                        new int[] {e2, h3},
                        new Move(NORMAL, g1, e2)),
                new TurnData(
                        parse("rnbqk2r/p3ppbp/2pp1np1/1p6/3PP3/2N1BP2/PPPQN1PP/R3KB1R b KQkq - 1 7"),
                        OK,
                        b8,
                        new int[] {a6, d7},
                        new Move(NORMAL, b8, d7)),
                new TurnData(
                        parse("r1bqk2r/p2nppbp/2pp1np1/1p6/3PP3/2N1BP2/PPPQN1PP/R3KB1R w KQkq - 2 8"),
                        OK,
                        e3,
                        new int[] {f2, g1, f4, g5, h6},
                        new Move(NORMAL, e3, h6)),
                new TurnData(
                        parse("r1bqk2r/p2nppbp/2pp1npB/1p6/3PP3/2N2P2/PPPQN1PP/R3KB1R b KQkq - 3 8"),
                        OK,
                        g7,
                        new int[] {f8, h6},
                        new Move(CAPTURE, g7, h6)),
                new TurnData(
                        parse("r1bqk2r/p2npp1p/2pp1npb/1p6/3PP3/2N2P2/PPPQN1PP/R3KB1R w KQkq - 0 9"),
                        OK,
                        d2,
                        new int[] {c1, d1, d3, e3, f4, g5, h6},
                        new Move(CAPTURE, d2, h6)),
                new TurnData(
                        parse("r1bqk2r/p2npp1p/2pp1npQ/1p6/3PP3/2N2P2/PPP1N1PP/R3KB1R b KQkq - 0 9"),
                        OK,
                        c8,
                        new int[] {b7, a6},
                        new Move(NORMAL, c8, b7)),
                new TurnData(
                        parse("r2qk2r/pb1npp1p/2pp1npQ/1p6/3PP3/2N2P2/PPP1N1PP/R3KB1R w KQkq - 1 10"),
                        OK,
                        a2,
                        new int[] {a3, a4},
                        new Move(PAWN_PUSH, a2, a3)),
                new TurnData(
                        parse("r2qk2r/pb1npp1p/2pp1npQ/1p6/3PP3/P1N2P2/1PP1N1PP/R3KB1R b KQkq - 0 10"),
                        OK,
                        e7,
                        new int[] {e6, e5},
                        new Move(DOUBLE_PUSH, e7, e5)),
                new TurnData(
                        parse("r2qk2r/pb1n1p1p/2pp1npQ/1p2p3/3PP3/P1N2P2/1PP1N1PP/R3KB1R w KQkq e6 0 11"),
                        OK,
                        e1,
                        new int[] {d1, c1, d2, f2},
                        WHITE_LONG_CASTLE_STEPS.get(0)),
                new TurnData(
                        parse("r2qk2r/pb1n1p1p/2pp1npQ/1p2p3/3PP3/P1N2P2/1PP1N1PP/2KR1B1R b kq - 1 11"),
                        OK,
                        d8,
                        new int[] {b8, c8, c7, b6, a5, e7},
                        new Move(NORMAL, d8, e7)),
                new TurnData(
                        parse("r3k2r/pb1nqp1p/2pp1npQ/1p2p3/3PP3/P1N2P2/1PP1N1PP/2KR1B1R w kq - 2 12"),
                        OK,
                        c1,
                        new int[] {b1, d2},
                        new Move(NORMAL, c1, b1)),
                new TurnData(
                        parse("r3k2r/pb1nqp1p/2pp1npQ/1p2p3/3PP3/P1N2P2/1PP1N1PP/1K1R1B1R b kq - 3 12"),
                        OK,
                        a7,
                        new int[] {a6, a5},
                        new Move(PAWN_PUSH, a7, a6)),
                new TurnData(
                        parse("r3k2r/1b1nqp1p/p1pp1npQ/1p2p3/3PP3/P1N2P2/1PP1N1PP/1K1R1B1R w kq - 0 13"),
                        OK,
                        e2,
                        new int[] {c1, g1, g3, f4},
                        new Move(NORMAL, e2, c1)),
                new TurnData(
                        parse("r3k2r/1b1nqp1p/p1pp1npQ/1p2p3/3PP3/P1N2P2/1PP3PP/1KNR1B1R b kq - 1 13"),
                        OK,
                        e8,
                        new int[] {d8, c8},
                        BLACK_LONG_CASTLE_STEPS.get(0)),
                new TurnData(
                        parse("2kr3r/1b1nqp1p/p1pp1npQ/1p2p3/3PP3/P1N2P2/1PP3PP/1KNR1B1R w - - 2 14"),
                        OK,
                        c1,
                        new int[] {a2, b3, d3, e2},
                        new Move(NORMAL, c1, b3)),
                new TurnData(
                        parse("2kr3r/1b1nqp1p/p1pp1npQ/1p2p3/3PP3/PNN2P2/1PP3PP/1K1R1B1R b - - 3 14"),
                        OK,
                        e5,
                        new int[] {d4},
                        new Move(PAWN_CAPTURE, e5, d4)),
                new TurnData(
                        parse("2kr3r/1b1nqp1p/p1pp1npQ/1p6/3pP3/PNN2P2/1PP3PP/1K1R1B1R w - - 0 15"),
                        OK,
                        d1,
                        new int[] {c1, e1, d2, d3, d4},
                        new Move(CAPTURE, d1, d4)),
                new TurnData(
                        parse("2kr3r/1b1nqp1p/p1pp1npQ/1p6/3RP3/PNN2P2/1PP3PP/1K3B1R b - - 0 15"),
                        OK,
                        c6,
                        new int[] {c5},
                        new Move(PAWN_PUSH, c6, c5)),
                new TurnData(
                        parse("2kr3r/1b1nqp1p/p2p1npQ/1pp5/3RP3/PNN2P2/1PP3PP/1K3B1R w - - 0 16"),
                        OK,
                        d4,
                        new int[] {a4, b4, c4, d1, d2, d3, d5, d6},
                        new Move(NORMAL, d4, d1)),
                new TurnData(
                        parse("2kr3r/1b1nqp1p/p2p1npQ/1pp5/4P3/PNN2P2/1PP3PP/1K1R1B1R b - - 1 16"),
                        OK,
                        d7,
                        new int[] {b8, b6, e5, f8},
                        new Move(NORMAL, d7, b6)),
                new TurnData(
                        parse("2kr3r/1b2qp1p/pn1p1npQ/1pp5/4P3/PNN2P2/1PP3PP/1K1R1B1R w - - 2 17"),
                        OK,
                        g2,
                        new int[] {g3, g4},
                        new Move(PAWN_PUSH, g2, g3)),
                new TurnData(
                        parse("2kr3r/1b2qp1p/pn1p1npQ/1pp5/4P3/PNN2PP1/1PP4P/1K1R1B1R b - - 0 17"),
                        OK,
                        c8,
                        new int[] {b8, c7, d7},
                        new Move(NORMAL, c8, b8)),
                new TurnData(
                        parse("1k1r3r/1b2qp1p/pn1p1npQ/1pp5/4P3/PNN2PP1/1PP4P/1K1R1B1R w - - 1 18"),
                        OK,
                        b3,
                        new int[] {a1, c1, d2, d4, a5, c5},
                        new Move(NORMAL, b3, a5)),
                new TurnData(
                        parse("1k1r3r/1b2qp1p/pn1p1npQ/Npp5/4P3/P1N2PP1/1PP4P/1K1R1B1R b - - 2 18"),
                        OK,
                        b7,
                        new int[] {a8, c8, c6, d5, e4},
                        new Move(NORMAL, b7, a8)),
                new TurnData(
                        parse("bk1r3r/4qp1p/pn1p1npQ/Npp5/4P3/P1N2PP1/1PP4P/1K1R1B1R w - - 3 19"),
                        OK,
                        f1,
                        new int[] {e2, d3, c4, b5, g2, h3},
                        new Move(NORMAL, f1, h3)),
                new TurnData(
                        parse("bk1r3r/4qp1p/pn1p1npQ/Npp5/4P3/P1N2PPB/1PP4P/1K1R3R b - - 4 19"),
                        OK,
                        d6,
                        new int[] {d5},
                        new Move(PAWN_PUSH, d6, d5)),
                new TurnData(
                        parse("bk1r3r/4qp1p/pn3npQ/Nppp4/4P3/P1N2PPB/1PP4P/1K1R3R w - - 0 20"),
                        OK,
                        h6,
                        new int[] {h7, h5, h4, g7, f8, g6, g5, f4, e3, d2, c1},
                        new Move(NORMAL, h6, f4)),
                new TurnData(
                        parse("bk1r3r/4qp1p/pn3np1/Nppp4/4PQ2/P1N2PPB/1PP4P/1K1R3R b - - 1 20"),
                        CHECKED,
                        b8,
                        new int[] {a7},
                        new Move(NORMAL, b8, a7)),
                new TurnData(
                        parse("b2r3r/k3qp1p/pn3np1/Nppp4/4PQ2/P1N2PPB/1PP4P/1K1R3R w - - 2 21"),
                        OK,
                        h1,
                        new int[] {g1, f1, e1},
                        new Move(NORMAL, h1, e1)),
                new TurnData(
                        parse("b2r3r/k3qp1p/pn3np1/Nppp4/4PQ2/P1N2PPB/1PP4P/1K1RR3 b - - 3 21"),
                        OK,
                        d5,
                        new int[] {d4, e4},
                        new Move(PAWN_PUSH, d5, d4)),
                new TurnData(
                        parse("b2r3r/k3qp1p/pn3np1/Npp5/3pPQ2/P1N2PPB/1PP4P/1K1RR3 w - - 0 22"),
                        OK,
                        c3,
                        new int[] {b5, d5, a2, a4, e2},
                        new Move(NORMAL, c3, d5)),
                new TurnData(
                        parse("b2r3r/k3qp1p/pn3np1/NppN4/3pPQ2/P4PPB/1PP4P/1K1RR3 b - - 1 22"),
                        OK,
                        b6,
                        new int[] {c8, d7, d5, a4, c4},
                        new Move(CAPTURE, b6, d5)),
                new TurnData(
                        parse("b2r3r/k3qp1p/p4np1/Nppn4/3pPQ2/P4PPB/1PP4P/1K1RR3 w - - 0 23"),
                        OK,
                        e4,
                        new int[] {d5, e5},
                        new Move(PAWN_CAPTURE, e4, d5)),
                new TurnData(
                        parse("b2r3r/k3qp1p/p4np1/NppP4/3p1Q2/P4PPB/1PP4P/1K1RR3 b - - 0 23"),
                        OK,
                        e7,
                        new int[] {e8, f8, b7, c7, d7, d6, e1, e2, e3, e4, e5, e6},
                        new Move(NORMAL, e7, d6)),
                new TurnData(
                        parse("b2r3r/k4p1p/p2q1np1/NppP4/3p1Q2/P4PPB/1PP4P/1K1RR3 w - - 1 24"),
                        OK,
                        d1,
                        new int[] {c1, d2, d3, d4},
                        new Move(CAPTURE, d1, d4)),
                new TurnData(
                        parse("b2r3r/k4p1p/p2q1np1/NppP4/3R1Q2/P4PPB/1PP4P/1K2R3 b - - 0 24"),
                        OK,
                        c5,
                        new int[] {c4, d4},
                        new Move(PAWN_CAPTURE, c5, d4)),
                new TurnData(
                        parse("b2r3r/k4p1p/p2q1np1/Np1P4/3p1Q2/P4PPB/1PP4P/1K2R3 w - - 0 25"),
                        OK,
                        e1,
                        new int[] {c1, d1, f1, g1, h1, e2, e3, e4, e5, e6, e7, e8},
                        new Move(NORMAL, e1, e7)),
                new TurnData(
                        parse("b2r3r/k3Rp1p/p2q1np1/Np1P4/3p1Q2/P4PPB/1PP4P/1K6 b - - 1 25"),
                        CHECKED,
                        a7,
                        new int[] {b8, b6},
                        new Move(NORMAL, a7, b6)),
                new TurnData(
                        parse("b2r3r/4Rp1p/pk1q1np1/Np1P4/3p1Q2/P4PPB/1PP4P/1K6 w - - 2 26"),
                        OK,
                        f4,
                        new int[] {e5, d6, f5, f6, g5, h6, g4, h4, c1, d2, e3, e4, d4},
                        new Move(CAPTURE, f4, d4)),
                new TurnData(
                        parse("b2r3r/4Rp1p/pk1q1np1/Np1P4/3Q4/P4PPB/1PP4P/1K6 b - - 0 26"),
                        CHECKED,
                        b6,
                        new int[] {a5},
                        new Move(CAPTURE, b6, a5)),
                new TurnData(
                        parse("b2r3r/4Rp1p/p2q1np1/kp1P4/3Q4/P4PPB/1PP4P/1K6 w - - 0 27"),
                        OK,
                        b2,
                        new int[] {b3, b4},
                        new Move(DOUBLE_PUSH, b2, b4)),
                new TurnData(
                        parse("b2r3r/4Rp1p/p2q1np1/kp1P4/1P1Q4/P4PPB/2P4P/1K6 b - b3 0 27"),
                        CHECKED,
                        a5,
                        new int[] {a4},
                        new Move(NORMAL, a5, a4)),
                new TurnData(
                        parse("b2r3r/4Rp1p/p2q1np1/1p1P4/kP1Q4/P4PPB/2P4P/1K6 w - - 1 28"),
                        OK,
                        d4,
                        new int[] {c5, b6, a7, e5, f6, c4, e4, f4, g4, h4, e3, f2, g1, d1, d2, d3, a1, b2, c3},
                        new Move(NORMAL, d4, c3)),
                new TurnData(
                        parse("b2r3r/4Rp1p/p2q1np1/1p1P4/kP6/P1Q2PPB/2P4P/1K6 b - - 2 28"),
                        OK,
                        d6,
                        new int[] {b8, c7, b6, c6, e6, e7, d7, c5, b4, d5, e5, f4, g3},
                        new Move(CAPTURE, d6, d5)),
                new TurnData(
                        parse("b2r3r/4Rp1p/p4np1/1p1q4/kP6/P1Q2PPB/2P4P/1K6 w - - 0 29"),
                        OK,
                        e7,
                        new int[] {a7, b7, c7, d7, f7, e1, e2, e3, e4, e5, e6, e8},
                        new Move(NORMAL, e7, a7)),
                new TurnData(
                        parse("b2r3r/R4p1p/p4np1/1p1q4/kP6/P1Q2PPB/2P4P/1K6 b - - 1 29"),
                        OK,
                        a8,
                        new int[] {b7, c6},
                        new Move(NORMAL, a8, b7)),
                new TurnData(
                        parse("3r3r/Rb3p1p/p4np1/1p1q4/kP6/P1Q2PPB/2P4P/1K6 w - - 2 30"),
                        OK,
                        a7,
                        new int[] {a6, a8, b7},
                        new Move(CAPTURE, a7, b7)),
                new TurnData(
                        parse("3r3r/1R3p1p/p4np1/1p1q4/kP6/P1Q2PPB/2P4P/1K6 b - - 0 30"),
                        OK,
                        d5,
                        new int[] {d1, d2, d3, d4, d6, d7, c5, e5, f5, g5, h5, e6, c6, b7, e4, f3, a2, b3, c4},
                        new Move(NORMAL, d5, c4)),
                new TurnData(
                        parse("3r3r/1R3p1p/p4np1/1p6/kPq5/P1Q2PPB/2P4P/1K6 w - - 1 31"),
                        OK,
                        c3,
                        new int[] {a1, b2, b3, d2, e1, d3, e3, c4, d4, e5, f6},
                        new Move(CAPTURE, c3, f6)),
                new TurnData(
                        parse("3r3r/1R3p1p/p4Qp1/1p6/kPq5/P4PPB/2P4P/1K6 b - - 0 31"),
                        OK,
                        a4,
                        new int[] {a3},
                        new Move(CAPTURE, a4, a3)),
                new TurnData(
                        parse("3r3r/1R3p1p/p4Qp1/1p6/1Pq5/k4PPB/2P4P/1K6 w - - 0 32"),
                        OK,
                        f6,
                        new int[] {f7, g6, g7, h8, g5, h4, f4, f5, a1, b2, c3, d4, e5, a6, b6, c6, d6, e6, d8, e7},
                        new Move(CAPTURE, f6, a6)),
                new TurnData(
                        parse("3r3r/1R3p1p/Q5p1/1p6/1Pq5/k4PPB/2P4P/1K6 b - - 0 32"),
                        CHECKED,
                        a3,
                        new int[] {b4},
                        new Move(CAPTURE, a3, b4)),
                new TurnData(
                        parse("3r3r/1R3p1p/Q5p1/1p6/1kq5/5PPB/2P4P/1K6 w - - 0 33"),
                        OK,
                        c2,
                        new int[] {c3},
                        new Move(PAWN_PUSH, c2, c3)),
                new TurnData(
                        parse("3r3r/1R3p1p/Q5p1/1p6/1kq5/2P2PPB/7P/1K6 b - - 0 33"),
                        CHECKED,
                        b4,
                        new int[] {b3, c3, c5},
                        new Move(CAPTURE, b4, c3)),
                new TurnData(
                        parse("3r3r/1R3p1p/Q5p1/1p6/2q5/2k2PPB/7P/1K6 w - - 0 34"),
                        OK,
                        a6,
                        new int[] {a1, a2, a3, a4, a5, a7, a8, b5, b6, c6, d6, e6, f6, g6},
                        new Move(NORMAL, a6, a1)),
                new TurnData(
                        parse("3r3r/1R3p1p/6p1/1p6/2q5/2k2PPB/7P/QK6 b - - 1 34"),
                        CHECKED,
                        c3,
                        new int[] {b3, b4, d2, d3},
                        new Move(NORMAL, c3, d2)),
                new TurnData(
                        parse("3r3r/1R3p1p/6p1/1p6/2q5/5PPB/3k3P/QK6 w - - 2 35"),
                        OK,
                        a1,
                        new int[] {a2, a3, a4, a5, a6, a7, a8, b2, c3, d4, e5, f6, g7, h8},
                        new Move(NORMAL, a1, b2)),
                new TurnData(
                        parse("3r3r/1R3p1p/6p1/1p6/2q5/5PPB/1Q1k3P/1K6 b - - 3 35"),
                        CHECKED,
                        d2,
                        new int[] {d1, e1, d3, e3},
                        new Move(NORMAL, d2, d1)),
                new TurnData(
                        parse("3r3r/1R3p1p/6p1/1p6/2q5/5PPB/1Q5P/1K1k4 w - - 4 36"),
                        OK,
                        h3,
                        new int[] {f1, g2, g4, f5, e6, d7, c8},
                        new Move(NORMAL, h3, f1)),
                new TurnData(
                        parse("3r3r/1R3p1p/6p1/1p6/2q5/5PP1/1Q5P/1K1k1B2 b - - 5 36"),
                        OK,
                        d8,
                        new int[] {a8, b8, c8, e8, f8, g8, d2, d3, d4, d5, d6, d7},
                        new Move(NORMAL, d8, d2)),
                new TurnData(
                        parse("7r/1R3p1p/6p1/1p6/2q5/5PP1/1Q1r3P/1K1k1B2 w - - 6 37"),
                        OK,
                        b7,
                        new int[] {b5, b6, b8, a7, c7, d7, e7, f7},
                        new Move(NORMAL, b7, d7)),
                new TurnData(
                        parse("7r/3R1p1p/6p1/1p6/2q5/5PP1/1Q1r3P/1K1k1B2 b - - 7 37"),
                        OK,
                        d2,
                        new int[] {d3, d4, d5, d6, d7},
                        new Move(CAPTURE, d2, d7)),
                new TurnData(
                        parse("7r/3r1p1p/6p1/1p6/2q5/5PP1/1Q5P/1K1k1B2 w - - 0 38"),
                        OK,
                        f1,
                        new int[] {e2, d3, c4, g2, h3},
                        new Move(CAPTURE, f1, c4)),
                new TurnData(
                        parse("7r/3r1p1p/6p1/1p6/2B5/5PP1/1Q5P/1K1k4 b - - 0 38"),
                        OK,
                        b5,
                        new int[] {b4, c4},
                        new Move(PAWN_CAPTURE, b5, c4)),
                new TurnData(
                        parse("7r/3r1p1p/6p1/8/2p5/5PP1/1Q5P/1K1k4 w - - 0 39"),
                        OK,
                        b2,
                        new int[] {a1, a2, a3, c1, c2, d2, e2, f2, g2, b3, b4, b5, b6, b7, b8, c3, d4, e5, f6, g7, h8},
                        new Move(CAPTURE, b2, h8)),
                new TurnData(
                        parse("7Q/3r1p1p/6p1/8/2p5/5PP1/7P/1K1k4 b - - 0 39"),
                        OK,
                        d7,
                        new int[] {d2, d3, d4, d5, d6, d8, a7, b7, c7, e7},
                        new Move(NORMAL, d7, d3)),
                new TurnData(
                        parse("7Q/5p1p/6p1/8/2p5/3r1PP1/7P/1K1k4 w - - 1 40"),
                        OK,
                        h8,
                        new int[] {h7, a8, b8, c8, d8, e8, f8, g8, a1, b2, c3, d4, e5, f6, g7},
                        new Move(NORMAL, h8, a8)),
                new TurnData(
                        parse("Q7/5p1p/6p1/8/2p5/3r1PP1/7P/1K1k4 b - - 2 40"),
                        OK,
                        c4,
                        new int[] {c3},
                        new Move(PAWN_PUSH, c4, c3)),
                new TurnData(
                        parse("Q7/5p1p/6p1/8/8/2pr1PP1/7P/1K1k4 w - - 0 41"),
                        OK,
                        a8,
                        new int[] {b8, c8, d8, e8, f8, g8, h8, a1, a2, a3, a4, a5, a6, a7, b7, c6, d5, e4},
                        new Move(NORMAL, a8, a4)),
                new TurnData(
                        parse("8/5p1p/6p1/8/Q7/2pr1PP1/7P/1K1k4 b - - 1 41"),
                        CHECKED,
                        d1,
                        new int[] {e1, d2, e2},
                        new Move(NORMAL, d1, e1)),
                new TurnData(
                        parse("8/5p1p/6p1/8/Q7/2pr1PP1/7P/1K2k3 w - - 2 42"),
                        OK,
                        f3,
                        new int[] {f4},
                        new Move(PAWN_PUSH, f3, f4)),
                new TurnData(
                        parse("8/5p1p/6p1/8/Q4P2/2pr2P1/7P/1K2k3 b - - 0 42"),
                        OK,
                        f7,
                        new int[] {f6, f5},
                        new Move(DOUBLE_PUSH, f7, f5)),
                new TurnData(
                        parse("8/7p/6p1/5p2/Q4P2/2pr2P1/7P/1K2k3 w - f6 0 43"),
                        OK,
                        b1,
                        new int[] {a1, a2, c2, c1},
                        new Move(NORMAL, b1, c1)),
                new TurnData(
                        parse("8/7p/6p1/5p2/Q4P2/2pr2P1/7P/2K1k3 b - - 1 43"),
                        OK,
                        d3,
                        new int[] {d1, d2, d4, d5, d6, d7, d8, e3, f3, g3},
                        new Move(NORMAL, d3, d2)),
                new TurnData(
                        parse("8/7p/6p1/5p2/Q4P2/2p3P1/3r3P/2K1k3 w - - 2 44"),
                        OK,
                        a4,
                        new int[] {a1, a2, a3, a5, a6, a7, a8, b3, c2, d1, b4, c4, d4, e4, b5, c6, d7, e8},
                        new Move(NORMAL, a4, a7)),
                new TurnData(parse("8/Q6p/6p1/5p2/5P2/2p3P1/3r3P/2K1k3 b - - 3 44"), OK, -1, null, null));
    }
}
