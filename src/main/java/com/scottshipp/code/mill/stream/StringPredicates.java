package com.scottshipp.code.mill.stream;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Predicates to use with Streams of StringPredicates. Some of these seem pointless
 * as they are aliases to existing Java methods that you can pass as a method
 * reference. Their real power comes out when used with the fluent composition
 * methods of predicates (such as: or, and, negate). Filtering a stream of
 * StringPredicates to those that are nonNull and nonEmpty can look like this:
 *
 * <pre>
 *     {@code
 *     // with this library
 *     streamWithNullsAndEmpties.filter(nonNull.and(nonEmpty));
 *     }
 * </pre>
 *
 * Instead of:
 * <pre>
 *     {@code
 *     // standard java
 *     streamWithNullsAndEmpties.filter(s -> s != null && !s.isEmpty());
 *     }
 * </pre>
 * Or:
 * <pre>
 *     {@code
 *     // standard java
 *     streamWithNullsAndEmpties.filter(((Predicate<String>)Objects::nonNull)
 *                                          .and(((Predicate<String>)String::isEmpty).negate());
 *     }
 * </pre>
 */
public final class StringPredicates {

    private StringPredicates() {
        // static methods only
    }

    /**
     * Predicate alias for method reference String::isEmpty.
     *
     * @return a predicate that returns true if a given string has length 0
     * @see String#isEmpty()
     */
    public static final Predicate<String> isEmpty() {
        return String::isEmpty;
    }

    /**
     * Predicate alias for method reference Objects::isNull.
     * @return a predicate that returns true if a given object is null
     * @see Objects#isNull(Object)
     */
    public static final Predicate<String> isNull() {
        return Objects::isNull;
    }

    /**
     * Predicate alias for method reference Objects::nonNull.
     * @return a predicate that returns true if a given object is not null
     * @see Objects#nonNull(Object)
     */
    public static final Predicate<String> nonNull() {
        return Objects::nonNull;
    }

    /**
     * Predicate alias of (s) -&gt; !s.isEmpty().
     * @return returns true if a given string is not empty
     * @see String#isEmpty()
     */
    public static final Predicate<String> nonEmpty() {
        return isEmpty().negate();
    }

    /**
     * Predicate to check that a string is longer than a given length,
     * exclusive of the bound.
     *
     * @param length the bound for length
     * @return A predicate that returns true when a given string's length &gt; length
     * @see String#length()
     */
    public static Predicate<String> longerThan(int length) {
        return s ->  s != null && s.length() > length;
    }

    /**
     * Predicate to check that a string is shorter than a given length,
     * exclusive of the bound.
     *
     * @param length the bound for length
     * @return A predicate that returns true when a given string's length &lt; length
     * @see String#length()
     */
    public static Predicate<String> shorterThan(int length) {
        return s ->  s != null && s.length() < length;
    }

    /**
     * Predicate to check that a string is shorter than or the same as a given length.
     *
     * @param length the bound for length
     * @return A predicate that returns true when a given string's length &lt;= length
     * @see String#length()
     */
    public static Predicate<String> withMaximumLength(int length) {
        return shorterThan(length + 1);
    }

    /**
     * Predicate to check that a string is longer than or the same as a given length.
     *
     * @param length the bound for length
     * @return A predicate that returns true when a given string's length &gt;= length
     * @see String#length()
     */
    public static Predicate<String> withMinimumLength(int length) {
        return longerThan(length - 1);
    }

    /**
     * Predicate to check that a string equals a given value using Objects.equals.
     * Under this method contract only a null will equal another
     * null, otherwise the strings must match exactly according to the normal
     * contract of String.equals.
     *
     * @param match A string to match against
     * @return A predicate that returns true when a given string equals the match
     * @see Objects#equals(Object, Object)
     */
    public static Predicate<String> equaling(String match) {
        return s -> Objects.equals(s, match);
    }

    /**
     * A predicate that returns true if and only if a given string contains the specified sequence of char values.
     * @param sub the subsequence to search for
     * @return A predicate that returns true if and only if a given string contains the specified sequence of char values
     */
    public static Predicate<String> containing(String sub) {
        return s -> (s != null) ? s.contains(sub) : sub == null;
    }

    /**
     * A predicate that tells whether or not a given string matches the given regular expression.
     * @param regex the regular expression to which this string is to be matched
     * @return a predicate that tells whether or not a given string matches the given regular expression
     * @see String#matches(String)
     */
    public static Predicate<String> matches(String regex) {
        return s -> (s != null) ? s.matches(regex) : regex == null;
    }


    /**
     * Predicate to check that a string equals a given value using String.equalsIgnoreCase.
     * Under this method contract only a null will equal another
     * null, otherwise the strings must match exactly according to the normal
     * contract of String.equals.
     *
     * @param match A string to match against
     * @return A predicate that returns true when a given string equals the match
     * @see String#equalsIgnoreCase(String)
     */
    public static Predicate<String> equalsIgnoreCase(String match) {
        return s -> (s != null) ? s.equalsIgnoreCase(match) : match == null;
    }

    /**
     *
     * A predicate that returns true if and only if a given string contains the
     * specified sequence of char values, ignoring case.
     *
     * @param sub the subsequence to search for
     * @return A predicate that returns true if and only if a given string contains the
     * specified sequence of char values, ignoring case.
     * @see String#equalsIgnoreCase(String)
     */
    public static Predicate<String> containingIgnoreCase(String sub) {
        return s -> (s != null) ? s.toLowerCase().contains(sub.toLowerCase()) : sub == null;
    }

}
