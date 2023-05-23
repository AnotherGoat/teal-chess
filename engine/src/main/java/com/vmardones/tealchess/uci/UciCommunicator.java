/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.uci;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.vmardones.tealchess.ai.MoveChooser;
import com.vmardones.tealchess.ai.RandomMoveChooser;
import com.vmardones.tealchess.board.Mailbox;
import com.vmardones.tealchess.generator.LegalGenerator;
import com.vmardones.tealchess.generator.MoveGenerator;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.piece.PromotionChoice;
import com.vmardones.tealchess.position.Position;
import com.vmardones.tealchess.square.AlgebraicConverter;

final class UciCommunicator {

    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
    private static final String ENGINE_NAME = "TealChess v0.0";
    private static final String AUTHOR = "Víctor M.";

    private final MoveMaker moveMaker = new MoveMaker();
    private final MoveGenerator legalGenerator = new LegalGenerator();
    private final MoveChooser moveChooser = new RandomMoveChooser();
    private Position position = Position.INITIAL_POSITION;

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
            position = Position.INITIAL_POSITION;
        } else if (positionInput.startsWith("fen ")) {
            var fen = positionInput.substring("fen".length() + 1);
            position = FenParser.parse(fen);
        }

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
                    position = moveMaker.make(position, move);
                } else {
                    var move = new Move(moveType, source, destination);
                    position = moveMaker.make(position, move);
                }
            }
        }
    }

    private MoveType inferMoveType(int source, int destination) {
        var mailbox = new Mailbox(position.board());

        if (mailbox.pieceAt(source).isPawn()) {

            if (mailbox.pieceAt(destination) != null) {
                return MoveType.PAWN_CAPTURE;
            }

            if (position.enPassantTarget() != null && destination == position.enPassantTarget()) {
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
        var legalMoves = legalGenerator.generate(position);

        if (!legalMoves.isEmpty()) {
            var move = moveChooser.chooseMove(position, legalMoves);
            System.out.println("bestmove " + move);
        }
    }

    private void sendPrint() {
        // TODO: Add a pretty print method for the board and use it here
        System.out.println(position.board().unicode());
    }
}
