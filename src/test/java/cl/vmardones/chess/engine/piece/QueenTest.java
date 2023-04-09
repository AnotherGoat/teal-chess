/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QueenTest {

    @Test
    void constructor() {
        assertThat(new Queen("a1", Alliance.BLACK)).matches(Queen::firstMove);
    }

    @Test
    void toSingleChar() {
        assertThat(new Queen("a1", Alliance.WHITE).singleChar()).isEqualTo("Q");
        assertThat(new Queen("a1", Alliance.BLACK).singleChar()).isEqualTo("q");
    }

    @Test
    void diagonalMove() {
        var queen = new Queen("a1", Alliance.BLACK);
        assertThat(queen.moveVectors()).containsOnlyOnce(new int[] {1, -1});
    }

    @Test
    void horizontalMove() {
        var queen = new Queen("a1", Alliance.BLACK);
        assertThat(queen.moveVectors()).containsOnlyOnce(new int[] {-1, 0});
    }

    @Test
    void verticalMove() {
        var queen = new Queen("a1", Alliance.BLACK);
        assertThat(queen.moveVectors()).containsOnlyOnce(new int[] {0, 1});
    }

    @Test
    void illegalMove() {
        var queen = new Queen("a1", Alliance.BLACK);
        assertThat(queen.moveVectors()).isNotEmpty().doesNotContain(new int[] {-1, 2});
    }

    @Test
    void moveTo() {
        var queenToMove = new Queen("a1", Alliance.BLACK);

        assertThat(queenToMove.moveTo("a2"))
                .isInstanceOf(Queen.class)
                .matches(queen -> queen.position().equals(Position.of("a2")))
                .matches(queen -> !queen.firstMove());
    }
}
