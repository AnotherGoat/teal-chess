/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AlgebraicConverterTest {

    @Test
    void validAlgebraicNotation() {
        assertThat(AlgebraicConverter.toIndex("a8")).isZero();
        assertThat(AlgebraicConverter.toIndex("a1")).isEqualTo(56);
        assertThat(AlgebraicConverter.toIndex("h8")).isEqualTo(7);
        assertThat(AlgebraicConverter.toIndex("h1")).isEqualTo(63);
    }

    @Test
    void invalidAlgebraicNotation() {
        assertThatThrownBy(() -> AlgebraicConverter.toIndex("x4"))
                .isInstanceOf(BadAlgebraicNotationException.class)
                .hasMessageContaining("x4");
    }

    @Test
    void toAlgebraic() {
        assertThat(AlgebraicConverter.toAlgebraic(0)).isEqualTo("a8");
        assertThat(AlgebraicConverter.toAlgebraic(56)).isEqualTo("a1");
        assertThat(AlgebraicConverter.toAlgebraic(7)).isEqualTo("h8");
        assertThat(AlgebraicConverter.toAlgebraic(63)).isEqualTo("h1");
    }
}
