package com.scottshipp.code.mill.stream;


import com.scottshipp.code.mill.data.Birthday;
import com.scottshipp.code.mill.data.Holiday;
import com.scottshipp.code.mill.data.TestValues;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ValueComparisonPredicatesTest {

    // similar use case to https://stackoverflow.com/questions/47774076/java-8-stream-filter-with-comparator
    @Test
    public void testFilterMemberDataGreaterThan() {
        // Pretend it's July 4, 2018
        LocalDate independenceDay = LocalDate.of(2018, 7, 4);
        String holidaysComingUp = TestValues.allHolidays()
                .filter(ValueComparisonPredicates.where(Holiday::getDate).isGreaterThan(independenceDay))
                .collect(MoreCollectors.joining(", "));
        assertEquals("2018-09-03, 2018-11-22, 2018-11-23, 2018-12-24, 2018-12-25", holidaysComingUp);
    }

    @Test
    public void testFilterMemberDataToEmptyList() {
        // Pretend it's March 5, 2018
        LocalDate basisDate = LocalDate.of(2018, 1, 1);
        String noHolidays = TestValues.allHolidays()
                .filter(ValueComparisonPredicates.where(Holiday::getDate).isLessThan(basisDate))
                .collect(MoreCollectors.joining(", "));
        assertEquals("", noHolidays);
    }

    @Test
    public void testFilterMemberDataByComposedPredicates() {
        String holidaysInJanuaryAndDecember = TestValues.allHolidays()
                .filter(
                        ValueComparisonPredicates.where(Holiday::getDate).isGreaterThanOrEqualTo(LocalDate.of(2018, 12, 1))
                                .or(ValueComparisonPredicates.where(Holiday::getDate).isLessThanOrEqualTo(LocalDate.of(2018,1,31)))
                )
                .collect(MoreCollectors.joining(", "));
        assertEquals("2018-01-01, 2018-12-24, 2018-12-25", holidaysInJanuaryAndDecember);
    }

//    @Test
//    public void testFilterElementsLessThan();
//
//    @Test
//    public void testFilterElementsGreaterThanOrEqualTo();
//
//    @Test
//    public void testFilterElementsEqualTo();
//
//    @Test
//    public void testFilterElementsInClosedRange();
//
//    @Test
//    public void testFilterElementsInOpenRange();

    @Test
    public void testFilterMemberDataIsBetween() {
        String youngerOnes = TestValues.allBirthdays()
                .filter(ValueComparisonPredicates.where(Birthday::birthday).isBetween(LocalDate.of(1990, 1, 1), LocalDate.now()))
                .collect(MoreCollectors.joining(", "));
        assertEquals("Jane: 01/12, Wanda: 08/11", youngerOnes);
    }

}
