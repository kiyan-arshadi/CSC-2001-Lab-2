public record DateInterval(SimpleDate start, SimpleDate end) {

    public DateInterval {
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
