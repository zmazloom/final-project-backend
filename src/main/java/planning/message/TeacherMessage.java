package planning.message;

public class TeacherMessage {

    private static final String FA_TEACHER_NOT_FOUND = "استاد با شناسه " + "{TEACHERID}" + " پیدا نشد.";

    private static final String FA_TEACHER_DELETED = "استاد با شناسه " + "{TEACHERID}" + " حذف شد.";

    private static final String FA_DUPLICATE_TEACHER = "استاد با نام کاربری " + "{USERNAME}" + " تکراری است!";

    private static final String FA_ERROR_SAVE_AVATAR = "خطای داخلی در ذخیره تصویر!";

    private static final String FA_INVALID_USERNAME = "پارامتر username باید بزگتر از 3 کاراکتر باشد.";

    private static final String FA_INVALID_FORMAT_AVATAR = "فرمت تصویر باید JPG یا PNG باشد.";

    private static final String FA_INVALID_SIZE_AVATAR = "اندازه تصویر باید کمتر از 1 مگابایت باشد.";


    public static String getTeacherNotFound(String teacherId) {
        return FA_TEACHER_NOT_FOUND.replace("{TEACHERID}", teacherId);
    }

    public static String getTeacherDeleted(String teacherId) {
        return FA_TEACHER_DELETED.replace("{TEACHERID}", teacherId);
    }

    public static String getDuplicateTeacher(String username) {
        return FA_DUPLICATE_TEACHER.replace("{USERNAME}", username);
    }

    public static String getErrorSaveAvatar() {
        return FA_ERROR_SAVE_AVATAR;
    }

    public static String getInvalidUsername() {
        return FA_INVALID_USERNAME;
    }

    public static String getInvalidFormatAvatar() {
        return FA_INVALID_FORMAT_AVATAR;
    }

    public static String getInvalidSizeAvatar() {
        return FA_INVALID_SIZE_AVATAR;
    }
}
