package planning.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import planning.model.Classroom;
import planning.model.Result;
import planning.modelVO.ClassroomVO;

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

    @RequestMapping(value = {"/", "/dashboard"})
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/classroom")
    public String getAllClassrooms(Model model, HttpServletRequest request,
                                   @RequestParam(defaultValue = "0") int page) {
        try {
            ResponseEntity<Result<Page<Classroom>>> classroomVOList = classroomController.getAllClassrooms(page, 2);

            if (classroomVOList.getBody() != null && classroomVOList.getBody().getResult() != null && !classroomVOList.getBody().getResult().isEmpty())
                model.addAttribute("classrooms", classroomVOList.getBody().getResult());
            else
                model.addAttribute("classroom", new ArrayList<ClassroomVO>());
        } catch (Exception ex) {
            model.addAttribute("classroom", new ArrayList<ClassroomVO>());
            model.addAttribute("errorMessage", ex.getMessage());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "classroom::#classroom-list";

        model.addAttribute("currentPage", page);

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
    public Optional<ClassroomVO> findOneClassroom(Model model, HttpServletRequest request, long id)   {
        try {
            ResponseEntity<Result<ClassroomVO>> classroom = classroomController.getClassroomById(id);

            if (classroom.getBody() != null && classroom.getBody().getResult() != null)
                return Optional.of(classroom.getBody().getResult());
        } catch (Exception ex) {
            return null;
        }

        return null;
    }

    @GetMapping("/classroom/delete")
    public String delete(long id) {
        classroomController.deleteClassroom(id);
        return "redirect:/classroom";
    }
}
