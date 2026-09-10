import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DateListTest {

    static final SimpleDate JAN_1 = new SimpleDate(2025, 1, 1);
    static final SimpleDate JAN_5 = new SimpleDate(2025, 1, 5);
    static final SimpleDate MAR_3 = new SimpleDate(2025, 3, 3);
    static final SimpleDate DEC_31 = new SimpleDate(2025, 12, 31);


    static final DateList THREE_DATES =
            new DateList(MAR_3, new DateList(JAN_1, new DateList(JAN_5, null)));

    @Test
    void dateListExamples() {
        DateList empty = null;
        DateList justNewYears = new DateList(JAN_1, null);
        DateList threeDays = new DateList(JAN_1, new DateList(JAN_5, new DateList(MAR_3, null)));
    }

    @Test
    void listLen() {
        assertEquals(0, DateList.listLen(null));
        assertEquals(1, DateList.listLen(new DateList(JAN_1, null)));
        assertEquals(3, DateList.listLen(THREE_DATES));
    }

    @Test
    void minDate() {
        assertNull(DateList.minDate(null));
        assertEquals(JAN_1, DateList.minDate(new DateList(JAN_1, null)));
        assertEquals(JAN_1, DateList.minDate(THREE_DATES));
        assertEquals(JAN_1, DateList.minDate(new DateList(JAN_1, new DateList(DEC_31, null))));
    }

    @Test
    void maxDate() {
        assertNull(DateList.maxDate(null));
        assertEquals(JAN_1, DateList.maxDate(new DateList(JAN_1, null)));
        assertEquals(MAR_3, DateList.maxDate(THREE_DATES));
        assertEquals(DEC_31, DateList.maxDate(new DateList(JAN_1, new DateList(DEC_31, null))));
    }

    @Test
    void dateCover() {
        assertNull(DateList.dateCover(null));
        assertEquals(new DateInterval(JAN_1, JAN_1), DateList.dateCover(new DateList(JAN_1, null)));
        assertEquals(new DateInterval(JAN_1, MAR_3), DateList.dateCover(THREE_DATES));
    }

    @Test
    void allTomorrows() {
        assertNull(DateList.allTomorrows(null));
        DateList expected = new DateList(new SimpleDate(2025, 3, 4),
                new DateList(new SimpleDate(2025, 1, 2),
                        new DateList(new SimpleDate(2025, 1, 6), null)));
        assertEquals(expected, DateList.allTomorrows(THREE_DATES));
        assertEquals(3, DateList.listLen(DateList.allTomorrows(THREE_DATES)));
        assertEquals(new DateList(new SimpleDate(2026, 1, 1), null),
                DateList.allTomorrows(new DateList(DEC_31, null)));
    }

    @Test
    void addToEnd() {
        assertEquals(new DateList(JAN_1, null), DateList.addToEnd(null, JAN_1));
        DateList expected = new DateList(MAR_3,
                new DateList(JAN_1, new DateList(JAN_5, new DateList(DEC_31, null))));
        assertEquals(expected, DateList.addToEnd(THREE_DATES, DEC_31));
        assertEquals(4, DateList.listLen(DateList.addToEnd(THREE_DATES, DEC_31)));
        assertEquals(3, DateList.listLen(THREE_DATES));
    }

    @Test
    void append() {
        DateList pair = new DateList(DEC_31, new DateList(JAN_5, null));

        assertNull(DateList.append(null, null));
        assertEquals(pair, DateList.append(null, pair));
        assertEquals(THREE_DATES, DateList.append(THREE_DATES, null));
        DateList expected = new DateList(MAR_3,
                new DateList(JAN_1,
                        new DateList(JAN_5,
                                new DateList(DEC_31, new DateList(JAN_5, null)))));
        assertEquals(expected, DateList.append(THREE_DATES, pair));
        assertEquals(5, DateList.listLen(DateList.append(THREE_DATES, pair)));
    }
}
