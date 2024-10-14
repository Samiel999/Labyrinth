package com.samuelschwenn.game_logic.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CoordsIntTest {

    @ParameterizedTest
    @MethodSource
    void toString_shouldReturnCorrectString_givenValidCoordsInt(CoordsInt given, String expected) {
        String actual = given.toString();
        assertThat(expected).isEqualTo(actual);
    }

    @ParameterizedTest
    @MethodSource
    void toCoordsDouble_shouldReturnCorrectCoordsDouble_givenValidCoordsInt(CoordsInt given, CoordsDouble expected) {
        CoordsDouble actual = given.toCoordsDouble();
        assertThat(expected).isEqualTo(actual);
    }

    @ParameterizedTest
    @MethodSource
    void isInRange_shouldReturnTrue_whenCoordsIntIsInGivenRange(CoordsInt given, CoordsInt target, int range) {
        boolean actual = given.isInRange(range, target);
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @MethodSource
    void isInRange_shouldReturnFalse_whenCoordsIntIsNotInGivenRange(CoordsInt given, CoordsInt target, int range) {
        boolean actual = given.isInRange(range, target);
        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @MethodSource
    void equals_shouldReturnTrue_whenCoordsIntAreEqual(CoordsInt given, CoordsInt target) {
        boolean actual = given.equals(target);
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @MethodSource
    void equals_shouldReturnFalse_whenCoordsIntAreNotEqual(CoordsInt given, CoordsInt target) {
        boolean actual = given.equals(target);
        assertThat(actual).isFalse();
    }

    private static Stream<Arguments> toString_shouldReturnCorrectString_givenValidCoordsInt() {
        return Stream.of(
                Arguments.of(new CoordsInt(0,0), "0_0"),
                Arguments.of(new CoordsInt(-100, -100), "-100_-100"),
                Arguments.of(new CoordsInt(10000, 10000), "10000_10000"),
                Arguments.of(new CoordsInt(-100000, 100000), "-100000_100000"),
                Arguments.of(new CoordsInt(1000000, -100000), "1000000_-100000")
        );
    }

    private static Stream<Arguments> toCoordsDouble_shouldReturnCorrectCoordsDouble_givenValidCoordsInt() {
        return Stream.of(
                Arguments.of(new CoordsInt(0,0), new CoordsDouble(0,0)),
                Arguments.of(new CoordsInt(-100, -100), new CoordsDouble(-100,-100)),
                Arguments.of(new CoordsInt(10000, 10000), new CoordsDouble(10000,10000)),
                Arguments.of(new CoordsInt(-100000, 100000), new CoordsDouble(-100000,100000)),
                Arguments.of(new CoordsInt(1000000, -100000), new CoordsDouble(1000000, -100000))
        );
    }

    private static Stream<Arguments> isInRange_shouldReturnTrue_whenCoordsIntIsInGivenRange() {
        return Stream.of(
                Arguments.of(
                        new CoordsInt(0,0),
                        new CoordsInt(0,0),
                        1
                ),
                Arguments.of(
                        new CoordsInt(0, 0),
                        new CoordsInt(16, 0),
                        16
                ),
                Arguments.of(
                        new CoordsInt(4,4),
                        new CoordsInt(0,0),
                        6
                ),
                Arguments.of(
                        new CoordsInt(-1, -1),
                        new CoordsInt(1,1),
                        3
                )
        );
    }

    private static Stream<Arguments> isInRange_shouldReturnFalse_whenCoordsIntIsNotInGivenRange() {
        return Stream.of(
                Arguments.of(
                        new CoordsInt(0,0),
                        new CoordsInt(0,0),
                        -1
                ),
                Arguments.of(
                        new CoordsInt(0, 0),
                        new CoordsInt(16, 0),
                        15
                ),
                Arguments.of(
                        new CoordsInt(4,4),
                        new CoordsInt(0,0),
                        3
                ),
                Arguments.of(
                        new CoordsInt(-1, -1),
                        new CoordsInt(1,1),
                        1
                )
        );
    }

    private static Stream<Arguments> equals_shouldReturnTrue_whenCoordsIntAreEqual() {
        return Stream.of(
                Arguments.of(new CoordsInt(0,0), new CoordsInt(0,0)),
                Arguments.of(new CoordsInt(-100, -100), new CoordsInt(-100, -100)),
                Arguments.of(new CoordsInt(10000, 10000), new CoordsInt(10000,10000)),
                Arguments.of(new CoordsInt(-100000, 100000), new CoordsInt(-100000,100000)),
                Arguments.of(new CoordsInt(1000000, -100000), new CoordsInt(1000000, -100000))
        );
    }

    private static Stream<Arguments> equals_shouldReturnFalse_whenCoordsIntAreNotEqual() {
        return Stream.of(
                Arguments.of(new CoordsInt(0,0), new CoordsInt(0,1)),
                Arguments.of(new CoordsInt(-100, -100), new CoordsInt(100, 100)),
                Arguments.of(new CoordsInt(10000, 10000), new CoordsInt(10001,10000)),
                Arguments.of(new CoordsInt(-100000, 100000), new CoordsInt(100000,-100000)),
                Arguments.of(new CoordsInt(1000000, -100000), new CoordsInt(-1000000, -100000))
        );
    }
}
