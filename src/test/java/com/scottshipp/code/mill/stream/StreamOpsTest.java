package com.scottshipp.code.mill.stream;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class StreamOpsTest {

    @Test
    public void testFourStreamUnion() {
        Stream<String> sdets = Stream.of("Jane Brown", "Mukesh Jaffery");
        Stream<String> engineers = Stream.of("Mackenzie Miller", "Tracy Roberts", "Frankie Chen", "Emerson Lorrie", "Jean Limon", "Finley Vonnegut");
        Stream<String> manager = Stream.of("Riley Joson");
        Stream<String> tpm = Stream.of("Shannon Smith");

        // Readable way
        Stream<String> engineeringTeam2 = StreamOps.concat(sdets, engineers, manager, tpm);
        assertEquals(
                "Jane Brown, Mukesh Jaffery, Mackenzie Miller, Tracy Roberts, Frankie Chen, " +
                        "Emerson Lorrie, Jean Limon, Finley Vonnegut, Riley Joson, Shannon Smith",
                engineeringTeam2.collect(Collectors.joining(", ")));
    }

    @Test
    public void testStreamConversionOfStreams() {
        Stream<String> engineeringTeam1 = Stream.of(
                "Shannon Smith", "Riley Joson",
                "Reese Livermore", "Harper Olsen", "Parker Smolich",
                "Rory Rivers", "Tatum Greene"
        );

        Stream<String> engineeringTeam2 = Stream.of(
                "Mackenzie Miller", "Jane Brown", "Shannon Smith",
                "Riley Joson", "Tracy Roberts", "Frankie Chen",
                "Emerson Lorrie", "Mukesh Jaffery",
                "Jean Limon", "Finley Vonnegut"
        );


        assertEquals(
                "Shannon Smith, Riley Joson, Reese Livermore, Harper Olsen, Parker Smolich, Rory Rivers, Tatum Greene, Mackenzie Miller, Jane Brown, Shannon Smith, Riley Joson, Tracy Roberts, Frankie Chen, Emerson Lorrie, Mukesh Jaffery, Jean Limon, Finley Vonnegut",
                StreamOps.concat(engineeringTeam1, engineeringTeam2).collect(Collectors.joining(", ")));

    }

    @Test
    public void testStreamConversionOfArrays() {
        String[] engineeringTeam1 = {
                "Shannon Smith", "Riley Joson",
                "Reese Livermore", "Harper Olsen", "Parker Smolich",
                "Rory Rivers", "Tatum Greene"
        };

        String[] engineeringTeam2 = {
                "Mackenzie Miller", "Jane Brown", "Shannon Smith",
                "Riley Joson", "Tracy Roberts", "Frankie Chen",
                "Emerson Lorrie", "Mukesh Jaffery",
                "Jean Limon", "Finley Vonnegut"
        };


        assertEquals(
                "Shannon Smith, Riley Joson, Reese Livermore, Harper Olsen, Parker Smolich, Rory Rivers, Tatum Greene, Mackenzie Miller, Jane Brown, Shannon Smith, Riley Joson, Tracy Roberts, Frankie Chen, Emerson Lorrie, Mukesh Jaffery, Jean Limon, Finley Vonnegut",
                StreamOps.concat(engineeringTeam1, engineeringTeam2).collect(Collectors.joining(", ")));

    }

    @Test
    public void testStreamConversionOfCollections() {
        List<String> engineeringTeam1 = Arrays.asList(
                "Shannon Smith", "Riley Joson",
                "Reese Livermore", "Harper Olsen", "Parker Smolich",
                "Rory Rivers", "Tatum Greene"
        );

        List<String> engineeringTeam2 = Arrays.asList(
                "Mackenzie Miller", "Jane Brown", "Shannon Smith",
                "Riley Joson", "Tracy Roberts", "Frankie Chen",
                "Emerson Lorrie", "Mukesh Jaffery",
                "Jean Limon", "Finley Vonnegut"
        );

        assertEquals(
                "Shannon Smith, Riley Joson, Reese Livermore, Harper Olsen, Parker Smolich, Rory Rivers, Tatum Greene, Mackenzie Miller, Jane Brown, Shannon Smith, Riley Joson, Tracy Roberts, Frankie Chen, Emerson Lorrie, Mukesh Jaffery, Jean Limon, Finley Vonnegut",
                StreamOps.concat(engineeringTeam1, engineeringTeam2).collect(Collectors.joining(", "))
        );
    }

    @Test
    public void testIntersectionOfOneStream() {
        assertEquals(0, StreamOps.intersection(Stream.empty()).count());
    }

    @Test
    public void testIntersectionOfTwoStreams() {
        Stream<String> engineeringTeam1 = Stream.of(
                "Shannon Smith", "Riley Joson",
                "Reese Livermore", "Harper Olsen", "Parker Smolich",
                "Rory Rivers", "Tatum Greene"
        );

        Stream<String> engineeringTeam2 = Stream.of(
                "Mackenzie Miller", "Jane Brown", "Shannon Smith",
                "Riley Joson", "Tracy Roberts", "Frankie Chen",
                "Emerson Lorrie", "Mukesh Jaffery",
                "Jean Limon", "Finley Vonnegut"
        );

        assertEquals(
                "Riley Joson, Shannon Smith",
                StreamOps.intersection(engineeringTeam1, engineeringTeam2).sorted()
                        .collect(Collectors.joining(", "))
        );
    }

    @Test
    public void testIntersectionOfFourStreams() {
        Stream<String> engineeringTeam1 = Stream.of(
                "Shannon Smith", "Riley Joson",
                "Reese Livermore", "Harper Olsen", "Parker Smolich",
                "Rory Rivers", "Tatum Greene"
        );

        Stream<String> engineeringTeam2 = Stream.of(
                "Mackenzie Miller", "Jane Brown", "Shannon Smith",
                "Riley Joson", "Tracy Roberts", "Frankie Chen",
                "Emerson Lorrie", "Mukesh Jaffery",
                "Jean Limon", "Finley Vonnegut"
        );

        Stream<String> hiringManagers = Stream.of("Riley Joson", "Lita Monaghan", "Hans Mornirz");

        Stream<String> barRaisers = Stream.of("Frankie Chen", "Riley Joson", "Jack Kennedy");

        assertEquals(
                "Riley Joson",
                StreamOps.intersection(engineeringTeam1, engineeringTeam2, hiringManagers, barRaisers)
                        .collect(Collectors.joining(", "))
        );
    }

    @Test
    public void testSpecificIncorrectImplementationOfIntersection() {
        assertEquals(1,
                StreamOps.intersection(
                        Stream.of(1, 2, 3),
                        Stream.of(1),
                        Stream.of(1, 2, 3),
                        Stream.of(1, 2, 3)
                ).count());
    }

    @Test
    public void testDifferenceOfStreams() {
        Stream<String> engineeringTeam1 = Stream.of(
                "Shannon Smith", "Riley Joson",
                "Reese Livermore", "Harper Olsen", "Parker Smolich",
                "Rory Rivers", "Tatum Greene"
        );

        Stream<String> engineeringTeam2 = Stream.of(
                "Mackenzie Miller", "Jane Brown", "Shannon Smith",
                "Riley Joson", "Tracy Roberts", "Frankie Chen",
                "Emerson Lorrie", "Mukesh Jaffery",
                "Jean Limon", "Finley Vonnegut"
        );

        Stream<String> hiringManagers = Stream.of("Riley Joson", "Winter Valwest", "Hans Mornirz");

        Stream<String> barRaisers = Stream.of("Frankie Chen", "Riley Joson", "Jack Kennedy");

        assertEquals(
                "Harper Olsen, Parker Smolich, Reese Livermore, Rory Rivers, Tatum Greene",
                StreamOps.difference(engineeringTeam1, engineeringTeam2, hiringManagers, barRaisers).sorted()
                        .collect(Collectors.joining(", "))
        );
    }

    @Test
    public void testNonNullStreams() {
        List<String> listWithNulls = Arrays.asList(null, null, "Harper",
                "Reese", "Frankie", null, null, null, null);
        String results = StreamOps.nonNullStream(listWithNulls).collect(Collectors.joining(", "));
        assertEquals("Harper, Reese, Frankie", results);
    }

    @Test
    public void testMappedNonNullStreams() {
        List<String> nullsAndEmpties = Arrays.asList(null, null, "Mackenzie Miller",
                "Riley Joson", "Jean Limon", null, null, null, null);
        String results = StreamOps.mappedNonNullStream(nullsAndEmpties, s -> s.split(" ")[0])
                .collect(Collectors.joining(", "));
        assertEquals("Mackenzie, Riley, Jean", results);
    }
}
