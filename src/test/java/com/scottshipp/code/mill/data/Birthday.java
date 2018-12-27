package com.scottshipp.code.mill.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class Birthday {
    String name;
    LocalDate birthday;

    public Birthday(String name, LocalDate birthday) {
        this.name = name;
        this.birthday = birthday;
    }

    public String name() {
        return name;
    }

    public LocalDate birthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return name + ": " + birthday.format(DateTimeFormatter.ofPattern("MM/dd"));
    }
}
