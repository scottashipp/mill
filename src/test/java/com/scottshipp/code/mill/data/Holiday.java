package com.scottshipp.code.mill.data;

import java.time.LocalDate;

public final class Holiday {
    private final LocalDate date;

    public  Holiday(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date.toString();
    }
}
