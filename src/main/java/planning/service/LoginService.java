package planning.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import planning.model.Role;
import planning.model.Teacher;
import planning.model.Token;
import planning.repository.TokenCRUD;
import planning.utils.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class LoginService {

    private final TokenCRUD tokenCRUD;

    public Token loginUser(Teacher teacher) {
        String generatedToken = TokenUtils.generateToken(teacher);

        Token token = new Token();
        token.setTeacher(teacher);
        token.setUserToken(generatedToken);

        return tokenCRUD.saveAndFlush(token);
    }

    public boolean checkServiceAccess(HttpServletRequest request, Role requiredRole) {
        try {
            if (request.getHeader("Cookie") != null) {
                List<String> cookies = Arrays.asList(request.getHeader("Cookie").split(";"));
                if (!cookies.isEmpty()) {
                    for (String cookie : cookies) {
                        if (cookie.contains("MazMazAuthorization")) {
                            List<Token> tokens = tokenCRUD.getTokenByToken(cookie.substring(cookie.indexOf("=") + 1));

                            if (tokens == null || tokens.isEmpty())
                                return false;

                            if (!tokens.get(tokens.size() - 1).getTeacher().getRole().equals(requiredRole) && requiredRole.equals(Role.ROLE_ADMIN))
                                return false;

                            return true;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            return false;
        }

        return false;
    }

}
