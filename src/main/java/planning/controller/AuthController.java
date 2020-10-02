package planning.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import planning.exception.AccessDeniedException;
import planning.exception.InvalidRequestException;
import planning.exception.ResourceNotFoundException;
import planning.message.CommonMessage;
import planning.message.TeacherMessage;
import planning.model.*;
import planning.modelVO.LoginVO;
import planning.modelVO.TeacherAddVO;
import planning.modelVO.TeacherVO;
import planning.modelVO.TokenVO;
import planning.repository.TeacherCRUD;
import planning.service.LoginService;
import planning.service.TeacherService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/auth")
@AllArgsConstructor
public class AuthController {

    private final LoginService loginService;
    private final TeacherCRUD teacherCRUD;
    private final TeacherService teacherService;

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

    @PutMapping(value = "/profile/{userId}")
    public ResponseEntity<Result<TeacherVO>> updateUser(HttpServletRequest request,
                                                        @PathVariable("userId") @NotNull Long userId,
                                                        @RequestBody @NotNull @Validated TeacherAddVO teacherAddVO) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_USER))
            throw AccessDeniedException.getInstance(CommonMessage.getRequestDenied());

        Teacher user = teacherCRUD.getTeacherById(userId);

        if (user == null)
            throw ResourceNotFoundException.getInstance(TeacherMessage.getTeacherNotFound(userId.toString()));

        if (teacherAddVO.getUsername() != null && !teacherAddVO.getUsername().equals("") && teacherCRUD.checkDuplicateTeacherUsername(userId, teacherAddVO.getUsername()) != null)
            throw InvalidRequestException.getInstance(TeacherMessage.getDuplicateTeacher(teacherAddVO.getUsername()));

        return ResponseEntity.ok(ResFact.<TeacherVO>build()
                .setResult(teacherService.getTeacherVO(teacherService.updateProfile(user, teacherAddVO)))
                .get());
    }
}
