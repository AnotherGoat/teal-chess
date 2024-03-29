/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.uci;

import static java.util.Collections.emptyMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.vmardones.tealchess.board.Mailbox;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.piece.PromotionChoice;
import com.vmardones.tealchess.search.MoveChooser;
import com.vmardones.tealchess.search.RandomMoveChooser;
import com.vmardones.tealchess.square.AlgebraicConverter;

// TODO: Also use GameState here, to pass the memento
final class UciCommunicator {

    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
    private static final String ENGINE_NAME = "TealChess v0.0";
    private static final String AUTHOR = "Víctor M.";

    private final MoveChooser moveChooser = new RandomMoveChooser();
    private Game game = new Game(emptyMap()).whiteAi(moveChooser).blackAi(moveChooser);

    UciCommunicator() {}

    void start() throws IOException {
        System.out.println("Welcome to " + ENGINE_NAME + " by " + AUTHOR);

        while (true) {
            var input = READER.readLine();

            if (input.equals("quit") || input.equals("exit")) {
                break;
            }

            if (input.equals("uci")) {
                sendUciInfo();
            } else if (input.startsWith("setoption ")) {
                sendSetOption(input);
            } else if (input.equals("isready")) {
                sendReadyOk();
            } else if (input.equals("ucinewgame")) {
                sendUciNewGame();
            } else if (input.startsWith("position ")) {
                sendPosition(input.substring("position".length() + 1));
            } else if (input.equals("go")) {
                sendGo();
            } else if (input.equals("d")) {
                sendPrint();
            }
        }
    }

    private void sendUciInfo() {
        System.out.println("id name " + ENGINE_NAME);
        System.out.println("id author " + AUTHOR);
        sendOptions();
        System.out.println("uciok");
    }

    private void sendOptions() {}

    private void sendSetOption(String input) {}

    private void sendReadyOk() {
        System.out.println("readyok");
    }

    private void sendUciNewGame() {}

    private void sendPosition(String input) {
        String positionInput;
        String movesInput;

        if (input.contains(" moves ")) {
            var splitInput = input.split(" moves ");
            positionInput = splitInput[0];
            movesInput = splitInput[1];
        } else {
            positionInput = input;
            movesInput = "";
        }

        if (positionInput.equals("startpos")) {
            game = new Game(emptyMap());
        } else if (positionInput.startsWith("fen ")) {
            var fen = positionInput.substring("fen".length() + 1);
            game = new Game(emptyMap(), FenParser.parse(fen));
        }

        game.whiteAi(moveChooser).blackAi(moveChooser);

        if (!movesInput.isBlank()) {
            var moves = movesInput.split(" ");

            for (var moveText : moves) {
                if (moveText.length() < 4 || moveText.length() > 5) {
                    break;
                }

                var source = AlgebraicConverter.toSquare(moveText.substring(0, 2));
                var destination = AlgebraicConverter.toSquare(moveText.substring(2, 4));

                var moveType = inferMoveType(source, destination);

                if (moveText.length() == 5) {
                    var promotionChoice = PromotionChoice.fromSymbol(moveText.substring(4));
                    var move = new Move(moveType, source, destination, promotionChoice);
                    game.makeMove(move);
                } else {
                    var move = new Move(moveType, source, destination);
                    game.makeMove(move);
                }
            }
        }
    }

    // TODO: Move this method to a parser class
    private MoveType inferMoveType(int source, int destination) {
        var mailbox = new Mailbox(game.board());

        if (mailbox.pieceAt(source).isPawn()) {

            if (mailbox.pieceAt(destination) != null) {
                return MoveType.PAWN_CAPTURE;
            }

            if (game.enPassantTarget() != null && destination == game.enPassantTarget()) {
                return MoveType.EN_PASSANT;
            }

            var sourceRank = AlgebraicConverter.rankIndex(source);
            var destinationRank = AlgebraicConverter.rankIndex(destination);

            if (Math.abs(sourceRank - destinationRank) == 2) {
                return MoveType.DOUBLE_PUSH;
            }

            return MoveType.PAWN_PUSH;
        }

        if (mailbox.pieceAt(source).isKing()) {
            if (destination - source == 2) {
                return MoveType.SHORT_CASTLE;
            }

            if (destination - source == -2) {
                return MoveType.LONG_CASTLE;
            }
        }

        return mailbox.isEmpty(destination) ? MoveType.NORMAL : MoveType.CAPTURE;
    }

    private void sendGo() {
        var legalMoves = game.legalMoves();

        if (!legalMoves.isEmpty()) {
            var move = game.chooseAiMove();
            System.out.println("bestmove " + move);
        }
    }

    private void sendPrint() {
        // TODO: Add a pretty print method for the board and use it here
        System.out.println(game.board().unicode());
    }
}
