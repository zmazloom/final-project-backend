package planning.message;

public class CommonMessage {

    private static final String FA_PARAM_REQUIRED = "پارامتر " + "{PARAM}" + " نمی تواند خالی باشد.";

    private static final String FA_REQUEST_DENIED = "عدم دسترسی!";

    public static String getParamRequired(String parameterName) {
        return FA_PARAM_REQUIRED.replace("{PARAM}", parameterName);
    }

    public static String getRequestDenied() {
        return FA_REQUEST_DENIED;
    }

}
