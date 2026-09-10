
public record DateList(SimpleDate first, DateList rest) {

    public static int listLen(DateList dates) {
        return switch (dates) {
            case null -> 0;
            case DateList(SimpleDate first, DateList rest) -> 1 + listLen(rest);
        };
    }

    public static SimpleDate minDate(DateList dates) {
        return switch (dates) {
            case null -> null;
            case DateList(SimpleDate first, DateList rest) -> {
                SimpleDate restMin = minDate(rest);
                yield (restMin == null || SimpleDate.comesBefore(first, restMin)) ? first : restMin;
            }
        };
    }

    public static SimpleDate maxDate(DateList dates) {
        return switch (dates) {
            case null -> null;
            case DateList(SimpleDate first, DateList rest) -> {
                SimpleDate restMax = maxDate(rest);
                yield (restMax == null || SimpleDate.comesBefore(restMax, first)) ? first : restMax;
            }
        };
    }

    public static DateInterval dateCover(DateList dates) {
        SimpleDate earliest = minDate(dates);
        if (earliest == null) {
            return null;
        }
        return new DateInterval(earliest, maxDate(dates));
    }

    public static DateList allTomorrows(DateList dates) {
        return switch (dates) {
            case null -> null;
            case DateList(SimpleDate first, DateList rest) ->
                    new DateList(SimpleDate.tomorrow(first), allTomorrows(rest));
        };
    }

    public static DateList addToEnd(DateList dates, SimpleDate date) {
        return switch (dates) {
            case null -> new DateList(date, null);
            case DateList(SimpleDate first, DateList rest) ->
                    new DateList(first, addToEnd(rest, date));
        };
    }

    public static DateList append(DateList firstList, DateList secondList) {
        return switch (firstList) {
            case null -> secondList;
            case DateList(SimpleDate first, DateList rest) ->
                    new DateList(first, append(rest, secondList));
        };
    }
}
