package planning.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import planning.exception.ResourceNotFoundException;
import planning.message.LessonMessage;
import planning.model.Lesson;
import planning.model.ResFact;
import planning.model.Result;
import planning.modelVO.LessonVO;
import planning.repository.LessonCRUD;
import planning.service.LessonService;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin
@Controller
@AllArgsConstructor
@RequestMapping(value = "/api/lesson")
public class LessonController {

    private final LessonCRUD lessonCRUD;
    private final LessonService lessonService;


    @PostMapping(value = "")
    public ResponseEntity<Result<LessonVO>> addLesson(@RequestBody @NotNull @Validated LessonVO lessonVO) {
        Lesson lesson = lessonService.addLesson(lessonVO);

        return ResponseEntity.ok(ResFact.<LessonVO>build()
                .setResult(lessonService.getLessonVO(lesson))
                .get());
    }

    @GetMapping(value = "")
    public ResponseEntity<Result<List<LessonVO>>> getAllLessons() {
        return ResponseEntity.ok(ResFact.<List<LessonVO>>build()
                .setResult(lessonService.getLessonVOs(lessonCRUD.getAllLessons()))
                .get());
    }

    @GetMapping(value = "/{lessonId}")
    public ResponseEntity<Result<LessonVO>> getLessonById(@PathVariable("lessonId") @NotNull Long lessonId) {
        Lesson lesson = lessonCRUD.getLessonById(lessonId);

        if(lesson == null)
            throw ResourceNotFoundException.getInstance(LessonMessage.getLessonNotFound(lessonId.toString()));

        return ResponseEntity.ok(ResFact.<LessonVO>build()
                .setResult(lessonService.getLessonVO(lesson))
                .get());
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Result<Boolean>> deleteLesson(@PathVariable("lessonId") @NotNull Long lessonId) {
        Lesson lesson = lessonCRUD.getLessonById(lessonId);

        if(lesson == null)
            throw ResourceNotFoundException.getInstance(LessonMessage.getLessonNotFound(lessonId.toString()));

        lessonService.deleteLesson(lesson);

        return ResponseEntity.ok(ResFact.<Boolean>build()
                .setResult(true)
                .setMessage(LessonMessage.getLessonDeleted(lessonId.toString()))
                .get());
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<Result<LessonVO>> updateLesson(@PathVariable("lessonId") @NotNull Long lessonId,
                                                         @RequestBody LessonVO lessonVO) {
        Lesson lesson = lessonCRUD.getLessonById(lessonId);

        if(lesson == null)
            throw ResourceNotFoundException.getInstance(LessonMessage.getLessonNotFound(lessonId.toString()));

        Lesson changedLesson = lessonService.updateLesson(lesson, lessonVO);

        return ResponseEntity.ok(ResFact.<LessonVO>build()
                .setResult(lessonService.getLessonVO(changedLesson))
                .get());
    }

    @GetMapping("/count")
    public ResponseEntity<Result<Long>> getLessonsCount() {
        return ResponseEntity.ok(ResFact.<Long>build()
                .setResult(lessonCRUD.getLessonsCount())
                .get());
    }

}
