package com.scottshipp.code.mill.stream;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * ValueComparisonPredicates is intended as a predicate builder to allow the
 * comparison of some member data, of type T, of an object of type S in a
 * Stream&lt;S&gt; against another object of type T.
 *
 * It is short-hand for the common use case of filtering a stream of a given
 * type based on a provided value or range of values matching a given object of
 * the return type of one of its member methods.
 *
 * <p>Here is how that looks using the standard library.</p>
 *
 * <p><em>All of the below examples will use the following Stream&lt;Holiday&gt; storing a stream
 * of US holidays:</em></p>
 *
 * <pre>
 *     {@code
 *     // US holidays
 *     Stream<Holiday> holidays = Stream.of(newYears, memorialDay, independenceDay, laborDay, thanksgiving, dayAfterThanksgiving, dayBeforeChristmas, christmas);
 *     }
 * </pre>
 *
 * <p>To filter the stream using the standard Java 8 library against a LocalDate:</p>
 *
 * <pre>
 *     {@code
 *          ... // Java 8 standard library
 *
 *          // Find all holidays taking place after Independence Day
 *          LocalDate independenceDay = LocalDate.of(2018, 7, 4);
 *          holidays
 *              .filter(
 *                  holiday -> {
 *                          return holiday.getLocalDate().compareTo(independenceDay) > 0;
 *                  }
 *               ... // etc
 *     }
 * </pre>
 *
 * <p>And here is how the same task would look using ValueComparisonPredicates:</p>
 *
 * <pre>
 *     {@code
 *          ... // Using ValueComparisonPredicates
 *          LocalDate independenceDay = LocalDate.of(2018, 7, 4);
 *          holidays
 *              .filter(
 *                  where(Holiday::getDate()).isGreaterThan(independenceDay)
 *              )
 *
 *          ... // etc.
 *     }
 * </pre>
 *
 * <p>A few more examples:</p>
 *
 * <pre>
 *     {@code
 *          import static ValueComparisonPredicates.where;
 *
 *          // US Holidays happening before July 4
 *          holidays.filter(where(Holiday::getDate).isLessThan(LocalDate.of(2018, 7, 4));
 *
 *          // US holidays before New Years (empty result)
 *          holidays.filter(where(Holiday::getDate).isLessThan(LocalDate.of(2018, 1, 1));
 *
 *          // US holidays happening between two dates, inclusive
 *          holidays.filter(ComparatorPredicates.where(Holiday::getDate).isInRangeClosed(LocalDate.of(2018, 5, 1), LocalDate.of(2018, 8, 1));
 *     }
 * </pre>
 *
 * @param <S> Type of the element in a Stream&lt;S&gt;
 * @param <T> The return type of a given method call on S
 */
public final class ValueComparisonPredicates<S, T extends Comparable<T>> implements RangePredicate<S, T> {
    private final Function<S, T> methodRef;

    private ValueComparisonPredicates(Function<S, T> methodRef) {
        this.methodRef = methodRef;
    }

    /**
     * Used to construct a predicate which can test against a given
     * value or range of values.
     *
     * @param methodRef the method reference
     * @param <S> the type holding the method
     * @param <T> the return type of the method
     * @return a {@link ValueComparisonPredicates ValueComparisonPredicates} for the data method reference in question
     */
    public static <S, T extends Comparable<T>> ValueComparisonPredicates<S, T> where(Function<S, T> methodRef) {
        return new ValueComparisonPredicates<>(methodRef);
    }

    /**
     *
     * Convenience method which redirects the call to the
     * {@link #isInRangeOpen(Comparable, Comparable) isInRangeOpen} method.
     *
     * @param low See {@link #isInRangeOpen(Comparable, Comparable) isInRangeOpen}
     * @param high See {@link #isInRangeOpen(Comparable, Comparable) isInRangeOpen}
     * @return See {@link #isInRangeOpen(Comparable, Comparable) isInRangeOpen}
     * @see ComparablePredicates#isInRangeOpen(Comparable, Comparable)
     */
    @Override
    public Predicate<S> isBetween(T low, T high) {
        return isInRangeOpen(low, high);
    }

    /**
     *
     * Predicate intended to be passed to Stream.filter, that filters out
     * any elements whose methodReference returns data outside the open range
     * (lower, higher).
     *
     * This method is provided for Streams of type S where the filtering is
     * being done by some member method of S.
     *
     * Take the example of a Stream of Holiday objects where we want
     * to filter by the date of the Holiday. Thus, we pass a method reference
     * to Holiday::getDate:
     *
     * <pre>
     *     {@code
     *     List<Holiday> holidaysSecondHalf2048 = Stream.of(
     *                  newYears, memorialDay, independenceDay, laborDay,
     *                  thanksgiving, dayAfterThanksgiving, dayBeforeChristmas, christmas
     *              )
     *              .filter(where(Holiday::getDate).isInRangeOpen(LocalDate.of(2048, 5, 31), LocalDate.of(2049, 1, 1)))
     *              .collect(Collectors.toList());
     *     }
     * </pre>
     *
     * This method ignores null data. It will throw a runtime exception if
     * the inputs passed to it are such that low &gt;= high
     *
     * @param low the lower bound
     * @param high the higher bound
     * @return a predicate that returns true if lower &lt; data &lt; higher
     */
    @Override
    public Predicate<S> isInRangeOpen(T low, T high) {
        validateRange(low, high);
        return (S s) -> {
            T data = methodRef.apply(s);
            return data != null && data.compareTo(low) > 0 && data.compareTo(high) < 0;
        };
    }

    /**
     *
     * Predicate intended to be passed to Stream.filter, that filters out
     * any elements whose methodReference returns data outside the closed range
     * [lower, higher].
     *
     * This method is provided for Streams of type S where the filtering is
     * being done by some member method of S.
     *
     * Take the example of a Stream of Holiday objects where we want
     * to filter by the date of the Holiday. Thus, we pass a method reference
     * to Holiday::getDate:
     *
     * <pre>
     *     {@code
     *     List<Holiday> holidaysSecondHalf2048 = Stream.of(
     *                  newYears, memorialDay, independenceDay, laborDay,
     *                  thanksgiving, dayAfterThanksgiving, dayBeforeChristmas, christmas
     *              )
     *              .filter(where(Holiday::getDate).isInRangeClosed(LocalDate.of(2048, 6, 1), LocalDate.of(2048, 12, 31)))
     *              .collect(Collectors.toList());
     *     }
     * </pre>
     *
     * This method ignores null data. It will throw a runtime exception if
     * the inputs passed to it are such that low &gt;= high
     *
     * @param low the lower bound
     * @param high the higher bound
     * @return a predicate that returns true if lower &lt;= data &lt;= higher
     */
    @Override
    public Predicate<S> isInRangeClosed(T low, T high) {
        validateRange(low, high);
        return (S s) -> {
            T data = methodRef.apply(s);
            return data != null && data.compareTo(low) >= 0 && data.compareTo(high) <= 0;
        };
    }

    /**
     *
     * @param low the lower bound
     * @return true if the data is &gt; low
     */
    public Predicate<S> isGreaterThan(T low) {
        return (S s) -> {
            T data = methodRef.apply(s);
            return data != null && data.compareTo(low) > 0;
        };
    }

    /**
     *
     * @param high the upper bound
     * @return true if the data is &lt; low
     */
    public Predicate<S> isLessThan(T high) {
        return (S s) -> {
            T data = methodRef.apply(s);
            return data != null && data.compareTo(high) < 0;
        };
    }

    /**
     *
     * @param low the lower bound
     * @return true if the data is &gt;= low
     */
    public Predicate<S> isGreaterThanOrEqualTo(T low) {
        return (S s) -> {
            T data = methodRef.apply(s);
            return data != null && data.compareTo(low) >= 0;
        };
    }

    /**
     *
     * @param high the upper bound
     * @return true if the data is &lt;= low
     */
    public Predicate<S> isLessThanOrEqualTo(T high) {
        return (S s) -> {
            T data = methodRef.apply(s);
            return data != null && data.compareTo(high) <= 0;
        };
    }

    /**
     *
     * Applies Objects.equals to the value returned by the method reference.
     *
     * @param value the value to check for equality
     * @return true if the data equals value
     * @see Objects#equals(Object, Object)
     */
    public Predicate<S> equaling(T value) {
        return (S s) -> {
            T data = methodRef.apply(s);
            return Objects.equals(data, value);
        };
    }

    private <T extends Comparable<T>> void validateRange(T lower, T higher) {
        if(lower.compareTo(higher) > 0) {
            throw new IllegalArgumentException("Please pass a valid range to the inRange predicate. Your range (" + lower + ", " + higher + ") is invalid.");
        }
    }

}
