package planning.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import planning.model.Teacher;

public class TokenUtils {

    public static String generateToken(Teacher teacher) {

        Algorithm algorithmHS = Algorithm.HMAC512("secretstringforproject123");

        return JWT.create()
                .withIssuer("SAKKU-ISS")
                .withSubject(String.valueOf(teacher.getId()))
                .withArrayClaim("role", new String[]{teacher.getRole().toString()})
                .sign(algorithmHS);
    }

}
