package planning.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping(value = "/api/classroom")
@AllArgsConstructor
public class ClassroomController {

    private final ClassroomCRUD classroomCRUD;
    private final ClassroomService classroomService;

    @PostMapping(value = "")
    public ResponseEntity<Result<ClassroomVO>> addClassroom(@RequestBody @NotNull @Validated ClassroomVO classroomVO) {
        if(classroomCRUD.getClassroomByName(classroomVO.getName()) != null)
            throw ResourceConflictException.getInstance(ClassroomMessage.getDuplicateClassroom(classroomVO.getName()));

        Classroom classroom = classroomService.addClassroom(classroomVO);

        return ResponseEntity.ok(ResFact.<ClassroomVO>build()
                .setResult(classroomService.getClassroomVO(classroom))
                .get());
    }

    @GetMapping(value = "")
    public ResponseEntity<Result<Page<Classroom>>> getAllClassrooms(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ResFact.<Page<Classroom>>build()
                .setResult(classroomCRUD.getAllClassrooms(PageRequest.of(page, size)))
                .get());
    }

    @GetMapping(value = "/{classId}")
    public ResponseEntity<Result<ClassroomVO>> getClassroomById(@PathVariable("classId") @NotNull Long classId) {
        Classroom classroom = classroomCRUD.getClassroomById(classId);

        if(classroom == null)
            throw ResourceNotFoundException.getInstance(ClassroomMessage.getClassNotFound(classId.toString()));

        return ResponseEntity.ok(ResFact.<ClassroomVO>build()
                .setResult(classroomService.getClassroomVO(classroom))
                .get());
    }

    @DeleteMapping("/{classId}")
    public ResponseEntity<Result<Boolean>> deleteClassroom(@PathVariable("classId") @NotNull Long classId) {
        Classroom classroom = classroomCRUD.getClassroomById(classId);

        if(classroom == null)
            throw ResourceNotFoundException.getInstance(ClassroomMessage.getClassNotFound(classId.toString()));

        classroomService.deleteClassroom(classroom);

        return ResponseEntity.ok(ResFact.<Boolean>build()
                .setResult(true)
                .setMessage(ClassroomMessage.getClassDeleted(classId.toString()))
                .get());
    }

    @PutMapping("/{classId}")
    public ResponseEntity<Result<ClassroomVO>> updateClassroom(@PathVariable("classId") @NotNull Long classId, @RequestBody @NotNull @Validated ClassroomVO classroomVO) {
        Classroom classroom = classroomCRUD.getClassroomById(classId);

        if(classroom == null)
            throw ResourceNotFoundException.getInstance(ClassroomMessage.getClassNotFound(classId.toString()));

        if(classroomCRUD.checkDuplicateClassName(classId, classroomVO.getName()) != null)
            throw ResourceConflictException.getInstance(ClassroomMessage.getDuplicateClassroom(classroomVO.getName()));

        Classroom changedClassroom = classroomService.updateClassroom(classroom, classroomVO);

        return ResponseEntity.ok(ResFact.<ClassroomVO>build()
                .setResult(classroomService.getClassroomVO(changedClassroom))
                .get());
    }

}
