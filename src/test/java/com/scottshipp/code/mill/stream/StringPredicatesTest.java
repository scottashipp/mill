package com.scottshipp.code.mill.stream;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottshipp.code.mill.stream.ComparablePredicates.isBetween;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class StringPredicatesTest {

    @Test
    public void motivatingExample() {
        Stream<String> engineeringTeam = Stream.of(
                null, "Mackenzie Miller", "Jane Brown", "Shannon Smith",
                "Riley Joson", "Tracy Roberts", "Frankie Chen",
                null, null, "Emerson Lorrie", "Mukesh Jaffery",
                "Jean Limon", "Finley Vonnegut", ""
        );

        String staffAtoMWithShortNames = engineeringTeam
                .filter(Objects::nonNull)
                .filter(((Predicate<String>)String::isEmpty).negate())
                .filter(s -> s.compareTo("A") > 0 && s.compareTo("M") < 0)
                .filter(s -> s.length() <= 12)
                .collect(Collectors.joining(", "));

        assertEquals("Jane Brown, Frankie Chen, Jean Limon", staffAtoMWithShortNames);

        engineeringTeam = Stream.of(
                null, "Mackenzie Miller", "Jane Brown", "Shannon Smith",
                "Riley Joson", "Tracy Roberts", "Frankie Chen",
                null, null, "Emerson Lorrie", "Mukesh Jaffery",
                "Jean Limon", "Finley Vonnegut", ""
        );

        // Readable way
        Predicate<String> maxLen12AndRangeAToM =
                StringPredicates.nonNull().and(StringPredicates.nonEmpty())
                .and(isBetween("A", "M"))
                .and(StringPredicates.withMaximumLength(12));
        staffAtoMWithShortNames = engineeringTeam.filter(maxLen12AndRangeAToM).collect(Collectors.joining(", "));

        assertEquals("Jane Brown, Frankie Chen, Jean Limon", staffAtoMWithShortNames);
    }

    @Test
    public void testContainsFilter() {
        Stream<String> jobTitles = Stream.of("Director, Engineering",
                "Manager, Engineering", "Jr. Software Engineer", "Software Engineer",
                "Sr. Software Engineer", "Program Manager", "Sr. Program Manager");

        String softwareEngineers = jobTitles.filter(StringPredicates.containing("Software Engineer")).sorted().collect(Collectors.joining(", "));
        assertEquals("Jr. Software Engineer, Software Engineer, Sr. Software Engineer",
                softwareEngineers);
    }
}
