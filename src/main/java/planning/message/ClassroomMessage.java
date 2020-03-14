package planning.message;

public class ClassroomMessage {

    private static final String FA_DUPLICATE_CLASSROOM = "کلاس " + "{CLASSNAME}" + " تکراری است!";

    private static final String FA_CLASS_NOT_FOUND = "کلاس با شناسه " + "{CLASSID}" + " پیدا نشد.";

    private static final String FA_CLASS_DELETED = "کلاس با شناسه " + "{CLASSID}" + " حذف شد.";

    public static String getDuplicateClassroom(String className) {
        return FA_DUPLICATE_CLASSROOM.replace("{CLASSNAME}", className);
    }

    public static String getClassNotFound(String classId) {
        return FA_CLASS_NOT_FOUND.replace("{CLASSID}", classId);
    }

    public static String getClassDeleted(String classId) {
        return FA_CLASS_DELETED.replace("{CLASSID}", classId);
    }

}
