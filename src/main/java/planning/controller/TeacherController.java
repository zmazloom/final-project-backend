package planning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import planning.exception.ResourceNotFoundException;
import planning.message.TeacherMessage;
import planning.model.ResFact;
import planning.model.Result;
import planning.model.Teacher;
import planning.modelVO.TeacherVO;
import planning.repository.TeacherCRUD;
import planning.service.TeacherService;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/teacher")
public class TeacherController {

    private final TeacherCRUD teacherCRUD;

    @Autowired
    public TeacherController(TeacherCRUD teacherCRUD) {
        this.teacherCRUD = teacherCRUD;
    }

    @GetMapping(value = "")
    public ResponseEntity<Result<List<TeacherVO>>> getAllTeachers() {
        return ResponseEntity.ok(ResFact.<List<TeacherVO>>build()
                .setResult(TeacherService.getTeacherVOs(teacherCRUD.getAllTeachers()))
                .get());
    }

    @GetMapping(value = "/{teacherId}")
    public ResponseEntity<Result<TeacherVO>> getTeacherById(@PathVariable("teacherId") @NotNull Long teacherId) {
        Teacher teacher = teacherCRUD.getTeacherById(teacherId);

        if(teacher == null)
            throw ResourceNotFoundException.getInstance(TeacherMessage.getTeacherNotFound(teacherId.toString()));

        return ResponseEntity.ok(ResFact.<TeacherVO>build()
                .setResult(TeacherService.getTeacherVO(teacher))
                .get());
    }

    @DeleteMapping(value = "/{teacherId}")
    public ResponseEntity<Result<Boolean>> deleteTeacherById(@PathVariable("teacherId") @NotNull Long teacherId) {
        Teacher teacher = teacherCRUD.getTeacherById(teacherId);

        if(teacher == null)
            throw ResourceNotFoundException.getInstance(TeacherMessage.getTeacherNotFound(teacherId.toString()));

        teacherCRUD.deleteTeacher(teacher);

        return ResponseEntity.ok(ResFact.<Boolean>build()
                .setResult(true)
                .setMessage(TeacherMessage.getTeacherDeleted(teacherId.toString()))
                .get());
    }

}