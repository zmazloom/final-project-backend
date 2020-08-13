package planning.model;

public enum TeacherPrefix {
    DOCTOR("DOCTOR"),

    OSTAD_TAMAM("OSTAD_TAMAM"),

    OSTAD("OSTAD"),

    MOHANDES("MOHANDES");

    private final String prefix;

    private TeacherPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
