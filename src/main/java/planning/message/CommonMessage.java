package planning.message;

public class CommonMessage {

    private static final String FA_HIBERNATE_ERROR = "خطای کار با پایگاه داده رخ داد.";

    public static String getHibernateError() {
        return FA_HIBERNATE_ERROR;
    }

}
