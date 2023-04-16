package cl.vmardones.chess.engine.board;

/**
 * Exception thrown when board build steps are misused.
 */
public final class BoardConstructionException extends RuntimeException {
    BoardConstructionException(String message) {
        super(message);
    }
}
