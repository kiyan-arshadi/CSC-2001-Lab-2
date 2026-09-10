import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class DateIntervalTest {

    static final SimpleDate NEW_YEARS_DAY = new SimpleDate(2025, 1, 1);
    static final SimpleDate END_OF_JANUARY = new SimpleDate(2025, 1, 31);

    static final DateInterval FIRST_TEN_DAYS =
            new DateInterval(new SimpleDate(2025, 1, 1), new SimpleDate(2025, 1, 10));
    static final DateInterval FIFTH_TO_FIFTEENTH =
            new DateInterval(new SimpleDate(2025, 1, 5), new SimpleDate(2025, 1, 15));
    static final DateInterval LATER_IN_JANUARY =
            new DateInterval(new SimpleDate(2025, 1, 20), new SimpleDate(2025, 1, 25));
    static final DateInterval CONTAINED_INSIDE =
            new DateInterval(new SimpleDate(2025, 1, 3), new SimpleDate(2025, 1, 4));
    static final DateInterval TENTH_ONWARDS =
            new DateInterval(new SimpleDate(2025, 1, 10), new SimpleDate(2025, 1, 20));
    static final DateInterval EMPTY_INTERVAL =
            new DateInterval(new SimpleDate(2025, 1, 5), new SimpleDate(2025, 1, 5));

    static final DateInterval FIFTH_TO_TENTH =
            new DateInterval(new SimpleDate(2025, 1, 5), new SimpleDate(2025, 1, 10));


    @Test
    void dateIntervalExamples() {
        DateInterval januaryFirstWeek =
                new DateInterval(new SimpleDate(2025, 1, 1), new SimpleDate(2025, 1, 8));
        DateInterval allOf2025 =
                new DateInterval(new SimpleDate(2025, 1, 1), new SimpleDate(2026, 1, 1));
        DateInterval justOneDay =
                new DateInterval(new SimpleDate(2025, 9, 9), new SimpleDate(2025, 9, 10));
    }

    @Test
    void constructorRejectsBackwardsIntervals() {
        assertThrows(IllegalArgumentException.class,
                () -> new DateInterval(END_OF_JANUARY, NEW_YEARS_DAY));
        assertDoesNotThrow(() -> new DateInterval(NEW_YEARS_DAY, NEW_YEARS_DAY));
    }

    @Test
    void dateIntervalDays() {
        assertEquals(0, DateInterval.dateIntervalDays(
                new DateInterval(NEW_YEARS_DAY, NEW_YEARS_DAY)));
        assertEquals(10, DateInterval.dateIntervalDays(
                new DateInterval(NEW_YEARS_DAY, new SimpleDate(2025, 1, 11))));
        assertEquals(1, DateInterval.dateIntervalDays(
                new DateInterval(END_OF_JANUARY, new SimpleDate(2025, 2, 1))));
        assertEquals(365, DateInterval.dateIntervalDays(
                new DateInterval(NEW_YEARS_DAY, new SimpleDate(2026, 1, 1))));
    }

    @Test
    void dateOverlap() {
        assertTrue(DateInterval.dateOverlap(FIRST_TEN_DAYS, FIFTH_TO_FIFTEENTH));
        assertTrue(DateInterval.dateOverlap(FIFTH_TO_FIFTEENTH, FIRST_TEN_DAYS));
        assertTrue(DateInterval.dateOverlap(FIRST_TEN_DAYS, CONTAINED_INSIDE));
        assertFalse(DateInterval.dateOverlap(FIRST_TEN_DAYS, LATER_IN_JANUARY));
        assertFalse(DateInterval.dateOverlap(FIRST_TEN_DAYS, TENTH_ONWARDS));
        assertFalse(DateInterval.dateOverlap(FIRST_TEN_DAYS, EMPTY_INTERVAL));
    }

    @Test
    void dateIntervalIntersect() {
        assertEquals(FIFTH_TO_TENTH,
                DateInterval.dateIntervalIntersect(FIRST_TEN_DAYS, FIFTH_TO_FIFTEENTH));
        assertEquals(FIFTH_TO_TENTH,
                DateInterval.dateIntervalIntersect(FIFTH_TO_FIFTEENTH, FIRST_TEN_DAYS));
        assertEquals(CONTAINED_INSIDE,
                DateInterval.dateIntervalIntersect(FIRST_TEN_DAYS, CONTAINED_INSIDE));
        assertNull(DateInterval.dateIntervalIntersect(FIRST_TEN_DAYS, LATER_IN_JANUARY));
        assertNull(DateInterval.dateIntervalIntersect(FIRST_TEN_DAYS, TENTH_ONWARDS));
    }

    @Test
    void nullableDateIntervalIntersect() {
        assertEquals(FIFTH_TO_TENTH,
                DateInterval.nullableDateIntervalIntersect(FIRST_TEN_DAYS, FIFTH_TO_FIFTEENTH));
        assertNull(DateInterval.nullableDateIntervalIntersect(null, FIFTH_TO_FIFTEENTH));
        assertNull(DateInterval.nullableDateIntervalIntersect(FIRST_TEN_DAYS, null));
        assertNull(DateInterval.nullableDateIntervalIntersect(null, null));
    }
}
