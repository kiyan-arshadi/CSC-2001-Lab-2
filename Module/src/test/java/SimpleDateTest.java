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
}
