package com.scottshipp.code.mill.stream;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;

/**
 * Additional stream collectors to augment those in java.util.stream.Collectors.
 *
 */
public final class MoreCollectors {

    private MoreCollectors() {
        // static methods only
    }

    /**
     * Returns a Collector that concatenates the toString() value of the input
     * elements into a String, in encounter order.
     *
     * You can use java.util.stream.Collectors.joining on an object only
     *  after mapping it Object::toString like this:
     * <pre>
     *     {@code
     *     // java.util.stream.Collectors.joining usage
     *     String joined = things.stream()
     *                          .map(Object::toString)
     *                          .collect(Collectors.joining(", "));
     *     }
     * </pre>
     *
     * With MoreCollectors.joining, the mapping is unnecessary:
     * <pre>
     *     {@code
     *     String joined = things.stream().collect(MoreCollectors.joining(", "));
     *     }
     * </pre>
     *
     * @param delimiter the delimiter to be used between each element
     * @param <T> The type of element (will be inferred from the Stream)
     * @return a Collector that concatenates the toString() value of the input elements into a String, in encounter order
     * @see java.util.stream.Collector
     * @see java.util.stream.Stream#collect
     */
    public static <T> Collector<T, ?, String> joining(CharSequence delimiter) {
        return joining(delimiter, "", "");
    }

    /**
     * Returns a Collector that concatenates the toString() value of the input
     * elements into a String, in encounter order.
     *
     * @param delimiter the delimiter to be used between each element
     * @param prefix the sequence of characters to be used at the beginning of the joined result
     * @param suffix the sequence of characters to be used at the end of the joined result
     * @param <T> The type of element (will be inferred from the Stream)
     * @return a Collector that concatenates the toString() value of the input elements into a String, in encounter order
     * @see java.util.stream.Collector
     * @see java.util.stream.Stream#collect
     */
    public static <T> Collector<T, ?, String> joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return Collector.of(
                () -> new StringJoiner(delimiter, prefix, suffix),
                (a, t) -> a.add(t == null ? "null" : t.toString()),
                StringJoiner::merge,
                StringJoiner::toString
        );
    }

    public static <T, A, R> Collector<T, A, R> including(Predicate<T> predicate, Collector<T, A, R> collector) {
        return Collector.of(
                collector.supplier(),
                (s, t) -> {
                    if(predicate.test(t)) {
                        collector.accumulator().accept(s, t);
                    }
                },
                collector.combiner(),
                collector.finisher(),
                setToArray(collector.characteristics())
        );
    }

    public static <T, A, R> Collector<T, A, R> excludingNull(Collector<T, A, R> collector) {
        return excluding(Objects::isNull, collector);
    }

    public static <T, A, R> Collector<T, A, R> excluding(Predicate<T> predicate, Collector<T, A, R> collector) {
        return Collector.of(
                collector.supplier(),
                (s, t) -> {
                    if(predicate.negate().test(t)) {
                        collector.accumulator().accept(s, t);
                    }
                },
                collector.combiner(),
                collector.finisher(),
                setToArray(collector.characteristics())
        );
    }

    private static Collector.Characteristics[] setToArray(Set<Collector.Characteristics> characteristics) {
        return characteristics.toArray(new Collector.Characteristics[characteristics.size()]);
    }

    /**
     * Collects elements matching the given type, into a collection provided
     * by the given supplier.
     *
     * <pre>
     *     {@code
     *     List<RcsMessage> results = Stream.of(new RcsMessage(1), new SmsMessage(2), new MmsMessage(3))
     *           .collect(CollectorOps.typedCollector(RcsMessage.class, ArrayList::new));
     *     }
     * </pre>
     *
     * @param clazz the type of element to collect
     * @param supplier a supplier for this collector, generally a method reference to a collections object constructor, such as ArrayList::new
     * @param <T> the type of input elements to the reduction operation
     * @param <S> the type of output elements in the resulting collection
     * @param <R> the collection supplied by the supplier
     * @return a new collection of the elements that were instances of S
     */
    public static <T, S extends T, R extends Collection<S>> Collector<T, ?, R>  typedCollector(Class<S> clazz, Supplier<R> supplier) {
        return Collector.of(
                supplier,
                (R collection, T o) -> {
                    if (clazz.isInstance(o)) {
                        collection.add(clazz.cast(o));
                    }
                },
                (R r1, R r2) -> { r1.addAll(r2); return r1; },
                IDENTITY_FINISH
        );
    }

}
