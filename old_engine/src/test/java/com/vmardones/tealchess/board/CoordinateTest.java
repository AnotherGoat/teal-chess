/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

final class CoordinateTest {

    @Test
    void cache() {
        assertThat(Coordinate.of("c7")).isSameAs(Coordinate.of("c7"));
    }

    @Test
    void index() {
        assertThat(Coordinate.of("a8").index()).isZero();
        assertThat(Coordinate.of("h1").index()).isEqualTo(63);
    }

    @Test
    void getFile() {
        assertThat(Coordinate.of("f5").file()).isEqualTo("f");
    }

    @Test
    void getFileIndex() {
        assertThat(Coordinate.of("a6").fileIndex()).isZero();
        assertThat(Coordinate.of("h6").fileIndex()).isEqualTo(7);
    }

    @Test
    void sameFile() {
        assertThat(Coordinate.of("g7").sameFileAs(Coordinate.of("g2"))).isTrue();
    }

    @Test
    void differentFile() {
        assertThat(Coordinate.of("e1").sameFileAs(Coordinate.of("f1"))).isFalse();
    }

    @Test
    void getRank() {
        assertThat(Coordinate.of("f5").rank()).isEqualTo(5);
    }

    @Test
    void getRankIndex() {
        assertThat(Coordinate.of("c1").rankIndex()).isZero();
        assertThat(Coordinate.of("c8").rankIndex()).isEqualTo(7);
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
    void to() {
        assertThat(Coordinate.of("b2").to(1, 1)).isEqualTo(Coordinate.of("c3"));
    }

    @Test
    void toOutside() {
        assertThatThrownBy(() -> Coordinate.of("h8").to(-3, 1))
                .isInstanceOf(CoordinateException.class)
                .hasMessageContaining("(-3, 1)");
    }

    @Test
    void illegalHorizontalJump() {
        assertThatThrownBy(() -> Coordinate.of("a5").to(-1, 0))
                .isInstanceOf(CoordinateException.class)
                .hasMessageContaining("moving -1 units from file a");
    }

    @Test
    void toOrNull() {
        assertThat(Coordinate.of("b2").toOrNull(1, 1)).isNotNull().isEqualTo(Coordinate.of("c3"));
        assertThat(Coordinate.of("b1").toOrNull(-2, 1)).isNull();
    }

    @Test
    void up() {
        assertThat(Coordinate.of("a1").up(2)).isEqualTo(Coordinate.of("a3"));
    }

    @Test
    void upOutside() {
        assertThatThrownBy(() -> Coordinate.of("c8").up(1)).isInstanceOf(CoordinateException.class);
    }

    @Test
    void down() {
        assertThat(Coordinate.of("e4").down(2)).isEqualTo(Coordinate.of("e2"));
    }

    @Test
    void downOutside() {
        assertThatThrownBy(() -> Coordinate.of("g1").down(1)).isInstanceOf(CoordinateException.class);
    }

    @Test
    void left() {
        assertThat(Coordinate.of("e5").left(2)).isEqualTo(Coordinate.of("c5"));
    }

    @Test
    void leftOutside() {
        assertThatThrownBy(() -> Coordinate.of("a6").left(1)).isInstanceOf(CoordinateException.class);
    }

    @Test
    void right() {
        assertThat(Coordinate.of("b5").up(2)).isEqualTo(Coordinate.of("b7"));
    }

    @Test
    void rightOutside() {
        assertThatThrownBy(() -> Coordinate.of("h3").right(1)).isInstanceOf(CoordinateException.class);
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Coordinate.class).verify();
    }
}
