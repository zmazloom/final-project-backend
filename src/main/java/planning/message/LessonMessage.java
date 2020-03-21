package planning.message;

public class LessonMessage {

    private static final String FA_LESSON_NOT_FOUND = "درس با شناسه " + "{LESSONID}" + " پیدا نشد.";

    private static final String FA_LESSON_DELETED = "کلاس با شناسه " + "{LESSONID}" + " حذف شد.";


    public static String getLessonNotFound(String lessonId) {
        return FA_LESSON_NOT_FOUND.replace("{LESSONID}", lessonId);
    }

    public static String getLessonDeleted(String lessonId) {
        return FA_LESSON_DELETED.replace("{LESSONID}", lessonId);
    }
}
