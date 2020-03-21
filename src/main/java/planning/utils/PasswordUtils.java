package planning.utils;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordUtils {

    private static final int PASSWORD_LENGTH = 10;
    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";


    public static String generatePassword() {
        StringBuilder returnValue = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(returnValue);
    }
}
