package planning.message;

public class CommonMessage {

    private static final String FA_PARAM_REQUIRED = "پارامتر " + "{PARAM}" + " نمی تواند خالی باشد.";

    public static String getParamRequired(String parameterName) {
        return FA_PARAM_REQUIRED.replace("{PARAM}", parameterName);
    }

}
