package planning.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import planning.model.Result;
import planning.modelVO.ClassroomVO;
import planning.modelVO.LessonVO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@Controller
@AllArgsConstructor
@RequestMapping(value = "")
public class PanelController {

    private static final String AJAX_HEADER_NAME = "X-Requested-With";
    private static final String AJAX_HEADER_VALUE = "XMLHttpRequest";

    private final ClassroomController classroomController;
    private final LessonController lessonController;

    @RequestMapping(value = {"/", "/dashboard"})
    public String getIndexPage() {
        return "index";
    }


    /******************** classroom *********************/
    @GetMapping("/classroom")
    public String getAllClassrooms(Model model, HttpServletRequest request) {
        try {
            ResponseEntity<Result<List<ClassroomVO>>> classroomVOList = classroomController.getAllClassrooms();

            if (classroomVOList.getBody() != null && classroomVOList.getBody().getResult() != null && !classroomVOList.getBody().getResult().isEmpty())
                model.addAttribute("classrooms", classroomVOList.getBody().getResult());
            else
                model.addAttribute("classrooms", new ArrayList<ClassroomVO>());
        } catch (Exception ex) {
            model.addAttribute("classrooms", new ArrayList<ClassroomVO>());
            model.addAttribute("errorMessage", ex.getMessage());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "classroom::#classroom-list";

        return "classroom";
    }

    @PostMapping("/classroom/add")
    public String addClassroom(Model model, HttpServletRequest request, ClassroomVO classroomVO) {
        try {
            ResponseEntity<Result<ClassroomVO>> classroom = classroomController.addClassroom(classroomVO);

            if (classroom.getBody() != null && classroom.getBody().getResult() != null)
                return "redirect:/classroom";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "redirect:/classroom";
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "classroom::#classroom-list";

        return "redirect:/classroom";
    }

    @PostMapping("/classroom/update")
    public String editClassroom(Model model, HttpServletRequest request, ClassroomVO classroom) {
        try {
            ResponseEntity<Result<ClassroomVO>> changedClassroom = classroomController.updateClassroom(classroom.getId(), classroom);

            if (changedClassroom.getBody() != null && changedClassroom.getBody().getResult() != null)
                return "redirect:/classroom";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "redirect:/classroom";
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "classroom::#classroom-list";

        return "redirect:/classroom";
    }

    @GetMapping("/classroom/one")
    @ResponseBody
    public Optional<ClassroomVO> findOneClassroom(Model model, HttpServletRequest request, long id) {
        try {
            ResponseEntity<Result<ClassroomVO>> classroom = classroomController.getClassroomById(id);

            if (classroom.getBody() != null && classroom.getBody().getResult() != null)
                return Optional.of(classroom.getBody().getResult());
        } catch (Exception ex) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    @GetMapping("/classroom/delete")
    public String deleteClassroom(long id) {
        classroomController.deleteClassroom(id);
        return "redirect:/classroom";
    }
    /******************** end *********************/

    /******************** lesson *********************/
    @GetMapping("/lesson")
    public String getAllLessons(Model model, HttpServletRequest request) {
        try {
            ResponseEntity<Result<List<LessonVO>>> lessonVOList = lessonController.getAllLessons();

            if (lessonVOList.getBody() != null && lessonVOList.getBody().getResult() != null && !lessonVOList.getBody().getResult().isEmpty())
                model.addAttribute("lessons", lessonVOList.getBody().getResult());
            else
                model.addAttribute("lessons", new ArrayList<LessonVO>());
        } catch (Exception ex) {
            model.addAttribute("lessons", new ArrayList<LessonVO>());
            model.addAttribute("errorMessage", ex.getMessage());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "lesson::#lesson-list";

        return "lesson";
    }

    @PostMapping("/lesson/add")
    public String addLesson(Model model, HttpServletRequest request, LessonVO lessonVO) {
        try {
            ResponseEntity<Result<LessonVO>> lesson = lessonController.addLesson(lessonVO);

            if (lesson.getBody() != null && lesson.getBody().getResult() != null)
                return "redirect:/lesson";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "redirect:/lesson";
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "lesson::#lesson-list";

        return "redirect:/lesson";
    }

    @PostMapping("/lesson/update")
    public String editLesson(Model model, HttpServletRequest request, LessonVO lessonVO) {
        try {
            ResponseEntity<Result<LessonVO>> changedLesson = lessonController.updateLesson(lessonVO.getId(), lessonVO);

            if (changedLesson.getBody() != null && changedLesson.getBody().getResult() != null)
                return "redirect:/lesson";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "redirect:/lesson";
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "lesson::#lesson-list";

        return "redirect:/lesson";
    }

    @GetMapping("/lesson/one")
    @ResponseBody
    public Optional<LessonVO> findOneLesson(Model model, HttpServletRequest request, long id) {
        try {
            ResponseEntity<Result<LessonVO>> lesson = lessonController.getLessonById(id);

            if (lesson.getBody() != null && lesson.getBody().getResult() != null)
                return Optional.of(lesson.getBody().getResult());
        } catch (Exception ex) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    @GetMapping("/lesson/delete")
    public String deleteLesson(long id) {
        lessonController.deleteLesson(id);
        return "redirect:/lesson";
    }
    /******************** end *********************/
}
