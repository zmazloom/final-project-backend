package planning.model;

public enum Grade {
    KARSHENASI("KARSHENASI"),

    ARSHAD("ARSHAD"),

    DOCTORA("DOCTORA");

    private final String grade;

    private Grade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }
}
