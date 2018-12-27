package com.scottshipp.code.mill.stream;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>ComparatorPredicates is intended as a predicate builder to allow the comparison of
 * objects of type T in a Stream&lt;T&gt; against a reference object also of type T,
 * via a comparator built from a method on the type, that returns a Comparable.</p>
 *
 * <p><strong>If you have a Stream&lt;T extends Comparable&lt;T&gt; just use
 * {@link ComparablePredicates} instead.</strong></p>
 *
 * <p>It is short-hand for the common use case of filtering a stream of a given type
 * based on a value or range of values matching a given object of the same type.</p>
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
 * <p>To filter the stream using the standard Java 8 library:</p>
 *
 * <pre>
 *     {@code
 *          ... // Java 8 standard library
 *
 *          // Find all holidays taking place after Independence Day
 *          holidays
 *              .filter(
 *                  holiday -> {
 *                          Comparator<Holiday> comparingDates = Comparator.comparing(Holiday::getDate());
 *                          return comparingDates.compare(holiday, independenceDay) > 0;
 *                  }
 *               ... // etc
 *     }
 * </pre>
 *
 * <p>And here is how the same task would look using ComparatorPredicates:</p>
 *
 * <pre>
 *     {@code
 *          ... // Using ComparatorPredicates
 *
 *          holidays
 *              .filter(
 *                  where(Holiday::getDate()).isGreaterThan(independenceDay)
 *              )
 *
 *          ... // etc.
 *     }
 * </pre>
 *
 * <p>If you do already have a standard library comparator (such as {@link String#CASE_INSENSITIVE_ORDER})
 * or your own implementation of comparator, you can also use that:</p>
 *
 * <pre>
 *     {@code
 *          Stream<String> engineeringTeam = Stream.of("Parker", "Winter", "Mukesh", "Jean", "Mackenzie", "Shannon", "Tatum", "Jane");
 *          String engineeringTeamAtoP = engineeringTeam
 *                                      .filter(
 *                                          where(String.CASE_INSENSITIVE_ORDER).isLessThanOrEqualTo("Parker")
 *                                      )
 *                                      .collect(joining(", "));
            // results in "Parker, Mukesh, Jean, Mackenzie, Jane"
 *     }
 * </pre>
 *
 * <p>A few more examples:</p>
 *
 * <pre>
 *     {@code
 *          // US Holidays happening before July 4
 *          holidays.filter(ComparatorPredicates.where(Holiday::getDate).isLessThan(independenceDay));
 *
 *          // US holidays before New Years (empty result)
 *          holidays.filter(ComparatorPredicates.where(Holiday::getDate).isLessThan(newYears));
 *
 *          // US holidays happening between Labor Day and Christmas, inclusive
 *          holidays.filter(ComparatorPredicates.where(Holiday::getDate).isInRangeClosed(laborDay, christmas);
 *     }
 * </pre>
 *
 * @param <T> the type of value we are comparing
 */
public final class ComparatorPredicates<T> implements RangePredicate<T, T> {

    private final Comparator<T> comparator;

    private ComparatorPredicates(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * Returns a predicate that returns true if a given value is &gt; value.
     * @param value the value to compare against
     * @return a predicate that returns true if a given value is &gt; value
     */
    @Override
    public Predicate<T> isGreaterThan(T value) {
        return t -> comparator.compare(t, value) > 0;
    }

    /**
     * Returns a predicate that returns true if a given value is &lt; value.
     * @param value the value to compare against
     * @return a predicate that returns true if a given value is &lt; value
     */
    @Override
    public Predicate<T> isLessThan(T value) {
        return t -> comparator.compare(t, value) < 0;
    }

    /**
     * Returns a predicate that returns true if a given value is &gt;= value.
     * @param value the value to compare against
     * @return a predicate that returns true if a given value is &gt;= value
     */
    @Override
    public Predicate<T> isGreaterThanOrEqualTo(T value) {
        return t -> comparator.compare(t, value) >= 0;
    }

    /**
     * Returns a predicate that returns true if a given value is &lt;= value.
     * @param value the value to compare against
     * @return a predicate that returns true if a given value is &lt;= value
     */
    @Override
    public Predicate<T> isLessThanOrEqualTo(T value) {
        return t -> comparator.compare(t, value) <= 0;
    }

    /**
     *
     * Convenience method which redirects the call to the
     * {@link #isInRangeOpen(Object, Object)  isInRangeOpen} method.
     *
     * <p><strong>Note:</strong> The values passed in are not validated for low &lt; high</p>
     *
     * @param low See {@link #isInRangeOpen(Object, Object) isInRangeOpen}
     * @param high See {@link #isInRangeOpen(Object, Object)  isInRangeOpen}
     * @return See {@link #isInRangeOpen(Object, Object)  isInRangeOpen}
     */
    @Override
    public Predicate<T> isBetween(T low, T high) {
        return isInRangeOpen(low, high);
    }

    /**
     *
     * Returns a predicate that returns true if lower &lt; data &lt; higher.
     *
     * <p><strong>Note:</strong> The values passed in are not validated for low &lt; high</p>
     *
     * @param low the lower bound
     * @param high the higher bound
     * @return a predicate that returns true if lower &lt; data &lt; higher
     */
    @Override
    public Predicate<T> isInRangeOpen(T low, T high) {
        return isGreaterThan(low).and(isLessThan(high));
    }

    /**
     * Returns a predicate that returns true if lower &lt;= data &lt;= higher.
     *
     * <p><strong>Note:</strong> The values passed in are not validated for low &lt; high</p>
     *
     * @param low the lower bound
     * @param high the higher bound
     * @return a predicate that returns true if lower &lt;= data &lt;= higher
     */
    @Override
    public Predicate<T> isInRangeClosed(T low, T high) {
        return isGreaterThanOrEqualTo(low).and(isLessThanOrEqualTo(high));
    }

    /**
     * Used to construct a predicate which internally uses the supplied
     * comparator.
     *
     * @param comparator the comparator to use when making a given comparison
     * @param <T> the type of object being compared
     * @return a {@link ComparatorPredicates ComparatorPredicates} for the data method reference in question
     */
    public static <T> ComparatorPredicates<T> where(Comparator<T> comparator) {
        return new ComparatorPredicates<>(comparator);
    }

    /**
     * Used to construct a predicate which internally uses a comparator based on
     * a method reference. The comparator is created through a call to the
     * standard library method {@link Comparator#comparing(Function)}.
     *
     * @param methodReference a method reference to a method on the object which is then used to construct a comparator
     * @param <T> the type of object being compared
     * @param <U> the type the method reference returns
     * @return a {@link ComparatorPredicates ComparatorPredicates} for the data method reference in question
     */
    public static <T, U extends Comparable<? super U>> ComparatorPredicates<T> where(Function<T, U> methodReference) {
        Comparator<T> comparator = Comparator.comparing(methodReference);
        return new ComparatorPredicates<>(comparator);
    }

}
