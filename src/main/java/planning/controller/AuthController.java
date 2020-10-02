package planning.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import planning.exception.ResourceNotFoundException;
import planning.message.TeacherMessage;
import planning.model.*;
import planning.modelVO.LoginVO;
import planning.modelVO.TokenVO;
import planning.repository.TeacherCRUD;
import planning.service.LoginService;

import javax.validation.constraints.NotNull;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/auth")
@AllArgsConstructor
public class AuthController {

    private final LoginService loginService;
    private final TeacherCRUD teacherCRUD;

    @PostMapping(value = "/login")
    public ResponseEntity<Result<TokenVO>> login(@RequestBody @NotNull @Validated LoginVO loginVO) {
        Teacher teacher = teacherCRUD.getTeacherByUserPass(loginVO.getUsername(), loginVO.getPassword());

        if (teacher == null)
            throw ResourceNotFoundException.getInstance(TeacherMessage.getWrongAuth());

        Token token = loginService.loginUser(teacher);

        TokenVO tokenVO = new TokenVO();
        tokenVO.setToken(token.getUserToken());

        if (teacher.getRole().equals(Role.ROLE_ADMIN))
            tokenVO.setAddress("/");
        else
            tokenVO.setAddress("/teacher_page");

        return ResponseEntity.ok(ResFact.<TokenVO>build()
                .setResult(tokenVO)
                .get());
    }
}
