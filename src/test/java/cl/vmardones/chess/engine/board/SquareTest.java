/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.piece.Knight;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.piece.Rook;
import cl.vmardones.chess.engine.player.Alliance;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SquareTest {

    @Mock
    Coordinate anywhere;

    @Test
    void createOccupied() {
        var piece = new Knight(anywhere, Alliance.BLACK);
        assertThat(Square.create(anywhere, piece).piece()).isNotNull().isEqualTo(piece);
    }

    @Test
    void createEmpty() {
        assertThat(Square.create(anywhere, null).piece()).isNull();
    }

    @Test
    void cache() {
        assertThat(Square.create(Coordinate.of("g5"), null)).isEqualTo(Square.create(Coordinate.of("g5"), null));
    }

    @Test
    void whitePieceToString() {
        var piece = new Pawn(anywhere, Alliance.WHITE);
        assertThat(Square.create(anywhere, piece)).hasToString("P");
    }

    @Test
    void blackPieceToString() {
        var piece = new Rook(anywhere, Alliance.BLACK);
        assertThat(Square.create(anywhere, piece)).hasToString("r");
    }

    @Test
    void emptyToString() {
        assertThat(Square.create(anywhere, null)).hasToString("-");
    }

    @Test
    void getCoordinate() {
        assertThat(Square.create(Coordinate.of("c7"), null).coordinate())
                .isNotNull()
                .isEqualTo(Coordinate.of("c7"));
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Square.class).withNonnullFields("coordinate").verify();
    }
}
