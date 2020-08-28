package planning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import planning.model.PlanDetailGet;
import planning.model.Result;
import planning.modelVO.ClassroomVO;
import planning.modelVO.LessonVO;
import planning.modelVO.PlanVO;
import planning.modelVO.TeacherVO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@Controller
@RequestMapping(value = "")
public class PanelController {

    private static final String AJAX_HEADER_NAME = "X-Requested-With";
    private static final String AJAX_HEADER_VALUE = "XMLHttpRequest";

    private final ClassroomController classroomController;
    private final LessonController lessonController;
    private final TeacherController teacherController;
    private final PlanController planController;

    public PanelController(ClassroomController classroomController,
                           LessonController lessonController,
                           TeacherController teacherController,
                           PlanController planController) {
        this.classroomController = classroomController;
        this.lessonController = lessonController;
        this.teacherController = teacherController;
        this.planController = planController;
    }

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

    /******************** teacher *********************/
    @GetMapping("/teacher")
    public String getAllTeachers(Model model, HttpServletRequest request) {
        try {
            ResponseEntity<Result<List<TeacherVO>>> teacherVOList = teacherController.getAllTeachers();

            if (teacherVOList.getBody() != null && teacherVOList.getBody().getResult() != null && !teacherVOList.getBody().getResult().isEmpty())
                model.addAttribute("teachers", teacherVOList.getBody().getResult());
            else
                model.addAttribute("teachers", new ArrayList<TeacherVO>());
        } catch (Exception ex) {
            model.addAttribute("teachers", new ArrayList<TeacherVO>());
            model.addAttribute("errorMessage", ex.getMessage());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "teacher::#teacher-list";

        return "teacher";
    }

    @PostMapping("/teacher/add")
    public String addTeacher(Model model, HttpServletRequest request, TeacherVO teacherVO) {
        try {
            ResponseEntity<Result<TeacherVO>> teacher = teacherController.addTeacher(teacherVO.toString(), null);

            if (teacher.getBody() != null && teacher.getBody().getResult() != null)
                return "redirect:/teacher";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "redirect:/teacher";
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "teacher::#teacher-list";

        return "redirect:/teacher";
    }

    @PostMapping("/teacher/update")
    public String editTeacher(Model model, HttpServletRequest request, TeacherVO teacherVO) {
        try {
            ResponseEntity<Result<TeacherVO>> changedTeacher = teacherController.updateTeacher(teacherVO.getId(), teacherVO.toString(), null);

            if (changedTeacher.getBody() != null && changedTeacher.getBody().getResult() != null)
                return "redirect:/teacher";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "redirect:/teacher";
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "teacher::#teacher-list";

        return "redirect:/teacher";
    }

    @GetMapping("/teacher/one")
    @ResponseBody
    public Optional<TeacherVO> findOneTeacher(Model model, HttpServletRequest request, long id) {
        try {
            ResponseEntity<Result<TeacherVO>> teacher = teacherController.getTeacherById(id);

            if (teacher.getBody() != null && teacher.getBody().getResult() != null)
                return Optional.of(teacher.getBody().getResult());
        } catch (Exception ex) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    @GetMapping("/teacher/delete")
    public String deleteTeacher(long id) {
        teacherController.deleteTeacherById(id);
        return "redirect:/teacher";
    }
    /******************** end *********************/

    /******************** plans *********************/
    @GetMapping("/plan")
    public String getAllPlans(Model model, HttpServletRequest request) {
        try {
            ResponseEntity<Result<List<PlanVO>>> planVOList = planController.getAllPlans();

            if (planVOList.getBody() != null && planVOList.getBody().getResult() != null && !planVOList.getBody().getResult().isEmpty())
                model.addAttribute("plans", planVOList.getBody().getResult());
            else
                model.addAttribute("plans", new ArrayList<PlanVO>());
        } catch (Exception ex) {
            model.addAttribute("plans", new ArrayList<PlanVO>());
            model.addAttribute("errorMessage", ex.getMessage());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "plan::#plan-list";

        return "plan";
    }

    @PostMapping("/plan/add")
    public String addPlan(Model model, HttpServletRequest request, PlanVO planVO) {
        try {
            ResponseEntity<Result<PlanVO>> plan = planController.addPlan(planVO);

            if (plan.getBody() != null && plan.getBody().getResult() != null)
                return "redirect:/plan";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "redirect:/plan";
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "plan::#plan-list";

        return "redirect:/plan";
    }

    @PostMapping("/plan/update")
    public String editPlan(Model model, HttpServletRequest request, PlanVO planVO) {
        try {
            ResponseEntity<Result<PlanVO>> changedPlan = planController.updatePlan(planVO.getId(), planVO.getName());

            if (changedPlan.getBody() != null && changedPlan.getBody().getResult() != null)
                return "redirect:/plan";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "redirect:/plan";
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "plan::#plan-list";

        return "redirect:/plan";
    }

    @GetMapping("/plan/one")
    @ResponseBody
    public Optional<PlanVO> findOnePlan(Model model, HttpServletRequest request, long id) {
        try {
            ResponseEntity<Result<PlanVO>> plan = planController.getPlanById(id);

            if (plan.getBody() != null && plan.getBody().getResult() != null)
                return Optional.of(plan.getBody().getResult());
        } catch (Exception ex) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    @GetMapping("/plan/delete")
    public String deletePlan(long id) {
        planController.deletePlanById(id);
        return "redirect:/plan";
    }

    @PostMapping("/plan/copy")
    public String copyPlan(Model model, HttpServletRequest request, PlanVO planVO) {
        try {
            ResponseEntity<Result<PlanVO>> copiedPlan = planController.copyPlan(planVO.getId(), planVO.getName());

            if (copiedPlan.getBody() != null && copiedPlan.getBody().getResult() != null)
                return "redirect:/plan";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "redirect:/plan";
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "plan::#plan-list";

        return "redirect:/plan";
    }

    @GetMapping("/planning/{id}")
    public String getDetailsForOnePlan(Model model, HttpServletRequest request, @PathVariable("id") long id) {
        try {
            ResponseEntity<Result<PlanVO>> planVOList = planController.getPlanById(id);

            if (planVOList.getBody() != null && planVOList.getBody().getResult() != null)
                model.addAttribute("plans", planVOList.getBody().getResult());
            else
                model.addAttribute("plans", new ArrayList<PlanVO>());
        } catch (Exception ex) {
            model.addAttribute("plans", new ArrayList<PlanVO>());
            model.addAttribute("errorMessage", ex.getMessage());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "planning::#plan-list";

        return "planning";
    }

    @GetMapping("/reports/{id}")
    public String getReportsForOnePlan(Model model, HttpServletRequest request, @PathVariable("id") long id) {
        try {
            ResponseEntity<Result<List<PlanDetailGet>>> planVOList = planController.getPlanDetails(id);
            ResponseEntity<Result<List<ClassroomVO>>> classroomVOList = classroomController.getAllClassrooms();
            ResponseEntity<Result<List<TeacherVO>>> teacherVOList = teacherController.getAllTeachers();
            List<Long> classroomIds = new ArrayList<>();
            List<Long> teacherIds = new ArrayList<>();

            if (planVOList.getBody() != null && planVOList.getBody().getResult() != null)
                model.addAttribute("reports", planVOList.getBody().getResult());
            else
                model.addAttribute("reports", new ArrayList<PlanVO>());

            if (classroomVOList.getBody() != null && classroomVOList.getBody().getResult() != null) {
                model.addAttribute("classrooms", classroomVOList.getBody().getResult());
                for(ClassroomVO classroomVO : classroomVOList.getBody().getResult()) {
                    classroomIds.add(classroomVO.getId());
                }
                model.addAttribute("classroomIds", classroomIds);
            }
            else {
                model.addAttribute("classrooms", new ArrayList<ClassroomVO>());
                model.addAttribute("classroomIds", new ArrayList<Long>());
            }

            if (teacherVOList.getBody() != null && teacherVOList.getBody().getResult() != null) {
                model.addAttribute("teachers", teacherVOList.getBody().getResult());
                for(TeacherVO teacherVO : teacherVOList.getBody().getResult()) {
                    teacherIds.add(teacherVO.getId());
                }
                model.addAttribute("teacherIds", teacherIds);
            }
            else {
                model.addAttribute("teachers", new ArrayList<TeacherVO>());
                model.addAttribute("teacherIds", new ArrayList<Long>());
            }


        } catch (Exception ex) {
            model.addAttribute("reports", new ArrayList<PlanVO>());
            model.addAttribute("classrooms", new ArrayList<ClassroomVO>());
            model.addAttribute("teachers", new ArrayList<TeacherVO>());
            model.addAttribute("teacherIds", new ArrayList<Long>());
            model.addAttribute("classroomIds", new ArrayList<Long>());
            model.addAttribute("errorMessage", ex.getMessage());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "reports::#report-list";

        return "reports";
    }
    /******************** end *********************/
}
