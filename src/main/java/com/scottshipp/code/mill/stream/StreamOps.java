package com.scottshipp.code.mill.stream;


import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides common operations that apply to multiple Streams, Collections, or
 * Arrays, and result in a new Stream.
 *
 */
public final class StreamOps {

    /**
     * Concatenates any number of streams together into one new stream.
     * @param streams An array of streams of homogenous type
     * @param <T> All elements in all streams are instances of this single type
     * @return A new stream formed from the concatenation of all streams in the array
     */
    @SafeVarargs
    public static <T> java.util.stream.Stream<T> concat(java.util.stream.Stream<T>... streams) {
        return java.util.stream.Stream.of(streams).flatMap(stream -> stream);
    }

    /**
     * Concatenates any number of collections together into a new stream.
     * @param collections An array of collections of homogenous type
     * @param <T> All elements in all collections are instances of this single type
     * @return A new stream formed from the concatenation of all collections in the array
     */
    @SafeVarargs
    public static <T> java.util.stream.Stream<T> concat(Collection<T>... collections) {
        return Arrays.stream(collections).map(c -> c.stream()).flatMap(stream -> stream);
    }

    /**
     * Concatenates any number of arrays together into a new stream.
     * @param arrays An array of arrays of homogenous type
     * @param <T> All elements in all arrays are instances of this single type
     * @return A new stream formed from the concatenation of all arrays in the array
     */
    @SafeVarargs
    public static <T> java.util.stream.Stream<T> concat(T[]... arrays) {
        return Arrays.stream(arrays).flatMap(a -> Arrays.stream(a));
    }

    /**
     * Given any number of streams, returns distinct values such that the set of
     * elements in the returned stream each exist in all of the passed streams.
     *
     * @param streams An array of streams
     * @param <T> All elements in all streams are instances of this single type
     * @return distinct values of all passed streams such that the set of elements
     * in the returned stream each exist in all of the passed streams
     */
    public static <T> Stream<T> intersection(Stream<T>... streams) {
        return Stream.of(streams).reduce(StreamOps::intersection).orElseGet(Stream::empty);
    }

    private static <T> Stream<T> intersection(Stream<T> stream1, Stream<T> stream2) {
        Set<T> set1 = stream1.distinct().collect(Collectors.toSet());
        return stream2.filter(set1::contains);
    }

    /**
     * Returns a new stream containing all distinct values from all passed streams.
     * @param streams An array of streams of homogenous type
     * @param <T> All elements in all streams are instances of this single type
     * @return a new stream containing all distinct values from all passed streams
     */
    public static <T> Stream<T> distinctValues(Stream<T>... streams) {
        return concat(streams).distinct();
    }

    /**
     * Given any number of streams, returns distinct values such that the set of
     * elements in the returned stream are those distinct elements of the first
     * stream that are not contained in any of the subsequent passed streams.
     *
     * @param streams An array of streams
     * @param <T> All elements in all streams are instances of this single type
     * @return distinct values of the first stream such that the set of elements
     * in the returned stream are not contained in any of the subsequent passed streams
     */
    public static <T> Stream<T> difference(Stream<T>... streams) {
        if(streams.length < 1) {
            return Stream.empty();
        } else if(streams.length < 2) {
            return streams[0];
        } else {
            return difference(streams[0], concat(Arrays.copyOfRange(streams, 1, streams.length - 1)));
        }
    }

    private static <T> Stream<T> difference(Stream<T> stream1, Stream<T> stream2) {
        Set<T> set2 = stream2.distinct().collect(Collectors.toSet());
        Predicate<T> notContainedInStream2 = ((Predicate<T>)set2::contains).negate();
        return stream1.filter(notContainedInStream2);
    }

    public static <T> Stream<T> nonNullStream(Collection<T> collection) {
        return collection.stream().filter(Objects::nonNull);
    }

    public static <T, U> Stream<U> mappedNonNullStream(Collection<T> collection, Function<T, U> function) {
        return collection.stream().filter(Objects::nonNull).map(function).filter(Objects::nonNull);
    }

}
