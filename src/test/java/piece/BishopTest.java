package piece;

import org.junit.jupiter.api.Test;
import player.Alliance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BishopTest {

    @Test
    void illegalMove() {
        var bishop = new Bishop(0, Alliance.WHITE);
        assertFalse(bishop.isLegalMove(1));
    }

    @Test
    void diagonalMove() {
        var bishop = new Bishop(0, Alliance.BLACK);
        assertTrue(bishop.isLegalMove(9));
    }
}