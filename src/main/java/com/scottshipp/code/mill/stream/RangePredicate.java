package com.scottshipp.code.mill.stream;

import java.util.function.Predicate;

/**
 * Interface used to construct predicates that return true
 * for a given range or based on some comparison value.
 *
 * @param <S> The type of predicate returned by each method
 * @param <T> The type of object(s) used as comparison value(s)
 */
public interface RangePredicate<S, T> {

    /**
     * Returns a predicate that returns true if a given value is &gt; value.
     * @param value the value to compare against
     * @return a predicate that returns true if a given value is &gt; value
     */
    Predicate<S> isGreaterThan(T value);

    /**
     * Returns a predicate that returns true if a given value is &lt; value.
     * @param value the value to compare against
     * @return a predicate that returns true if a given value is &lt; value
     */
    Predicate<S> isLessThan(T value);

    /**
     * Returns a predicate that returns true if a given value is &gt;= value.
     * @param value the value to compare against
     * @return a predicate that returns true if a given value is &gt;= value
     */
    Predicate<S> isGreaterThanOrEqualTo(T value);

    /**
     * Returns a predicate that returns true if a given value is &lt;= value.
     * @param value the value to compare against
     * @return a predicate that returns true if a given value is &lt;= value
     */
    Predicate<S> isLessThanOrEqualTo(T value);

    /**
     *
     * Same as {@link #isInRangeOpen(Object, Object)}
     *
     * @param low the lower bound
     * @param high the higher bound
     * @return a predicate that returns true if lower &lt; data &lt; higher
     */
    Predicate<S> isBetween(T low, T high);

    /**
     * Returns a predicate that returns true if lower &lt; data &lt; higher.
     * @param low the lower bound
     * @param high the higher bound
     * @return a predicate that returns true if lower &lt; data &lt; higher
     */
    Predicate<S> isInRangeOpen(T low, T high);

    /**
     * Returns a predicate that returns true if lower &lt;= data &lt;= higher.
     * @param low the lower bound
     * @param high the higher bound
     * @return a predicate that returns true if lower &lt;= data &lt;= higher
     */
    Predicate<S> isInRangeClosed(T low, T high);

}
