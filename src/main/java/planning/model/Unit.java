package planning.model;

public enum Unit {
    ZERO("ZERO"),

    ONE("ONE"),

    TWO("TWO"),

    THREE("THREE"),

    FOUR("FOUR");

    private final String unit;

    private Unit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
