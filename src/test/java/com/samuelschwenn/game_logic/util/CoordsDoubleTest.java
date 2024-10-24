package com.samuelschwenn.game_logic.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CoordsDoubleTest {

    @ParameterizedTest
    @MethodSource
    void toString_shouldReturnCorrectString_givenValidCoordsDouble(CoordsDouble given, String expected) {
        String actual = given.toString();
        assertThat(expected).isEqualTo(actual);
    }

    @ParameterizedTest
    @MethodSource
    void toCoordsDouble_shouldReturnCorrectCoordsInt_givenValidCoordsDouble(CoordsDouble given, CoordsInt expected) {
        CoordsInt actual = given.toCoordsInt();
        assertThat(expected).isEqualTo(actual);
    }

    @ParameterizedTest
    @MethodSource
    void getNormalized_shouldReturnNormalizedCoordsDouble_givenValidDirection(Direction given, CoordsDouble expected) {
        CoordsDouble actual = CoordsDouble.getNormalized(given);
        assertThat(expected).isEqualTo(actual);
    }

    @ParameterizedTest
    @MethodSource
    void getDirectionTo_shouldReturnCorrectDirection_givenCoordsNotEqual(CoordsDouble given, CoordsDouble target, Direction expected) {
        Direction actual = given.getDirectionTo(target);
        assertThat(expected).isEqualTo(actual);
    }

    @ParameterizedTest
    @MethodSource
    void getDirectionTo_shouldReturnNull_givenCoordsEqual(CoordsDouble given, CoordsDouble target) {
        Direction actual = given.getDirectionTo(target);
        assertThat(actual).isNull();
    }

    @ParameterizedTest
    @MethodSource
    void multipliedBy_shouldReturnScaledCoordsDouble_givenValidFactor(CoordsDouble given, double factor, CoordsDouble expected) {
        CoordsDouble actual = given.multipliedBy(factor);
        assertThat(expected).isEqualTo(actual);
    }

    @ParameterizedTest
    @MethodSource
    void add_shouldReturnSumOfBoth_givenValidOtherCoordsDouble(CoordsDouble given, CoordsDouble other, CoordsDouble expected) {
        CoordsDouble actual = given.add(other);
        assertThat(expected).isEqualTo(actual);
    }

    @ParameterizedTest
    @MethodSource
    void max_shouldReturnMaxCoordsOfBoth_givenValidOtherCoordsDouble(CoordsDouble given, CoordsDouble other, CoordsDouble expected) {
        CoordsDouble actual = given.maxCoords(other);
        assertThat(expected).isEqualTo(actual);
    }

    @ParameterizedTest
    @MethodSource
    void min_shouldReturnMinCoordsOfBoth_givenValidOtherCoordsDouble(CoordsDouble given, CoordsDouble other, CoordsDouble expected) {
        CoordsDouble actual = given.minCoords(other);
        assertThat(expected).isEqualTo(actual);
    }


    private static Stream<Arguments> toString_shouldReturnCorrectString_givenValidCoordsDouble() {
        return Stream.of(
                Arguments.of(new CoordsDouble(0, 0), "0.0_0.0"),
                Arguments.of(new CoordsDouble(-100, -100), "-100.0_-100.0"),
                Arguments.of(new CoordsDouble(10000, 10000), "10000.0_10000.0"),
                Arguments.of(new CoordsDouble(-100000, 100000), "-100000.0_100000.0"),
                Arguments.of(new CoordsDouble(1000000, -100000), "1000000.0_-100000.0")
        );
    }

    private static Stream<Arguments> toCoordsDouble_shouldReturnCorrectCoordsInt_givenValidCoordsDouble() {
        return Stream.of(
                Arguments.of(new CoordsDouble(0.5, 0), new CoordsInt(0, 0)),
                Arguments.of(new CoordsDouble(-100.2345, -100), new CoordsInt(-100, -100)),
                Arguments.of(new CoordsDouble(10000, 10000.99999), new CoordsInt(10000, 10000)),
                Arguments.of(new CoordsDouble(-100000, 100000.9), new CoordsInt(-100000, 100000)),
                Arguments.of(new CoordsDouble(1000000.999, -100000), new CoordsInt(1000000, -100000))
        );
    }

    private static Stream<Arguments> getNormalized_shouldReturnNormalizedCoordsDouble_givenValidDirection() {
        return Stream.of(
                Arguments.of(Direction.EAST, new CoordsDouble(1, 0)),
                Arguments.of(Direction.NORTH, new CoordsDouble(0, 1)),
                Arguments.of(Direction.SOUTH, new CoordsDouble(0, -1)),
                Arguments.of(Direction.WEST, new CoordsDouble(-1, 0))
        );
    }

    private static Stream<Arguments> getDirectionTo_shouldReturnCorrectDirection_givenCoordsNotEqual() {
        return Stream.of(
                Arguments.of(new CoordsDouble(10, 4), new CoordsDouble(10, 5), Direction.NORTH),
                Arguments.of(new CoordsDouble(0,0), new CoordsDouble(1, 1), Direction.EAST),
                Arguments.of(new CoordsDouble(10, 0), new CoordsDouble(0, 10), Direction.WEST),
                Arguments.of(new CoordsDouble(0, 0), new CoordsDouble(0, -1), Direction.SOUTH)
        );
    }

    private static Stream<Arguments> getDirectionTo_shouldReturnNull_givenCoordsEqual() {
        return Stream.of(
                Arguments.of(new CoordsDouble(0,0), new CoordsDouble(0,0)),
                Arguments.of(new CoordsDouble(10, 10), new CoordsDouble(10, 10)),
                Arguments.of(new CoordsDouble(-10, -10), new CoordsDouble(-10, -10))
        );
    }

    private static Stream<Arguments> multipliedBy_shouldReturnScaledCoordsDouble_givenValidFactor() {
        return Stream.of(
                Arguments.of(new CoordsDouble(0, 0), 20.0, new CoordsDouble(0, 0)),
                Arguments.of(new CoordsDouble(1, 1), 15.0, new CoordsDouble(15, 15)),
                Arguments.of(new CoordsDouble(-1, -1), 10.0, new CoordsDouble(-10, -10)),
                Arguments.of(new CoordsDouble(-10, 10), 0.1, new CoordsDouble(-1, 1))
        );
    }

    private static Stream<Arguments> add_shouldReturnSumOfBoth_givenValidOtherCoordsDouble() {
        return Stream.of(
                Arguments.of(new CoordsDouble(0, 0), new CoordsDouble(15, 1), new CoordsDouble(15, 1)),
                Arguments.of(new CoordsDouble(15, -5), new CoordsDouble(23, 6), new CoordsDouble(38, 1)),
                Arguments.of(new CoordsDouble(0, 0), new CoordsDouble(0, 0), new CoordsDouble(0, 0))
        );
    }

    public static Stream<Arguments> max_shouldReturnMaxCoordsOfBoth_givenValidOtherCoordsDouble() {
        return Stream.of(
                Arguments.of(new CoordsDouble(0,0), new CoordsDouble(1, 1), new CoordsDouble(1,1)),
                Arguments.of(new CoordsDouble(1, 1), new CoordsDouble(-1, -1), new CoordsDouble(1, 1)),
                Arguments.of(new CoordsDouble(-1, 1), new CoordsDouble(1, -1), new CoordsDouble(1, 1)),
                Arguments.of(new CoordsDouble(0,0), new CoordsDouble(0,0), new CoordsDouble(0,0))
        );
    }

    public static Stream<Arguments> min_shouldReturnMinCoordsOfBoth_givenValidOtherCoordsDouble() {
        return Stream.of(
                Arguments.of(new CoordsDouble(0,0), new CoordsDouble(1, 1), new CoordsDouble(0,0)),
                Arguments.of(new CoordsDouble(1, 1), new CoordsDouble(-1, -1), new CoordsDouble(-1, -1)),
                Arguments.of(new CoordsDouble(-1, 1), new CoordsDouble(1, -1), new CoordsDouble(-1, -1)),
                Arguments.of(new CoordsDouble(0,0), new CoordsDouble(0,0), new CoordsDouble(0,0))
        );
    }

}
