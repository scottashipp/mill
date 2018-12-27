package com.scottshipp.code.mill.data;

import java.time.LocalDate;
import java.util.stream.Stream;

public final class TestValues {

    public static final Holiday NEW_YEARS = new Holiday(LocalDate.of(2018, 1, 1));
    public static final Holiday MEMORIAL_DAY = new Holiday(LocalDate.of(2018, 5, 28));
    public static final Holiday INDEPENDENCE_DAY = new Holiday(LocalDate.of(2018, 7, 4));
    public static final Holiday LABOR_DAY = new Holiday(LocalDate.of(2018, 9, 3));
    public static final Holiday THANKSGIVING = new Holiday(LocalDate.of(2018, 11, 22));
    public static final Holiday DAY_AFTER_THANKSGIVING = new Holiday(THANKSGIVING.getDate().plusDays(1));
    public static final Holiday CHRISTMAS = new Holiday(LocalDate.of(2018, 12, 25));
    public static final Holiday DAY_BEFORE_CHRISTMAS = new Holiday(CHRISTMAS.getDate().minusDays(1));

    public static Stream<Holiday> allHolidays() {
        return Stream.of(NEW_YEARS, MEMORIAL_DAY,
                INDEPENDENCE_DAY, LABOR_DAY, THANKSGIVING, DAY_AFTER_THANKSGIVING,
                DAY_BEFORE_CHRISTMAS, CHRISTMAS);
    }

    public static final Birthday JOHN = new Birthday("John", LocalDate.of(1954, 11, 22));
    public static final Birthday ALICE = new Birthday("Alice", LocalDate.of(1973, 5, 24));
    public static final Birthday JANE = new Birthday("Jane", LocalDate.of(1991, 1, 12));
    public static final Birthday BEN = new Birthday("Wanda", LocalDate.of(1993, 8, 11));

    public static Stream<Birthday> allBirthdays() {
        return Stream.of(JOHN, ALICE, JANE, BEN);
    }
}
