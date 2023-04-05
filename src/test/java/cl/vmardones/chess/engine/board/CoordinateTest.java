/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cl.vmardones.chess.engine.player.Alliance;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class CoordinateTest {

    @Test
    void cache() {
        assertThat(Coordinate.of("c7")).isEqualTo(Coordinate.of("c7"));
    }

    @Test
    void validIndex() {
        assertThat(Coordinate.of(63)).isEqualTo(Coordinate.of("h1"));
    }

    @Test
    void tooLowIndex() {
        assertThatThrownBy(() -> Coordinate.of(-1))
                .isInstanceOf(InvalidCoordinateException.class)
                .hasMessageContaining("-1");
    }

    @Test
    void tooHighIndex() {
        assertThatThrownBy(() -> Coordinate.of(64))
                .isInstanceOf(InvalidCoordinateException.class)
                .hasMessageContaining("64");
    }

    @Test
    void validAlgebraicNotation() {
        assertThat(Coordinate.of("a8")).isEqualTo(Coordinate.of(0));
    }

    @Test
    void invalidAlgebraicNotation() {
        assertThatThrownBy(() -> Coordinate.of("x4"))
                .isInstanceOf(InvalidCoordinateException.class)
                .hasMessageContaining("x4");
    }

    @Test
    void index() {
        assertThat(Coordinate.of("h1").index()).isEqualTo(63);
    }

    @Test
    void getColumn() {
        assertThat(Coordinate.of("f5").column()).isEqualTo('f');
    }

    @Test
    void getColumnIndex() {
        assertThat(Coordinate.of("c3").columnIndex()).isEqualTo(2);
    }

    @Test
    void sameColumn() {
        assertThat(Coordinate.of("g7").sameColumnAs(Coordinate.of("g2"))).isTrue();
    }

    @Test
    void differentColumn() {
        assertThat(Coordinate.of("e1").sameColumnAs(Coordinate.of("f1"))).isFalse();
    }

    @Test
    void getRank() {
        assertThat(Coordinate.of("f5").rank()).isEqualTo(5);
    }

    @Test
    void sameRank() {
        assertThat(Coordinate.of("c1").sameRankAs(Coordinate.of("d1"))).isTrue();
    }

    @Test
    void differentRank() {
        assertThat(Coordinate.of("a4").sameRankAs(Coordinate.of("a5"))).isFalse();
    }

    @Test
    void asString() {
        assertThat(Coordinate.of("e5")).hasToString("e5");
    }

    @Test
    void getWhiteColor() {
        assertThat(Coordinate.of("a8").color()).isEqualTo(Alliance.WHITE);
    }

    @Test
    void getBlackColor() {
        assertThat(Coordinate.of("b8").color()).isEqualTo(Alliance.BLACK);
    }

    @Test
    void sameColor() {
        assertThat(Coordinate.of("a8").sameColorAs(Coordinate.of("h1"))).isTrue();
    }

    @Test
    void differentColor() {
        assertThat(Coordinate.of("a8").sameColorAs(Coordinate.of("h8"))).isFalse();
    }

    @Test
    void to() {
        assertThat(Coordinate.of("b2").to(1, 1)).isNotNull();
        assertThat(Coordinate.of("b2").to(1, 1)).isEqualTo(Coordinate.of("c3"));
    }

    @Test
    void toOutside() {
        assertThat(Coordinate.of("b1").to(-2, 1)).isNull();
    }

    @Test
    void up() {
        assertThat(Coordinate.of("a1").up(2)).isNotNull();
        assertThat(Coordinate.of("a1").up(2)).isEqualTo(Coordinate.of("a3"));
    }

    @Test
    void upOutside() {
        assertThat(Coordinate.of("c8").up(1)).isNull();
    }

    @Test
    void down() {
        assertThat(Coordinate.of("e4").down(2)).isNotNull();
        assertThat(Coordinate.of("e4").down(2)).isEqualTo(Coordinate.of("e2"));
    }

    @Test
    void downOutside() {
        assertThat(Coordinate.of("g1").down(1)).isNull();
    }

    @Test
    void left() {
        assertThat(Coordinate.of("e5").left(2)).isNotNull();
        assertThat(Coordinate.of("e5").left(2)).isEqualTo(Coordinate.of("c5"));
    }

    @Test
    void leftOutside() {
        assertThat(Coordinate.of("a6").left(1)).isNull();
    }

    @Test
    void right() {
        assertThat(Coordinate.of("b5").up(2)).isNotNull();
        assertThat(Coordinate.of("b5").up(2)).isEqualTo(Coordinate.of("b7"));
    }

    @Test
    void rightOutside() {
        assertThat(Coordinate.of("h3").right(1)).isNull();
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Coordinate.class).verify();
    }
}
