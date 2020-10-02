package planning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import planning.model.*;
import planning.modelVO.*;
import planning.service.LoginService;
import planning.service.TeacherService;

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
    private final GroupController groupController;
    private final LoginService loginService;
    private final TeacherService teacherService;

    public PanelController(ClassroomController classroomController,
                           LessonController lessonController,
                           TeacherController teacherController,
                           PlanController planController,
                           GroupController groupController,
                           LoginService loginService,
                           TeacherService teacherService) {
        this.classroomController = classroomController;
        this.lessonController = lessonController;
        this.teacherController = teacherController;
        this.planController = planController;
        this.groupController = groupController;
        this.loginService = loginService;
        this.teacherService = teacherService;
    }

    @RequestMapping(value = {"/", "/dashboard"})
    public String getIndexPage(Model model, HttpServletRequest request) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        try {
            ResponseEntity<Result<Long>> classroomsCount = classroomController.getClassroomsCount();
            ResponseEntity<Result<Long>> teachersCount = teacherController.getTeachersCount();
            ResponseEntity<Result<Long>> lessonsCount = lessonController.getLessonsCount();
            ResponseEntity<Result<Long>> plansCount = planController.getPlansCount();
            Teacher user = teacherService.getTeacherByRequest(request);

            if (classroomsCount.getBody() != null && classroomsCount.getBody().getResult() != null)
                model.addAttribute("classrooms", classroomsCount.getBody().getResult());
            else
                model.addAttribute("classrooms", 0);

            if (teachersCount.getBody() != null && teachersCount.getBody().getResult() != null)
                model.addAttribute("teachers", teachersCount.getBody().getResult());
            else
                model.addAttribute("teachers", 0);
            if (lessonsCount.getBody() != null && lessonsCount.getBody().getResult() != null)
                model.addAttribute("lessons", lessonsCount.getBody().getResult());
            else
                model.addAttribute("lessons", 0);
            if (plansCount.getBody() != null && plansCount.getBody().getResult() != null)
                model.addAttribute("plans", plansCount.getBody().getResult());
            else
                model.addAttribute("plans", 0);
            if (user != null)
                model.addAttribute("user", user);
            else
                model.addAttribute("user", new TeacherVO());
        } catch (Exception ex) {
            model.addAttribute("classrooms", 0);
            model.addAttribute("teachers", 0);
            model.addAttribute("lessons", 0);
            model.addAttribute("plans", 0);
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("user", new TeacherVO());
        }

        return "index";
    }


    /******************** classroom *********************/
    @GetMapping("/classroom")
    public String getAllClassrooms(Model model, HttpServletRequest request) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        try {
            Teacher user = teacherService.getTeacherByRequest(request);

            ResponseEntity<Result<List<ClassroomVO>>> classroomVOList = classroomController.getAllClassrooms();

            if (classroomVOList.getBody() != null && classroomVOList.getBody().getResult() != null && !classroomVOList.getBody().getResult().isEmpty())
                model.addAttribute("classrooms", classroomVOList.getBody().getResult());
            else
                model.addAttribute("classrooms", new ArrayList<ClassroomVO>());
            if (user != null)
                model.addAttribute("user", user);
            else
                model.addAttribute("user", new TeacherVO());
        } catch (Exception ex) {
            model.addAttribute("classrooms", new ArrayList<ClassroomVO>());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("user", new TeacherVO());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "classroom::#classroom-list";

        return "classroom";
    }

    @PostMapping("/classroom/add")
    public String addClassroom(Model model, HttpServletRequest request, ClassroomVO classroomVO) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

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
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

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
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return null;

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
    public String deleteClassroom(HttpServletRequest request, long id) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        classroomController.deleteClassroom(id);
        return "redirect:/classroom";
    }
    /******************** end *********************/

    /******************** lesson *********************/
    @GetMapping("/lesson")
    public String getAllLessons(Model model, HttpServletRequest request) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        try {
            Teacher user = teacherService.getTeacherByRequest(request);
            ResponseEntity<Result<List<LessonVO>>> lessonVOList = lessonController.getAllLessons();

            if (lessonVOList.getBody() != null && lessonVOList.getBody().getResult() != null && !lessonVOList.getBody().getResult().isEmpty())
                model.addAttribute("lessons", lessonVOList.getBody().getResult());
            else
                model.addAttribute("lessons", new ArrayList<LessonVO>());
            if (user != null)
                model.addAttribute("user", user);
            else
                model.addAttribute("user", new TeacherVO());
        } catch (Exception ex) {
            model.addAttribute("lessons", new ArrayList<LessonVO>());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("user", new TeacherVO());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "lesson::#lesson-list";

        return "lesson";
    }

    @GetMapping("/lesson/one")
    @ResponseBody
    public Optional<LessonVO> findOneLesson(Model model, HttpServletRequest request, long id) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return null;

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
    public String deleteLesson(HttpServletRequest request, long id) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        lessonController.deleteLesson(id);
        return "redirect:/lesson";
    }
    /******************** end *********************/

    /******************** teacher *********************/
    @GetMapping("/teacher")
    public String getAllTeachers(Model model, HttpServletRequest request) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        try {
            Teacher user = teacherService.getTeacherByRequest(request);
            ResponseEntity<Result<List<TeacherVO>>> teacherVOList = teacherController.getAllTeachers();

            if (teacherVOList.getBody() != null && teacherVOList.getBody().getResult() != null && !teacherVOList.getBody().getResult().isEmpty())
                model.addAttribute("teachers", teacherVOList.getBody().getResult());
            else
                model.addAttribute("teachers", new ArrayList<TeacherVO>());
            if (user != null)
                model.addAttribute("user", user);
            else
                model.addAttribute("user", new TeacherVO());
        } catch (Exception ex) {
            model.addAttribute("teachers", new ArrayList<TeacherVO>());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("user", new TeacherVO());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "teacher::#teacher-list";

        return "teacher";
    }

    @GetMapping("/teacher/one")
    @ResponseBody
    public Optional<TeacherVO> findOneTeacher(Model model, HttpServletRequest request, long id) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return null;

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
    public String deleteTeacher(HttpServletRequest request, long id) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        teacherController.deleteTeacherById(id);
        return "redirect:/teacher";
    }
    /******************** end *********************/

    /******************** plans *********************/
    @GetMapping("/plan")
    public String getAllPlans(Model model, HttpServletRequest request) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        try {
            Teacher user = teacherService.getTeacherByRequest(request);
            ResponseEntity<Result<List<PlanVO>>> planVOList = planController.getAllPlans();

            if (planVOList.getBody() != null && planVOList.getBody().getResult() != null && !planVOList.getBody().getResult().isEmpty())
                model.addAttribute("plans", planVOList.getBody().getResult());
            else
                model.addAttribute("plans", new ArrayList<PlanVO>());
            if (user != null)
                model.addAttribute("user", user);
            else
                model.addAttribute("user", new TeacherVO());
        } catch (Exception ex) {
            model.addAttribute("plans", new ArrayList<PlanVO>());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("user", new TeacherVO());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "plan::#plan-list";

        return "plan";
    }

    @PostMapping("/plan/add")
    public String addPlan(Model model, HttpServletRequest request, PlanVO planVO) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

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
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        try {
            ResponseEntity<Result<PlanVO>> changedPlan = planController.updatePlan(planVO.getId(), planVO.getName(), planVO.getNimsal());

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
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return null;

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
    public String deletePlan(HttpServletRequest request, long id) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        planController.deletePlanById(id);
        return "redirect:/plan";
    }

    @PostMapping("/plan/copy")
    public String copyPlan(Model model, HttpServletRequest request, PlanVO planVO) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

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
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        model.addAttribute("planId", id);

        ResponseEntity<Result<PlanVO>> plan = null;
        try {
            Teacher user = teacherService.getTeacherByRequest(request);
            plan = planController.getPlanById(id);
            ResponseEntity<Result<List<PlanDetailGet>>> planVOList = planController.getPlanDetails(id);
            ResponseEntity<Result<List<ClassroomVO>>> classroomVOList = classroomController.getAllClassrooms();
            ResponseEntity<Result<List<TeacherVO>>> teacherVOList = teacherController.getAllTeachers();
            ResponseEntity<Result<List<LessonVO>>> lessonVOList = lessonController.getAllLessons();
            ResponseEntity<Result<List<AllTeacherTimeGet>>> teacherTimeVOList = teacherController.getAllTeacherTimes(id);

            model.addAttribute("planId", id);

            List<Long> classroomIds = new ArrayList<>();
            List<Long> teacherIds = new ArrayList<>();

            if (planVOList.getBody() != null && planVOList.getBody().getResult() != null)
                model.addAttribute("reports", planVOList.getBody().getResult());
            else
                model.addAttribute("reports", new ArrayList<PlanVO>());

            if (lessonVOList.getBody() != null && lessonVOList.getBody().getResult() != null)
                model.addAttribute("lessons", lessonVOList.getBody().getResult());
            else
                model.addAttribute("lessons", new ArrayList<LessonVO>());

            if (classroomVOList.getBody() != null && classroomVOList.getBody().getResult() != null) {
                model.addAttribute("classrooms", classroomVOList.getBody().getResult());
                for (ClassroomVO classroomVO : classroomVOList.getBody().getResult()) {
                    classroomIds.add(classroomVO.getId());
                }
                model.addAttribute("classroomIds", classroomIds);
            } else {
                model.addAttribute("classrooms", new ArrayList<ClassroomVO>());
                model.addAttribute("classroomIds", new ArrayList<Long>());
            }

            if (teacherVOList.getBody() != null && teacherVOList.getBody().getResult() != null) {
                model.addAttribute("teachers", teacherVOList.getBody().getResult());
                for (TeacherVO teacherVO : teacherVOList.getBody().getResult()) {
                    teacherIds.add(teacherVO.getId());
                }
                model.addAttribute("teacherIds", teacherIds);
            } else {
                model.addAttribute("teachers", new ArrayList<TeacherVO>());
                model.addAttribute("teacherIds", new ArrayList<Long>());
            }

            if (teacherTimeVOList.getBody() != null && teacherTimeVOList.getBody().getResult() != null)
                model.addAttribute("teachertimes", teacherTimeVOList.getBody().getResult());
            else
                model.addAttribute("teachertimes", new ArrayList<AllTeacherTimeGet>());

            if (user != null)
                model.addAttribute("user", user);
            else
                model.addAttribute("user", new TeacherVO());
        } catch (Exception ex) {
            model.addAttribute("reports", new ArrayList<PlanVO>());
            model.addAttribute("classrooms", new ArrayList<ClassroomVO>());
            model.addAttribute("teachers", new ArrayList<TeacherVO>());
            model.addAttribute("lessons", new ArrayList<LessonVO>());
            model.addAttribute("teacherIds", new ArrayList<Long>());
            model.addAttribute("classroomIds", new ArrayList<Long>());
            model.addAttribute("teachertimes", new ArrayList<AllTeacherTimeGet>());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("user", new TeacherVO());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "planning::#planning-list";

        if (plan != null && plan.getBody() != null && plan.getBody().getResult() != null && plan.getBody().getResult().getTimeType().equals(TimeType.ONE_THIRTY_HOURS))
            return "planningOtherTime";

        return "planning";
    }

    @GetMapping("/reports/{id}")
    public String getReportsForOnePlan(Model model, HttpServletRequest request, @PathVariable("id") long id) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        model.addAttribute("planId", id);

        ResponseEntity<Result<PlanVO>> plan = null;
        try {
            Teacher user = teacherService.getTeacherByRequest(request);
            plan = planController.getPlanById(id);
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
                for (ClassroomVO classroomVO : classroomVOList.getBody().getResult()) {
                    classroomIds.add(classroomVO.getId());
                }
                model.addAttribute("classroomIds", classroomIds);
            } else {
                model.addAttribute("classrooms", new ArrayList<ClassroomVO>());
                model.addAttribute("classroomIds", new ArrayList<Long>());
            }

            if (teacherVOList.getBody() != null && teacherVOList.getBody().getResult() != null) {
                model.addAttribute("teachers", teacherVOList.getBody().getResult());
                for (TeacherVO teacherVO : teacherVOList.getBody().getResult()) {
                    teacherIds.add(teacherVO.getId());
                }
                model.addAttribute("teacherIds", teacherIds);
            } else {
                model.addAttribute("teachers", new ArrayList<TeacherVO>());
                model.addAttribute("teacherIds", new ArrayList<Long>());
            }

            if (user != null)
                model.addAttribute("user", user);
            else
                model.addAttribute("user", new TeacherVO());

        } catch (Exception ex) {
            model.addAttribute("reports", new ArrayList<PlanVO>());
            model.addAttribute("classrooms", new ArrayList<ClassroomVO>());
            model.addAttribute("teachers", new ArrayList<TeacherVO>());
            model.addAttribute("teacherIds", new ArrayList<Long>());
            model.addAttribute("classroomIds", new ArrayList<Long>());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("user", new TeacherVO());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "reports::#report-list";

        if (plan != null && plan.getBody() != null && plan.getBody().getResult() != null && plan.getBody().getResult().getTimeType().equals(TimeType.ONE_THIRTY_HOURS))
            return "reportsOtherTime";

        return "reports";
    }

    @GetMapping("/teachertime/{id}")
    public String getTeacherTime(Model model, HttpServletRequest request, @PathVariable("id") long id) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        model.addAttribute("planId", id);

        ResponseEntity<Result<PlanVO>> plan = null;
        try {
            Teacher user = teacherService.getTeacherByRequest(request);
            plan = planController.getPlanById(id);
            ResponseEntity<Result<List<TeacherVO>>> teacherVOList = teacherController.getAllTeachers();
            ResponseEntity<Result<List<AllTeacherTimeGet>>> teacherTimeVOList = teacherController.getAllTeacherTimes(id);

            if (teacherTimeVOList.getBody() != null && teacherTimeVOList.getBody().getResult() != null)
                model.addAttribute("teachertimes", teacherTimeVOList.getBody().getResult());
            else
                model.addAttribute("teachertimes", new ArrayList<AllTeacherTimeGet>());

            if (teacherVOList.getBody() != null && teacherVOList.getBody().getResult() != null)
                model.addAttribute("teachers", teacherVOList.getBody().getResult());
            else
                model.addAttribute("teachers", new ArrayList<TeacherVO>());
            if (user != null)
                model.addAttribute("user", user);
            else
                model.addAttribute("user", new TeacherVO());

        } catch (Exception ex) {
            model.addAttribute("planId", id);
            model.addAttribute("teachertimes", new ArrayList<Time>());
            model.addAttribute("teachers", new ArrayList<TeacherVO>());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("user", new TeacherVO());
        }

        if (plan != null && plan.getBody() != null && plan.getBody().getResult() != null && plan.getBody().getResult().getTimeType().equals(TimeType.ONE_THIRTY_HOURS))
            return "teachertimeOtherTime";

        return "teachertime";
    }

    @GetMapping("/plandashboard/{id}")
    public String getPlanDashboard(Model model, HttpServletRequest request, @PathVariable("id") long id) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        model.addAttribute("planId", id);

        Teacher user = teacherService.getTeacherByRequest(request);

        if (user != null)
            model.addAttribute("user", user);
        else
            model.addAttribute("user", new TeacherVO());

        return "plandashboard";
    }
    /******************** end *********************/

    /******************* login ********************/
    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "login";
    }
    /******************** end *********************/

    /******************** group *********************/
    @GetMapping("/group/{id}")
    public String getAllGroups(Model model, HttpServletRequest request, @PathVariable("id") long id) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        model.addAttribute("planId", id);

        try {
            Teacher user = teacherService.getTeacherByRequest(request);
            ResponseEntity<Result<List<LessonGroupVO>>> lessonGroupVOList = groupController.getAllLessonGroups(id);
            ResponseEntity<Result<List<TeacherVO>>> teacherVOList = teacherController.getAllTeachers();
            ResponseEntity<Result<List<LessonVO>>> lessonVOList = lessonController.getAllLessons();

            if (lessonGroupVOList.getBody() != null && lessonGroupVOList.getBody().getResult() != null && !lessonGroupVOList.getBody().getResult().isEmpty())
                model.addAttribute("groups", lessonGroupVOList.getBody().getResult());
            else
                model.addAttribute("groups", new ArrayList<LessonGroupVO>());

            if (teacherVOList.getBody() != null && teacherVOList.getBody().getResult() != null)
                model.addAttribute("teachers", teacherVOList.getBody().getResult());
            else
                model.addAttribute("teachers", new ArrayList<TeacherVO>());

            if (lessonVOList.getBody() != null && lessonVOList.getBody().getResult() != null)
                model.addAttribute("lessons", lessonVOList.getBody().getResult());
            else
                model.addAttribute("lessons", new ArrayList<LessonVO>());

            if (user != null)
                model.addAttribute("user", user);
            else
                model.addAttribute("user", new TeacherVO());

        } catch (Exception ex) {
            model.addAttribute("groups", new ArrayList<LessonGroupVO>());
            model.addAttribute("teachers", new ArrayList<TeacherVO>());
            model.addAttribute("lessons", new ArrayList<LessonVO>());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("user", new TeacherVO());
        }

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME)))
            return "groups::#group-list";

        return "group";
    }

    @GetMapping("/group/one")
    @ResponseBody
    public Optional<LessonGroupVO> findOneLessonGroup(Model model, HttpServletRequest request, long id) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return null;

        try {
            ResponseEntity<Result<LessonGroupVO>> lessonGroup = groupController.getLessonGroupById(id);

            if (lessonGroup.getBody() != null && lessonGroup.getBody().getResult() != null)
                return Optional.of(lessonGroup.getBody().getResult());
        } catch (Exception ex) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    @GetMapping("/group/{id}/delete/{planId}")
    public String deleteLessonGroup(HttpServletRequest request, @PathVariable("id") long id, @PathVariable("planId") long planId) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            return "redirect:/login";

        groupController.deleteLessonGroup(id);
        return "redirect:/group/" + planId;
    }
    /******************** end *********************/

    /******************** profile *********************/
    @GetMapping("/profile")
    public String getUserProfile(Model model, HttpServletRequest request) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_USER))
            return "redirect:/login";

        Teacher user = teacherService.getTeacherByRequest(request);

        if (user == null)
            return "redirect:/login";

        model.addAttribute("user", user);

        if(user.getRole().equals(Role.ROLE_ADMIN))
            model.addAttribute("role", "admin");
        else model.addAttribute("role", "user");

        return "profile";
    }
    /******************** end *********************/

    /******************* logout ********************/
    @GetMapping("/exit")
    public String logout(HttpServletRequest request) {
        Teacher user = teacherService.getTeacherByRequest(request);

        if (user != null)
            loginService.deleteAllTeacherTokens(user);

        return "redirect:/login";
    }
    /******************** end *********************/

    /******************** teacher pages *********************/
    @GetMapping("/teacher_page")
    public String getTeacherPage(Model model, HttpServletRequest request) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_USER))
            return "redirect:/login";

        try {
            Teacher user = teacherService.getTeacherByRequest(request);
            if (user != null) {
                ResponseEntity<Result<List<PlanVO>>> plans = planController.getAllPlans();
                ResponseEntity<Result<List<TeacherAllTimeGetVO>>> teacherTimeVOList = teacherController.getTeacherTimeForAllPlans(user.getId());

                if (plans.getBody() != null && plans.getBody().getResult() != null)
                    model.addAttribute("plans", plans.getBody().getResult());
                else
                    model.addAttribute("plans", new ArrayList<PlanVO>());

                if (teacherTimeVOList.getBody() != null && teacherTimeVOList.getBody().getResult() != null)
                    model.addAttribute("teachertimes", teacherTimeVOList.getBody().getResult());
                else
                    model.addAttribute("teachertimes", new ArrayList<TeacherAllTimeGetVO>());
                if (user != null)
                    model.addAttribute("user", user);
                else
                    model.addAttribute("user", new TeacherVO());
            }

        } catch (Exception ex) {
            model.addAttribute("plans", new ArrayList<PlanVO>());
            model.addAttribute("teachertimes", new ArrayList<TeacherAllTimeGetVO>());
            model.addAttribute("user", new TeacherVO());
            model.addAttribute("errorMessage", ex.getMessage());
        }

        return "teacher_page";
    }
    /******************** end *********************/
}
