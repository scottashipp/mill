package com.scottshipp.code.mill.stream;

import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottshipp.code.mill.stream.ComparablePredicates.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ComparablePredicatesTest {

    @Test
    public void testFilterComparableDataWithBetween() {
        String lettersBetweenDandN = Stream.of("R", "A", "I", "N", "E", "R", "T", "E", "A", "M")
                .filter(ComparablePredicates.isBetween("D", "N"))
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
        assertEquals("E, I, M", lettersBetweenDandN);
    }

    @Test
    public void testFilterComparableDataWithRangeOpen() {
        String lettersBetweenDandN = Stream.of("R", "A", "I", "N", "E", "R", "T", "E", "A", "M")
                .filter(ComparablePredicates.isInRangeOpen("D", "N"))
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
        assertEquals("E, I, M", lettersBetweenDandN);
    }

    @Test
    public void testFilterComparableDataWithRangeClosed() {
        String lettersBetweenDandN = Stream.of("R", "A", "I", "N", "E", "R", "T", "E", "A", "M")
                .filter(isInRangeClosed("D", "N"))
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
        assertEquals("E, I, M, N", lettersBetweenDandN);
    }

    @Test
    public void testFilterComparableDataLessThan() {
        String lettersBetweenDandN = Stream.of("R", "A", "I", "N", "E", "R", "T", "E", "A", "M")
                .filter(isLessThan("E"))
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
        assertEquals("A", lettersBetweenDandN);
    }

    @Test
    public void testFilterComparableDataLessThanOrEqualTo() {
        String lettersBetweenDandN = Stream.of("R", "A", "I", "N", "E", "R", "T", "E", "A", "M")
                .filter(isLessThanOrEqualTo("E"))
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
        assertEquals("A, E", lettersBetweenDandN);
    }

    @Test
    public void testFilterComparableDataGreaterThan() {
        String lettersBetweenDandN = Stream.of("R", "A", "I", "N", "E", "R", "T", "E", "A", "M")
                .filter(isGreaterThan("M"))
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
        assertEquals("N, R, T", lettersBetweenDandN);
    }

    @Test
    public void testFilterComparableDataGreaterThanOrEqualTo() {
        String lettersBetweenDandN = Stream.of("R", "A", "I", "N", "E", "R", "T", "E", "A", "M")
                .filter(isGreaterThanOrEqualTo("M"))
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
        assertEquals("M, N, R, T", lettersBetweenDandN);
    }

    @Test
    public void testShowJava8StandardGreaterThan() {
        String lettersGreaterThanM = Stream.of("R", "A", "I", "N", "E", "R", "T", "E", "A", "M")
                .filter(s -> s.compareTo("M") > 0)
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
        assertEquals("N, R, T", lettersGreaterThanM);
    }


}
