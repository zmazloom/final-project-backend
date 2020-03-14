package planning.message;

public class TeacherMessage {

    private static final String FA_TEACHER_NOT_FOUND = "استاد با شناسه " + "{TEACHERID}" + " پیدا نشد.";

    private static final String FA_TEACHER_DELETED = "استاد با شناسه " + "{TEACHERID}" + " حذف شد.";

    public static String getTeacherNotFound(String teacherId) {
        return FA_TEACHER_NOT_FOUND.replace("{TEACHERID}", teacherId);
    }

    public static String getTeacherDeleted(String teacherId) {
        return FA_TEACHER_DELETED.replace("{TEACHERID}", teacherId);
    }
}
