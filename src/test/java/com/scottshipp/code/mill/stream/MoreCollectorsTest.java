package com.scottshipp.code.mill.stream;

import static com.scottshipp.code.mill.data.TestValues.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MoreCollectorsTest {

    @Test
    public void testJoiningCollector() {

        String standardJava = majorUsHolidays()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        assertEquals("2018-01-01, 2018-04-01, 2018-05-28, 2018-07-04, 2018-09-03, 2018-11-22, 2018-11-23, 2018-12-24, 2018-12-25", standardJava);

        // mill doesn't require the extra map(Object::toString) call
        String withMill = majorUsHolidays().collect(MoreCollectors.joining(", "));
        assertEquals(standardJava, withMill);
    }

    @Test
    public void testCollectorIgnoringNulls() {
        Collector<String, ?, List<String>> nullExcludingListCollector =
                MoreCollectors.excludingNull(toList());
        List<String> fruit = Stream.of(null, null, "Banana",
                "Orange", null, "Kiwi", "Mango", null,
                "Apple")
                .collect(nullExcludingListCollector);
        assertEquals(
                "Banana, Orange, Kiwi, Mango, Apple",
                fruit.stream().collect(Collectors.joining(", "))
        );
    }

    @Test
    public void testCollectorIgnoringCustom() {
        List<String> berries = Stream.of("", null, "", null, "Strawberry",
                "Blueberry", null, "Huckleberry", "Raspberry", "", null,
                "Blackberry").collect(MoreCollectors.excluding(StringPredicates.isNull().or(StringPredicates.isEmpty()), toList()));
        assertEquals(
                "Strawberry, Blueberry, Huckleberry, Raspberry, Blackberry",
                berries.stream().collect(Collectors.joining(", "))
        );
    }

    @Test
    public void testCollectorIgnoringCustom2() {
        String veggies = Stream.of("", null, "", null, "Tomato",
                "Lettuce", null, "Onion", "Beet", "", null,
                "Turnip")
                .collect(
                        MoreCollectors.excluding(s -> s == null || s.length() == 0, Collectors.joining(", "))
                );
        assertEquals(
                "Tomato, Lettuce, Onion, Beet, Turnip",
                veggies
        );
    }

    @Test
    public void testOnlyIncluding() {
        Collector<String, ?, List<String>> nullExcludingListCollector =
                MoreCollectors.including(s -> s != null && s.length() > 1, toList());
        List<String> pFruit = Stream.of(null, null, "Pineapple",
                "Papaya", null, "Peach", "Plum", null,
                "Pear")
                .collect(nullExcludingListCollector);
        assertEquals(
                "Pineapple, Papaya, Peach, Plum, Pear",
                pFruit.stream().collect(MoreCollectors.joining(", "))
        );
    }

    @Test
    public void testTypedCollector() {
        // RcsMessage, SmsMessage, and MmsMessage are all instances of Message (see below)
        Stream<Message> messages = Stream.of(new RcsMessage(1), new SmsMessage(2), new MmsMessage(3));
        List<RcsMessage> rcsMessages = messages.collect(MoreCollectors.typedCollector(RcsMessage.class, ArrayList::new));
        assertEquals(1, rcsMessages.size());
        assertEquals("RcsMessage { id: 1 }", rcsMessages.get(0).toString());
    }

    interface Message {
        void send();
        boolean wasSent();
    }

    class SmsMessage implements Message {
        private final int messageId;
        private boolean sent;

        SmsMessage(int messageId) {
            this.messageId = messageId;
        }

        @Override
        public void send() {
            sent = true;
        }

        @Override
        public boolean wasSent() {
            return sent;
        }

        @Override
        public String toString() {
            return "SmsMessage { id: " + messageId + " }";
        }
    }

    class MmsMessage implements Message {
        private final int messageId;
        private boolean sent;

        MmsMessage(int messageId) {
            this.messageId = messageId;
        }

        @Override
        public void send() {
            sent = false;
        }

        @Override
        public boolean wasSent() {
            return sent;
        }

        @Override
        public String toString() {
            return "MmsMessage { id: " + messageId + " }";
        }
    }

    class RcsMessage implements Message {
        private final int messageId;
        private boolean sent;

        RcsMessage(int messageId) {
            this.messageId = messageId;
        }

        @Override
        public void send() {
            sent = true;
        }

        @Override
        public boolean wasSent() {
            return sent;
        }

        @Override
        public String toString() {
            return "RcsMessage { id: " + messageId + " }";
        }
    }

}
