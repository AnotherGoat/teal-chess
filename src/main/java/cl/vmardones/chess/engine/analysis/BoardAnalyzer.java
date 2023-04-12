package cl.vmardones.chess.engine.analysis;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveResult;
import cl.vmardones.chess.engine.move.MoveType;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Alliance;
import cl.vmardones.chess.engine.player.Player;

import java.util.List;

public final class BoardAnalyzer {

    private final King king;
    private final List<Piece> pieces;
    private final List<Move> legals;
    private final King opponentKing;
    private final List<Piece> opponentPieces;
    private final List<Move> opponentLegals;

    /* Dependencies */
    private final MoveTester moveTester;
    private final PlayerFactory playerFactory;

    public BoardAnalyzer(Board board, Alliance nextMoveMaker) {
        king = board.king(nextMoveMaker);
        pieces = board.pieces(nextMoveMaker);
        legals = MoveChecker.calculateLegals(board, nextMoveMaker);

        opponentKing = board.king(nextMoveMaker.opposite());
        opponentPieces = board.pieces(nextMoveMaker.opposite());
        opponentLegals = MoveChecker.calculateLegals(board, nextMoveMaker.opposite());

        moveTester = new MoveTester(king, legals, opponentLegals);
        playerFactory = new PlayerFactory(nextMoveMaker, king, pieces, legals, opponentKing, opponentPieces, moveTester);
    }

    public Player createPlayer(Alliance alliance) {
        return playerFactory.create(alliance);
    }

    public MoveResult testMove(Move move) {
        return moveTester.testMove(move);
    }
}
