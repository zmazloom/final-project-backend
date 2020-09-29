package planning.message;

public class PlanMessage {

    private static final String FA_PLAN_NOT_FOUND = "برنامه با شناسه " + "{PLANID}" + " پیدا نشد.";

    private static final String FA_PLAN_DELETED = "برنامه با شناسه " + "{PLANID}" + " حذف شد.";

    private static final String FA_DUPLICATE_PLAN = "برنامه با نام " + "{PLANNAME}" + " تکراری است!";

    private static final String FA_INVALID_LESSON_UNIT = "تعداد واحد درس " + "{LESSONNAME}" + " بیشتر از حد مجاز است.";

    private static final String FA_INVALID_TEACHER_TIME = "تداخل زمانی استاد " + "{TEACHERNAME}" + " در زمان " + "{TIME}";

    private static final String FA_INVALID_CLASS_TIME = "تداخل زمانی کلاس " + "{CLASSNAME}" + " در زمان " + "{TIME}";

    private static final String FA_INVALID_LESSON_TEACHER = "درس " + "{LESSONNAME}" + " به بیش از یک استاد انتساب دارد.";

    private static final String FA_GROUP_NOT_FOUND = "گروه با شناسه " + "{PLANID}" + " پیدا نشد.";

    private static final String FA_GROUP_DELETED = "گروه با شناسه " + "{GROUPID}" + " حذف شد.";

    public static String getDuplicatePlan(String planName) {
        return FA_DUPLICATE_PLAN.replace("{PLANNAME}", planName);
    }

    public static String getPlanNotFound(String planId) {
        return FA_PLAN_NOT_FOUND.replace("{PLANID}", planId);
    }

    public static String getPlanDeleted(String planId) {
        return FA_PLAN_DELETED.replace("{PLANID}", planId);
    }

    public static String getInvalidLessonUnit(String lessonName) {
        return FA_INVALID_LESSON_UNIT.replace("{LESSONNAME}", lessonName);
    }

    public static String getInvalidTeacherTime(String teacherName, String time) {
        return FA_INVALID_TEACHER_TIME.replace("{TEACHERNAME}", teacherName).replace("{TIME}", time);
    }

    public static String getInvalidClassTime(String className, String time) {
        return FA_INVALID_CLASS_TIME.replace("{CLASSNAME}", className).replace("{TIME}", time);
    }

    public static String getInvalidLessonTeacher(String lessonName) {
        return FA_INVALID_LESSON_TEACHER.replace("{LESSONNAME}", lessonName);
    }

    public static String getGroupNotFound(String groupId) {
        return FA_GROUP_NOT_FOUND.replace("{GROUPID}", groupId);
    }

    public static String getGroupDeleted(String groupId) {
        return FA_GROUP_DELETED.replace("{GROUPID}", groupId);
    }


}
