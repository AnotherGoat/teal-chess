/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

public enum PieceType {
    /**
     * The pawn piece. It only moves forward (depending on the side) and can eat other pieces
     * diagonally. A very weak piece, but it can be promoted when it gets to the last rank at the
     * opposite side.
     * @see <a href="https://www.chessprogramming.org/Pawn">Pawn</a>
     */
    PAWN(1, "P", "p", "♙", "♟"),
    /**
     * The knight piece. It moves in an L shape.
     * @see <a href="https://www.chessprogramming.org/Knight">Knight</a>
     */
    KNIGHT(3, "N", "n", "♘", "♞"),
    /**
     * The bishop piece. It can move diagonally.
     * @see <a href="https://www.chessprogramming.org/Bishop">Bishop</a>
     */
    BISHOP(3, "B", "b", "♗", "♝"),
    /**
     * The rook piece. It can move horizontally and vertically.
     * @see <a href="https://www.chessprogramming.org/Rook">Rook</a>
     */
    ROOK(5, "R", "r", "♖", "♜"),
    /**
     * The queen, the strongest piece in the game and the most common promoted piece.
     * It can move horizontally, vertically and diagonally.
     * @see <a href="https://www.chessprogramming.org/Queen">Queen</a>
     */
    QUEEN(9, "Q", "q", "♕", "♛"),
    /**
     * The king piece. The most important piece in the game, must be defended at all costs. It moves
     * like the queen, but only one space at a time. It also cannot move into a square where it could
     * be captured the next turn.
     * @see <a href="https://www.chessprogramming.org/King">King</a>
     */
    KING(200, "K", "k", "♔", "♚");

    private final int value;
    private final String whiteFen;
    private final String blackFen;
    private final String whiteUnicode;
    private final String blackUnicode;

    public static PieceType fromSymbol(String symbol) {
        return switch (symbol) {
            case "P", "p" -> PAWN;
            case "N", "n" -> KNIGHT;
            case "B", "b" -> BISHOP;
            case "R", "r" -> ROOK;
            case "Q", "q" -> QUEEN;
            case "K", "k" -> KING;
            default -> throw new PieceSymbolException(symbol);
        };
    }

    /* Getters */

    int value() {
        return value;
    }

    String whiteFen() {
        return whiteFen;
    }

    String blackFen() {
        return blackFen;
    }

    String whiteUnicode() {
        return whiteUnicode;
    }

    String blackUnicode() {
        return blackUnicode;
    }

    PieceType(int value, String whiteFen, String blackFen, String whiteUnicode, String blackUnicode) {
        this.value = value;
        this.whiteFen = whiteFen;
        this.blackFen = blackFen;
        this.whiteUnicode = whiteUnicode;
        this.blackUnicode = blackUnicode;
    }
}
