package com.scottshipp.code.mill.stream;

import java.util.function.Predicate;

/**
 * General predicates intended to be used for filtering streams of classes
 * implementing Comparable&lt;T&gt;.
 *
 * Using the standard Java 8 library, the code looks like:
 *
 * <pre>
 *     {@code
 *          String lettersGreaterThanM = Stream.of("R", "A", "I", "N", "E", "R")
 *                                              .filter(s -> s.compareTo("M") > 0);
 *     }
 * </pre>
 *
 * Some examples using this library:
 * <pre>
 *     {@code
 *     String lettersBetweenDandN = Stream.of("R", "A", "I", "N", "E", "R")
 *                                          .filter(ComparablePredicates.isBetween("D", "N"))
 *                                          .distinct()
 *                                          .sorted()
 *                                          .collect(Collectors.joining(", "));
 *     // result = "E, I"
 *     }
 * </pre>
 *
 * <pre>
 *     {@code
 *     String lettersBetweenDandN = Stream.of("R", "A", "I", "N", "E", "R")
 *                                          .filter(ComparablePredicates.isInRangeClosed("D", "N"))
 *                                          .distinct()
 *                                          .sorted()
 *                                          .collect(Collectors.joining(", "));
 *     // result = "E, I, N"
 *     }
 * </pre>
 *
 *
 * @see java.util.stream.Stream
 */
public final class ComparablePredicates {

    private ComparablePredicates() {
        // provides static methods only
    }

    /**
     * Handy alias for {@link #isInRangeOpen(Comparable, Comparable)}.
     * @param lower See {@link #isInRangeOpen(Comparable, Comparable)}
     * @param higher See {@link #isInRangeOpen(Comparable, Comparable)}
     * @param <T> See {@link #isInRangeOpen(Comparable, Comparable)}
     * @return See {@link #isInRangeOpen(Comparable, Comparable)}
     */
    public static <T extends Comparable<T>> Predicate<T> isBetween(T lower, T higher) {
        return isInRangeOpen(lower, higher);
    }

    /**
     * Predicate intended to be passed to Stream.filter, that filters out
     * elements which are outside the open range (lower, higher). (lower &lt; element &lt; higher)
     *
     * <pre>
     *     {@code
     *     String lettersBetweenDandN = Stream.of("R", "A", "I", "N", "E", "R")
     *                                          .filter(ComparablePredicates.isInRangeOpen("D", "N"))
     *                                          .distinct()
     *                                          .sorted()
     *                                          .collect(Collectors.joining(", "));
     *     // result = "E, I"
     *     }
     * </pre>
     *
     * @param lower the lower bound for the range
     * @param higher the higher bound
     * @param <T> the type of element in the stream, must implement Comparable
     * @return true if lower &lt; element &lt; higher.
     */
    public static <T extends Comparable<T>> Predicate<T> isInRangeOpen(T lower, T higher) {
        return o -> o != null && o.compareTo(lower) > 0 && o.compareTo(higher) < 0;
    }

    /**
     * Predicate intended to be passed to Stream.filter, that filters out
     * elements which are outside the closed range [lower, higher]. (lower &lt;= element &lt;= higher)
     *
     *
     * @param lower the lower bound for the range
     * @param higher the higher bound
     * @param <T> the type of element in the stream, must implement Comparable
     * @return true if lower &lt;= element &lt;= higher.
     */
    public static <T extends Comparable<T>> Predicate<T> isInRangeClosed(T lower, T higher) {
        if(lower.compareTo(higher) > 0) {
            throw new IllegalArgumentException("Please pass a valid range to the inRange predicate. Your range (" + lower + ", " + higher + ") is invalid.");
        }
        return s -> s != null && s.compareTo(lower) >= 0 && s.compareTo(higher) <= 0;
    }

    public static <T extends Comparable<T>> Predicate<T> isLessThan(T value) {
        return s -> s != null && s.compareTo(value) < 0;
    }

    public static <T extends Comparable<T>> Predicate<T> isGreaterThan(T value) {
        return s -> s != null && s.compareTo(value) > 0;
    }

    public static <T extends Comparable<T>> Predicate<T> isGreaterThanOrEqualTo(T value) {
        return s -> s != null && s.compareTo(value) >= 0;
    }

    public static <T extends Comparable<T>> Predicate<T> isLessThanOrEqualTo(T value) {
        return s -> s != null && s.compareTo(value) <= 0;
    }
}
