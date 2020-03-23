package planning.service;

import planning.model.Time;

public class TimeService {

    public static String getTimePersian(Time time) {
        switch (time) {
            case SHANBE8T:
                return "شنبه 8";
            case SHANBE10T:
                return "شنبه 10";
            case SHANBE12T:
            case SHANBE12O:
                return "شنبه 12";
            case SHANBE14T:
                return "شنبه 14";
            case SHANBE16T:
                return "شنبه 16";
            case SHANBE18O:
            case SHANBE18T:
                return "شنبه 18";
            case YEKSHANBE8T:
                return "یکشنبه 8";
            case YEKSHANBE10T:
                return "یکشنبه 10";
            case YEKSHANBE12O:
            case YEKSHANBE12T:
                return "یکشنبه 12";
            case YEKSHANBE14T:
                return "یکشنبه 14";
            case YEKSHANBE16T:
                return "یکشنبه 16";
            case YEKSHANBE18O:
            case YEKSHANBE18T:
                return "یکشنبه 18";
            case DOSHANBE8T:
                return "دوشنبه 8";
            case DOSHANBE10T:
                return "دوشنبه 10";
            case DOSHANBE12O:
            case DOSHANBE12T:
                return "دوشنبه 12";
            case DOSHANBE14T:
                return "دوشنبه 14";
            case DOSHANBE16T:
                return "دوشنبه 16";
            case DOSHANBE18O:
            case DOSHANBE18T:
                return "دوشنبه 18";
            case SESHANBE8T:
                return "سه شنبه 8";
            case SESHANBE10T:
                return "سه شنبه 10";
            case SESHANBE12O:
            case SESHANBE12T:
                return "سه شنبه 12";
            case SESHANBE14T:
                return "سه شنبه 14";
            case SESHANBE16T:
                return "سه شنبه 16";
            case SESHANBE18O:
            case SESHANBE18T:
                return "سه شنبه 18";
            case CHARSHANBE8T:
                return "چهارشنبه 8";
            case CHARSHANBE10T:
                return "چهارشنبه 10";
            case CHARSHANBE12O:
            case CHARSHANBE12T:
                return "چهارشنبه 12";
            case CHARSHANBE14T:
                return "چهارشنبه 14";
            case CHARSHANBE16T:
                return "چهارشنبه 16";
            case CHARSHANBE18O:
            case CHARSHANBE18T:
                return "چهارشنبه 18";

            case SHANBE730O:
                return "شنبه 7:30";
            case SHANBE9O:
                return "شنبه 9";
            case SHANBE1030O:
                return "شنبه 10:30";
            case SHANBE1330O:
                return "شنبه 13:30";
            case SHANBE15O:
                return "شنبه 15";
            case SHANBE1630O:
                return "شنبه 16:30";
            case YEKSHANBE730O:
                return "یکشنبه 7:30";
            case YEKSHANBE9O:
                return "یکشنبه 9";
            case YEKSHANBE1030O:
                return "یکشنبه 10:30";
            case YEKSHANBE1330O:
                return "یکشنبه 13:30";
            case YEKSHANBE15O:
                return "یکشنبه 15";
            case YEKSHANBE1630O:
                return "یکشنبه 16:30";
            case DOSHANBE730O:
                return "دوشنبه 7:30";
            case DOSHANBE9O:
                return "دوشنبه 9";
            case DOSHANBE1030O:
                return "دوشنبه 10:30";
            case DOSHANBE1330O:
                return "دوشنبه 13:30";
            case DOSHANBE15O:
                return "دوشنبه 15";
            case DOSHANBE1630O:
                return "دوشنبه 16:30";
            case SESHANBE730O:
                return "سه شنبه 7:30";
            case SESHANBE9O:
                return "سه شنبه 9";
            case SESHANBE1030O:
                return "سه شنبه 10:30";
            case SESHANBE1330O:
                return "سه شنبه 13:30";
            case SESHANBE15O:
                return "سه شنبه 15";
            case SESHANBE1630O:
                return "سه شنبه 16:30";
            case CHARSHANBE730O:
                return "چهارشنبه 7:30";
            case CHARSHANBE9O:
                return "چهارشنبه 9";
            case CHARSHANBE1030O:
                return "چهارشنبه 10:30";
            case CHARSHANBE1330O:
                return "چهارشنبه 13:30";
            case CHARSHANBE15O:
                return "چهارشنبه 15";
            case CHARSHANBE1630O:
                return "چهارشنبه 16:30";

        }

        return "";
    }
}
