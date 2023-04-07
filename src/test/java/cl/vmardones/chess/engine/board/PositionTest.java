/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class PositionTest {

    @Test
    void cache() {
        assertThat(Position.of("c7")).isSameAs(Position.of("c7"));
    }

    @Test
    void validIndex() {
        assertThat(Position.of(63)).isEqualTo(Position.of("h1"));
    }

    @Test
    void tooLowIndex() {
        assertThatThrownBy(() -> Position.of(-1))
                .isInstanceOf(OutsidePositionException.class)
                .hasMessageContaining("-1");
    }

    @Test
    void tooHighIndex() {
        assertThatThrownBy(() -> Position.of(64))
                .isInstanceOf(OutsidePositionException.class)
                .hasMessageContaining("64");
    }

    @Test
    void index() {
        assertThat(Position.of("h1").index()).isEqualTo(63);
    }

    @Test
    void getFile() {
        assertThat(Position.of("f5").file()).isEqualTo('f');
    }

    @Test
    void sameFile() {
        assertThat(Position.of("g7").sameFileAs(Position.of("g2"))).isTrue();
    }

    @Test
    void differentFile() {
        assertThat(Position.of("e1").sameFileAs(Position.of("f1"))).isFalse();
    }

    @Test
    void getRank() {
        assertThat(Position.of("f5").rank()).isEqualTo(5);
    }

    @Test
    void sameRank() {
        assertThat(Position.of("c1").sameRankAs(Position.of("d1"))).isTrue();
    }

    @Test
    void differentRank() {
        assertThat(Position.of("a4").sameRankAs(Position.of("a5"))).isFalse();
    }

    @Test
    void asString() {
        assertThat(Position.of("e5")).hasToString("e5");
    }

    @Test
    void to() {
        assertThat(Position.of("b2").to(1, 1)).isNotNull();
        assertThat(Position.of("b2").to(1, 1)).isEqualTo(Position.of("c3"));
    }

    @Test
    void toOutside() {
        assertThat(Position.of("b1").to(-2, 1)).isNull();
    }

    @Test
    void up() {
        assertThat(Position.of("a1").up(2)).isNotNull();
        assertThat(Position.of("a1").up(2)).isEqualTo(Position.of("a3"));
    }

    @Test
    void upOutside() {
        assertThat(Position.of("c8").up(1)).isNull();
    }

    @Test
    void down() {
        assertThat(Position.of("e4").down(2)).isNotNull();
        assertThat(Position.of("e4").down(2)).isEqualTo(Position.of("e2"));
    }

    @Test
    void downOutside() {
        assertThat(Position.of("g1").down(1)).isNull();
    }

    @Test
    void left() {
        assertThat(Position.of("e5").left(2)).isNotNull();
        assertThat(Position.of("e5").left(2)).isEqualTo(Position.of("c5"));
    }

    @Test
    void leftOutside() {
        assertThat(Position.of("a6").left(1)).isNull();
    }

    @Test
    void right() {
        assertThat(Position.of("b5").up(2)).isNotNull();
        assertThat(Position.of("b5").up(2)).isEqualTo(Position.of("b7"));
    }

    @Test
    void rightOutside() {
        assertThat(Position.of("h3").right(1)).isNull();
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Position.class).verify();
    }
}
