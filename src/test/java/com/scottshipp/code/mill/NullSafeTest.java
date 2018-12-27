package com.scottshipp.code.mill;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NullSafeTest {

    private Function<String, String> NULL_RESULT = (s) -> null;
    private Function<String, String> REMOVE_FIRST_LETTER = (s) -> s.substring(1);

    @Test
    public void testOneArgument() {
        String s = "123456789";
        s = NullSafe.of(s).get(REMOVE_FIRST_LETTER);
        assertEquals("23456789", s);
    }

    @Test
    public void testOneArgumentNullResult() {
        String s = "123456789";
        s = NullSafe.of(s).get(NULL_RESULT);
        assertNull(s);
    }

    @Test
    public void testOneArgumentNullInput() {
        String s = null;
        s = NullSafe.of(s).get(REMOVE_FIRST_LETTER);
        assertNull(s);
    }

    @Test
    public void testFourArguments() {
        String s = "123456789";
        s = NullSafe.of(s)
                .call(REMOVE_FIRST_LETTER)
                .call(REMOVE_FIRST_LETTER)
                .call(REMOVE_FIRST_LETTER)
                .get(REMOVE_FIRST_LETTER);
        assertEquals("56789", s);
    }

    @Test
    public void testFourArgumentsWithNullFn() {
        String s = "123456789";
        s = NullSafe.of(s)
                .call(REMOVE_FIRST_LETTER)
                .call(REMOVE_FIRST_LETTER)
                .call(NULL_RESULT)
                .get(REMOVE_FIRST_LETTER);
        assertNull(s);
    }

    @Test
    public void testFourArgumentsWithNullFnAndException() {
        String exceptionMsg = "something fishy";
        String s = "123456789";
        RuntimeException ex = new RuntimeException(exceptionMsg);

        Throwable e = assertThrows(RuntimeException.class, () -> {
                    NullSafe.of(s)
                        .call(REMOVE_FIRST_LETTER)
                        .call(REMOVE_FIRST_LETTER)
                        .call(NULL_RESULT)
                        .call(REMOVE_FIRST_LETTER)
                        .getOrThrow(ex);
                });
        assertEquals(exceptionMsg, e.getMessage());
    }

}
