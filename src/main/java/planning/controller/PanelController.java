package planning.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import planning.model.Classroom;
import planning.model.Result;
import planning.modelVO.ClassroomVO;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
@AllArgsConstructor
@RequestMapping(value = "")
public class PanelController {

    private final ClassroomController classroomController;

    @GetMapping(value = {"/", "/dashboard"})
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/classroom")
    public String getAllClassrooms(Model model) {
        try {
            ResponseEntity<Result<List<ClassroomVO>>> classroomVOList = classroomController.getAllClassrooms();

            if (classroomVOList.getBody() != null && classroomVOList.getBody().getResult() != null && !classroomVOList.getBody().getResult().isEmpty())
                model.addAttribute("classrooms", classroomVOList.getBody().getResult());
            else
                model.addAttribute("classroom", new ArrayList<ClassroomVO>());
        } catch (Exception ex) {
            model.addAttribute("classroom", new ArrayList<ClassroomVO>());
            model.addAttribute("errorMessage", ex.getMessage());
        }

        return "classroom";
    }

}
