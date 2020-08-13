package planning.model;

public enum TimeType {
    TWO_HOURS("TWO_HOURS"),

    ONE_THIRTY_HOURS("ONE_THIRTY_HOURS");

    private final String timeType;

    private TimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getTimeType() {
        return timeType;
    }
}
