import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class SimpleDateTest {

    static final SimpleDate NEW_YEARS_DAY = new SimpleDate(2025, 1, 1);
    static final SimpleDate END_OF_JANUARY = new SimpleDate(2025, 1, 31);
    static final SimpleDate END_OF_FEBRUARY = new SimpleDate(2025, 2, 28);
    static final SimpleDate NEW_YEARS_EVE = new SimpleDate(2025, 12, 31);


    void dateExamples() {
        SimpleDate moonLanding = new SimpleDate(1969, 7, 20);
        SimpleDate leapDayThatIsNot = new SimpleDate(2024, 2, 28);
        SimpleDate someTuesday = new SimpleDate(2025, 9, 9);
    }

    @Test
    void daysInMonth() {
        assertEquals(31, SimpleDate.daysInMonth(1));
        assertEquals(31, SimpleDate.daysInMonth(12));
        assertEquals(30, SimpleDate.daysInMonth(4));
        assertEquals(30, SimpleDate.daysInMonth(11));
        assertEquals(28, SimpleDate.daysInMonth(2));
    }

    @Test
    void daysInMonthRejectsBadMonths() {
        assertThrows(IllegalArgumentException.class, () -> SimpleDate.daysInMonth(0));
        assertThrows(IllegalArgumentException.class, () -> SimpleDate.daysInMonth(13));
        assertThrows(IllegalArgumentException.class, () -> SimpleDate.daysInMonth(-3));
    }

    @Test
    void tomorrow() {
        assertEquals(new SimpleDate(2025, 1, 2), SimpleDate.tomorrow(NEW_YEARS_DAY));
        assertEquals(new SimpleDate(2025, 2, 1), SimpleDate.tomorrow(END_OF_JANUARY));
        assertEquals(new SimpleDate(2025, 3, 1), SimpleDate.tomorrow(END_OF_FEBRUARY));
        assertEquals(new SimpleDate(2024, 3, 1), SimpleDate.tomorrow(new SimpleDate(2024, 2, 28)));
        assertEquals(new SimpleDate(2026, 1, 1), SimpleDate.tomorrow(NEW_YEARS_EVE));
    }

    @Test
    void dayOfYear() {
        assertEquals(0, SimpleDate.dayOfYear(NEW_YEARS_DAY));
        assertEquals(1, SimpleDate.dayOfYear(new SimpleDate(2025, 1, 2)));
        assertEquals(31, SimpleDate.dayOfYear(new SimpleDate(2025, 2, 1)));
        assertEquals(59, SimpleDate.dayOfYear(new SimpleDate(2025, 3, 1)));
        assertEquals(364, SimpleDate.dayOfYear(NEW_YEARS_EVE));
    }

    @Test
    void comesBefore() {
        assertTrue(SimpleDate.comesBefore(NEW_YEARS_DAY, END_OF_JANUARY));
        assertFalse(SimpleDate.comesBefore(END_OF_JANUARY, NEW_YEARS_DAY));
        assertTrue(SimpleDate.comesBefore(NEW_YEARS_DAY, NEW_YEARS_DAY));
        assertTrue(SimpleDate.comesBefore(new SimpleDate(2024, 12, 31), NEW_YEARS_DAY));
        assertFalse(SimpleDate.comesBefore(NEW_YEARS_DAY, new SimpleDate(2024, 12, 31)));
    }

    @Test
    void constructorRejectsIllegalDates() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleDate(2025, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> new SimpleDate(2025, 13, 1));
        assertThrows(IllegalArgumentException.class, () -> new SimpleDate(2025, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> new SimpleDate(2025, 1, 32));
        assertThrows(IllegalArgumentException.class, () -> new SimpleDate(2025, 4, 31));
        assertThrows(IllegalArgumentException.class, () -> new SimpleDate(2024, 2, 29));
    }

    @Test
    void constructorAcceptsLegalEdgeCases() {
        assertDoesNotThrow(() -> new SimpleDate(2025, 2, 28));
        assertDoesNotThrow(() -> new SimpleDate(2025, 12, 31));
        assertDoesNotThrow(() -> new SimpleDate(0, 1, 1));
    }

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
    void dateIntervalConstructorRejectsBackwardsIntervals() {
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
        DateInterval firstTenDays =
                new DateInterval(new SimpleDate(2025, 1, 1), new SimpleDate(2025, 1, 10));
        DateInterval fifthToFifteenth =
                new DateInterval(new SimpleDate(2025, 1, 5), new SimpleDate(2025, 1, 15));
        DateInterval laterInJanuary =
                new DateInterval(new SimpleDate(2025, 1, 20), new SimpleDate(2025, 1, 25));
        DateInterval containedInside =
                new DateInterval(new SimpleDate(2025, 1, 3), new SimpleDate(2025, 1, 4));


        assertTrue(DateInterval.dateOverlap(firstTenDays, fifthToFifteenth));
        assertTrue(DateInterval.dateOverlap(fifthToFifteenth, firstTenDays));
        assertTrue(DateInterval.dateOverlap(firstTenDays, containedInside));
        assertFalse(DateInterval.dateOverlap(firstTenDays, laterInJanuary));


        DateInterval tenthOnwards =
                new DateInterval(new SimpleDate(2025, 1, 10), new SimpleDate(2025, 1, 20));
        assertFalse(DateInterval.dateOverlap(firstTenDays, tenthOnwards));

        DateInterval empty =
                new DateInterval(new SimpleDate(2025, 1, 5), new SimpleDate(2025, 1, 5));
        assertFalse(DateInterval.dateOverlap(firstTenDays, empty));
    }

    @Test
    void dateIntervalIntersect() {
        DateInterval firstTenDays =
                new DateInterval(new SimpleDate(2025, 1, 1), new SimpleDate(2025, 1, 10));
        DateInterval fifthToFifteenth =
                new DateInterval(new SimpleDate(2025, 1, 5), new SimpleDate(2025, 1, 15));
        DateInterval laterInJanuary =
                new DateInterval(new SimpleDate(2025, 1, 20), new SimpleDate(2025, 1, 25));
        DateInterval containedInside =
                new DateInterval(new SimpleDate(2025, 1, 3), new SimpleDate(2025, 1, 4));


        assertEquals(new DateInterval(new SimpleDate(2025, 1, 5), new SimpleDate(2025, 1, 10)),
                DateInterval.dateIntervalIntersect(firstTenDays, fifthToFifteenth));
        assertEquals(new DateInterval(new SimpleDate(2025, 1, 5), new SimpleDate(2025, 1, 10)),
                DateInterval.dateIntervalIntersect(fifthToFifteenth, firstTenDays));
        assertEquals(containedInside,
                DateInterval.dateIntervalIntersect(firstTenDays, containedInside));
        assertNull(DateInterval.dateIntervalIntersect(firstTenDays, laterInJanuary));
        DateInterval tenthOnwards =
                new DateInterval(new SimpleDate(2025, 1, 10), new SimpleDate(2025, 1, 20));
        assertNull(DateInterval.dateIntervalIntersect(firstTenDays, tenthOnwards));
    }

    @Test
    void nullableDateIntervalIntersect() {
        DateInterval firstTenDays =
                new DateInterval(new SimpleDate(2025, 1, 1), new SimpleDate(2025, 1, 10));
        DateInterval fifthToFifteenth =
                new DateInterval(new SimpleDate(2025, 1, 5), new SimpleDate(2025, 1, 15));

        assertEquals(new DateInterval(new SimpleDate(2025, 1, 5), new SimpleDate(2025, 1, 10)),
                DateInterval.nullableDateIntervalIntersect(firstTenDays, fifthToFifteenth));
        assertNull(DateInterval.nullableDateIntervalIntersect(null, fifthToFifteenth));
        assertNull(DateInterval.nullableDateIntervalIntersect(firstTenDays, null));
        assertNull(DateInterval.nullableDateIntervalIntersect(null, null));
    }
}
