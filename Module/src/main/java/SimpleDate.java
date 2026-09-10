
public record SimpleDate(int year, int month, int day) {

    public static final int DAYS_IN_YEAR = 365;

    public SimpleDate {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("month must be 1..12, got: " + month);
        }
        if (day < 1 || day > daysInMonth(month)) {
            throw new IllegalArgumentException(
                    "day must be 1.." + daysInMonth(month) + " for month " + month + ", got: " + day);
        }
    }

    public static int daysInMonth(int month) {
        return switch (month) {
            case 1, 3, 5, 7, 8, 10, 12 -> 31;
            case 4, 6, 9, 11 -> 30;
            case 2 -> 28;
            default -> throw new IllegalArgumentException("month must be 1..12, got: " + month);
        };
    }

    public static SimpleDate tomorrow(SimpleDate date) {
        if (date.day() < daysInMonth(date.month())) {
            return new SimpleDate(date.year(), date.month(), date.day() + 1);
        } else if (date.month() < 12) {
            return new SimpleDate(date.year(), date.month() + 1, 1);
        } else {
            return new SimpleDate(date.year() + 1, 1, 1);
        }
    }

    public static int dayOfYear(SimpleDate date) {
        int days = date.day() - 1;
        for (int m = 1; m < date.month(); m++) {
            days += daysInMonth(m);
        }
        return days;
    }

    public static int dayNumber(SimpleDate date) {
        return date.year() * DAYS_IN_YEAR + dayOfYear(date);
    }

    public static boolean comesBefore(SimpleDate first, SimpleDate second) {
        return dayNumber(first) <= dayNumber(second);
    }
}

record DateInterval(SimpleDate start, SimpleDate end) {

    DateInterval {
        if (!SimpleDate.comesBefore(start, end)) {
            throw new IllegalArgumentException("interval ends before it starts: " + start + " .. " + end);
        }
    }

    public static int dateIntervalDays(DateInterval interval) {
        return SimpleDate.dayNumber(interval.end()) - SimpleDate.dayNumber(interval.start());
    }

    public static boolean dateOverlap(DateInterval a, DateInterval b) {
        int laterStart = Math.max(SimpleDate.dayNumber(a.start()), SimpleDate.dayNumber(b.start()));
        int earlierEnd = Math.min(SimpleDate.dayNumber(a.end()), SimpleDate.dayNumber(b.end()));
        return laterStart < earlierEnd;
    }

    public static DateInterval dateIntervalIntersect(DateInterval a, DateInterval b) {
        if (!dateOverlap(a, b)) {
            return null;
        }
        SimpleDate laterStart =
                SimpleDate.comesBefore(a.start(), b.start()) ? b.start() : a.start();
        SimpleDate earlierEnd =
                SimpleDate.comesBefore(a.end(), b.end()) ? a.end() : b.end();
        return new DateInterval(laterStart, earlierEnd);
    }

    public static DateInterval nullableDateIntervalIntersect(DateInterval a, DateInterval b) {
        if (a == null || b == null) {
            return null;
        }
        return dateIntervalIntersect(a, b);
    }
}
