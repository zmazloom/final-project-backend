package planning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import planning.exception.ResourceConflictException;
import planning.exception.ResourceNotFoundException;
import planning.message.ClassroomMessage;
import planning.model.Classroom;
import planning.model.Result;
import planning.modelVO.ClassroomVO;
import planning.model.ResFact;
import planning.repository.ClassroomCRUD;
import planning.service.ClassroomService;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/classroom")
public class ClassroomController {

    private final ClassroomCRUD classroomCRUD;

    @Autowired
    public ClassroomController(ClassroomCRUD classroomCRUD) {
        this.classroomCRUD = classroomCRUD;
    }

    @PostMapping(value = "")
    public ResponseEntity<Result<ClassroomVO>> addClassroom(@RequestBody @NotNull ClassroomVO classroomVO) {
        if(classroomCRUD.getClassroomByName(classroomVO.getName()) != null)
            throw ResourceConflictException.getInstance(ClassroomMessage.getDuplicateClassroom(classroomVO.getName()));

        Classroom classroom = classroomCRUD.addClassroom(classroomVO);

        return ResponseEntity.ok(ResFact.<ClassroomVO>build()
                .setResult(ClassroomService.getClassroomVO(classroom))
                .get());
    }

    @GetMapping(value = "")
    public ResponseEntity<Result<List<ClassroomVO>>> getAllClassrooms() {
        return ResponseEntity.ok(ResFact.<List<ClassroomVO>>build()
                .setResult(ClassroomService.getClassroomVOs(classroomCRUD.getAllClassrooms()))
                .get());
    }

    @GetMapping(value = "/{classId}")
    public ResponseEntity<Result<ClassroomVO>> getClassroomById(@PathVariable("classId") @NotNull Long classId) {
        Classroom classroom = classroomCRUD.getClassroomById(classId);

        if(classroom == null)
            throw ResourceNotFoundException.getInstance(ClassroomMessage.getClassNotFound(classId.toString()));

        return ResponseEntity.ok(ResFact.<ClassroomVO>build()
                .setResult(ClassroomService.getClassroomVO(classroom))
                .get());
    }

    @DeleteMapping("/{classId}")
    public ResponseEntity<Result<Boolean>> deleteClassroom(@PathVariable("classId") @NotNull Long classId) {
        Classroom classroom = classroomCRUD.getClassroomById(classId);

        if(classroom == null)
            throw ResourceNotFoundException.getInstance(ClassroomMessage.getClassNotFound(classId.toString()));

        classroomCRUD.deleteClassroom(classroom);

        return ResponseEntity.ok(ResFact.<Boolean>build()
                .setResult(true)
                .setMessage(ClassroomMessage.getClassDeleted(classId.toString()))
                .get());
    }

    @PutMapping("/{classId}")
    public ResponseEntity<Result<ClassroomVO>> updateClassroom(@PathVariable("classId") @NotNull Long classId, @RequestBody ClassroomVO classroomVO) {
        Classroom classroom = classroomCRUD.getClassroomById(classId);

        if(classroom == null)
            throw ResourceNotFoundException.getInstance(ClassroomMessage.getClassNotFound(classId.toString()));

        if(classroomCRUD.checkDuplicateClassName(classId, classroomVO.getName()) != null)
            throw ResourceConflictException.getInstance(ClassroomMessage.getDuplicateClassroom(classroomVO.getName()));

        Classroom changedClassroom = classroomCRUD.updateClassroom(classroom, classroomVO);

        return ResponseEntity.ok(ResFact.<ClassroomVO>build()
                .setResult(ClassroomService.getClassroomVO(changedClassroom))
                .get());
    }

}
