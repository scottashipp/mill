package com.scottshipp.code.mill.stream;

import com.scottshipp.code.mill.data.Birthday;
import com.scottshipp.code.mill.data.Holiday;
import com.scottshipp.code.mill.data.TestValues;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottshipp.code.mill.stream.ComparatorPredicates.where;
import static com.scottshipp.code.mill.stream.MoreCollectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ComparatorPredicatesTest {

    @Test
    public void testComparingPredicateBuilder() {
        Stream<Birthday> someBirthdaysJ8 = TestValues.allBirthdays();
        Stream<Birthday> someBirthdays = Stream.of(TestValues.JOHN, TestValues.ALICE, TestValues.JANE, TestValues.BEN);

        // Java 8
        String olderThanAlice = someBirthdaysJ8
                .filter(
                        t -> {
                            Comparator<Birthday> comparingBirthdays = Comparator.comparing(Birthday::birthday);
                            return comparingBirthdays.compare(t, TestValues.ALICE) > 0;
                        }
                )
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        assertEquals("Jane: 01/12, Wanda: 08/11", olderThanAlice);

        // This library
        Comparator<Birthday> comparingBirthdays = Comparator.comparing(Birthday::birthday);
        olderThanAlice = someBirthdays
                .filter(
                        where(comparingBirthdays).isGreaterThan(TestValues.ALICE)
                        // or without the comparator, just use where(Birthday::birthday).isGreaterThan(ALICE)
                )
                .collect(joining(", "));
        assertEquals("Jane: 01/12, Wanda: 08/11", olderThanAlice);
    }

    @Test
    public void testLessThanWithMethodRef() {
        String holidaysBeforeIndependenceDay = TestValues.allHolidays()
                .filter(ComparatorPredicates.where(Holiday::getDate).isLessThan(TestValues.INDEPENDENCE_DAY))
                .collect(joining(", "));
        assertEquals("2018-01-01, 2018-05-28", holidaysBeforeIndependenceDay);
    }

    @Test
    public void testLessThanOrEqualsWithMethodRef() {
        String holidaysBeforeIndependenceDay = TestValues.allHolidays()
                .filter(ComparatorPredicates.where(Holiday::getDate).isLessThanOrEqualTo(TestValues.INDEPENDENCE_DAY))
                .collect(joining(", "));
        assertEquals("2018-01-01, 2018-05-28, 2018-07-04", holidaysBeforeIndependenceDay);
    }

    @Test
    public void testGreaterThanWithMethodRef() {
        String holidaysAfterIndependenceDay = TestValues.allHolidays()
                .filter(ComparatorPredicates.where(Holiday::getDate).isGreaterThan(TestValues.INDEPENDENCE_DAY))
                .collect(joining(", "));
        assertEquals("2018-09-03, 2018-11-22, 2018-11-23, 2018-12-24, 2018-12-25", holidaysAfterIndependenceDay);
    }

    @Test
    public void testGreaterThanOrEqualsWithMethodRef() {
        String holidays = TestValues.allHolidays()
                .filter(ComparatorPredicates.where(Holiday::getDate).isGreaterThanOrEqualTo(TestValues.INDEPENDENCE_DAY))
                .collect(joining(", "));
        assertEquals("2018-07-04, 2018-09-03, 2018-11-22, 2018-11-23, 2018-12-24, 2018-12-25", holidays);
    }

    @Test
    public void testClosedRangeWithMethodRef() {
        String holidays = TestValues.allHolidays()
                            .filter(
                                    ComparatorPredicates.where(Holiday::getDate).isInRangeClosed(TestValues.LABOR_DAY, TestValues.CHRISTMAS)
                            )
                            .collect(joining(", "));
        assertEquals("2018-09-03, 2018-11-22, 2018-11-23, 2018-12-24, 2018-12-25", holidays);
    }

    @Test
    public void testOpenRangeWithMethodRef() {
        String holidays = TestValues.allHolidays()
                            .filter(
                                    ComparatorPredicates.where(Holiday::getDate).isInRangeOpen(TestValues.LABOR_DAY, TestValues.CHRISTMAS)
                            )
                            .collect(joining(", "));
        assertEquals("2018-11-22, 2018-11-23, 2018-12-24", holidays);
    }

    @Test
    public void testWithStandardLibraryObjectsAndComparators() {
        // notice that some have capital first letters and some have lower
        Stream<String> engineeringTeam = Stream.of("Parker", "Winter", "Mukesh", "Jean", "Mackenzie", "Shannon", "Tatum", "Jane");
        String engineeringTeamAtoP = engineeringTeam.filter(where(String.CASE_INSENSITIVE_ORDER).isLessThanOrEqualTo("Parker")).collect(joining(", "));
        assertEquals("Parker, Mukesh, Jean, Mackenzie, Jane", engineeringTeamAtoP);
    }


}
