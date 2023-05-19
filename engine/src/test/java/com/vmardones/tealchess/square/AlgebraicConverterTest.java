/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.square;

import static com.vmardones.tealchess.square.Square.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

final class AlgebraicConverterTest {

    @Test
    void fileToIndex() {
        assertThat(AlgebraicConverter.fileToIndex("a")).isEqualTo(0);
        assertThat(AlgebraicConverter.fileToIndex("c")).isEqualTo(2);
        assertThat(AlgebraicConverter.fileToIndex("f")).isEqualTo(5);
        assertThat(AlgebraicConverter.fileToIndex("h")).isEqualTo(7);
    }

    @Test
    void fileFromIndex() {
        assertThat(AlgebraicConverter.fileFromIndex(0)).isEqualTo("a");
        assertThat(AlgebraicConverter.fileFromIndex(2)).isEqualTo("c");
        assertThat(AlgebraicConverter.fileFromIndex(5)).isEqualTo("f");
        assertThat(AlgebraicConverter.fileFromIndex(7)).isEqualTo("h");
    }

    @Test
    void rankToIndex() {
        assertThat(AlgebraicConverter.rankToIndex(1)).isEqualTo(0);
        assertThat(AlgebraicConverter.rankToIndex(8)).isEqualTo(7);
    }

    @Test
    void rankFromIndex() {
        assertThat(AlgebraicConverter.rankFromIndex(0)).isEqualTo(1);
        assertThat(AlgebraicConverter.rankFromIndex(7)).isEqualTo(8);
    }

    @Test
    void validAlgebraicNotation() {
        assertThat(AlgebraicConverter.toSquare("a8")).isEqualTo(a8);
        assertThat(AlgebraicConverter.toSquare("a1")).isEqualTo(a1);
        assertThat(AlgebraicConverter.toSquare("h8")).isEqualTo(h8);
        assertThat(AlgebraicConverter.toSquare("h1")).isEqualTo(h1);
    }

    @Test
    void invalidAlgebraicNotation() {
        assertThatThrownBy(() -> AlgebraicConverter.toSquare("x4"))
                .isInstanceOf(AlgebraicNotationException.class)
                .hasMessageContaining("x4");
    }

    @Test
    void toAlgebraic() {
        assertThat(AlgebraicConverter.toAlgebraic(a8)).isEqualTo("a8");
        assertThat(AlgebraicConverter.toAlgebraic(a1)).isEqualTo("a1");
        assertThat(AlgebraicConverter.toAlgebraic(h8)).isEqualTo("h8");
        assertThat(AlgebraicConverter.toAlgebraic(h1)).isEqualTo("h1");
    }
}
