package planning.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import planning.exception.AccessDeniedException;
import planning.exception.ResourceConflictException;
import planning.exception.ResourceNotFoundException;
import planning.message.CommonMessage;
import planning.message.PlanMessage;
import planning.message.TeacherMessage;
import planning.model.*;
import planning.modelVO.*;
import planning.repository.PlanCRUD;
import planning.repository.TeacherCRUD;
import planning.repository.TeacherTimeCRUD;
import planning.service.LoginService;
import planning.service.TeacherService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/teacher")
@AllArgsConstructor
public class TeacherController {

    private final TeacherCRUD teacherCRUD;
    private final TeacherTimeCRUD teacherTimeCRUD;
    private final PlanCRUD planCRUD;
    private final TeacherService teacherService;
    private final LoginService loginService;

    @PostMapping(value = "")
    public ResponseEntity<Result<TeacherVO>> addTeacher(HttpServletRequest request, @RequestBody @NotNull TeacherAddVO teacherAddVO) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            throw AccessDeniedException.getInstance(CommonMessage.getRequestDenied());

        if (teacherAddVO.getUsername() != null && teacherCRUD.getTeacherByUsername(teacherAddVO.getUsername()) != null)
            throw ResourceConflictException.getInstance(TeacherMessage.getDuplicateTeacher(teacherAddVO.getUsername()));

        Teacher teacher = teacherService.addTeacher(teacherAddVO, null);

        if (teacherAddVO.getPassword() == null || teacherAddVO.getPassword().equals("")) {
            return ResponseEntity.ok(ResFact.<TeacherVO>build()
                    .setMessage(TeacherMessage.getTeacherPassword(teacher.getPassword()))
                    .setResult(teacherService.getTeacherVO(teacher))
                    .get());
        }
        return ResponseEntity.ok(ResFact.<TeacherVO>build()
                .setResult(teacherService.getTeacherVO(teacher))
                .get());
    }

    @PutMapping(value = "/{teacherId}")
    public ResponseEntity<Result<TeacherVO>> updateTeacher(HttpServletRequest request,
                                                           @PathVariable("teacherId") @NotNull Long teacherId,
                                                           @RequestBody @NotNull TeacherAddVO teacherAddVO) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            throw AccessDeniedException.getInstance(CommonMessage.getRequestDenied());

        Teacher teacher = teacherCRUD.getTeacherById(teacherId);

        if (teacher == null)
            throw ResourceNotFoundException.getInstance(TeacherMessage.getTeacherNotFound(teacherId.toString()));

        if (teacherAddVO.getUsername() != null && teacherCRUD.checkDuplicateTeacherUsername(teacherId, teacherAddVO.getUsername()) != null)
            throw ResourceConflictException.getInstance(TeacherMessage.getDuplicateTeacher(teacherAddVO.getUsername()));

        Teacher changedTeacher = teacherService.updateTeacher(teacher, teacherAddVO, null);

        return ResponseEntity.ok(ResFact.<TeacherVO>build()
                .setResult(teacherService.getTeacherVO(changedTeacher))
                .get());
    }

    @GetMapping(value = "")
    public ResponseEntity<Result<List<TeacherVO>>> getAllTeachers() {
        return ResponseEntity.ok(ResFact.<List<TeacherVO>>build()
                .setResult(teacherService.getTeacherVOs(teacherCRUD.getAllTeachers()))
                .get());
    }

    @GetMapping(value = "/{teacherId}")
    public ResponseEntity<Result<TeacherVO>> getTeacherById(@PathVariable("teacherId") @NotNull Long teacherId) {
        Teacher teacher = teacherCRUD.getTeacherById(teacherId);

        if (teacher == null)
            throw ResourceNotFoundException.getInstance(TeacherMessage.getTeacherNotFound(teacherId.toString()));

        return ResponseEntity.ok(ResFact.<TeacherVO>build()
                .setResult(teacherService.getTeacherVO(teacher))
                .get());
    }

    @DeleteMapping(value = "/{teacherId}")
    public ResponseEntity<Result<Boolean>> deleteTeacherById(@PathVariable("teacherId") @NotNull Long teacherId) {
        Teacher teacher = teacherCRUD.getTeacherById(teacherId);

        if (teacher == null)
            throw ResourceNotFoundException.getInstance(TeacherMessage.getTeacherNotFound(teacherId.toString()));

        teacherService.deleteTeacher(teacher);

        return ResponseEntity.ok(ResFact.<Boolean>build()
                .setResult(true)
                .setMessage(TeacherMessage.getTeacherDeleted(teacherId.toString()))
                .get());
    }

    @PostMapping("/{teacherId}/time")
    public ResponseEntity<Result<Boolean>> addTeacherTimes(HttpServletRequest request,
                                                           @PathVariable("teacherId") @NotNull Long teacherId,
                                                           @RequestBody @NotNull @Validated TeacherTimeVO teacherTimeVO) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_USER))
            throw AccessDeniedException.getInstance(CommonMessage.getRequestDenied());

        Teacher teacher = teacherCRUD.getTeacherById(teacherId);

        if (teacher == null)
            throw ResourceNotFoundException.getInstance(TeacherMessage.getTeacherNotFound(teacherId.toString()));

        Plan plan = planCRUD.getPlanById(teacherTimeVO.getPlanId());

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(teacherTimeVO.getPlanId().toString()));

        teacherService.addTeacherTimes(teacher, plan, teacherTimeVO.getTimes());

        return ResponseEntity.ok(ResFact.<Boolean>build()
                .setResult(true)
                .setMessage(TeacherMessage.getAddTeacherTime())
                .get());
    }

    @GetMapping("/{teacherId}/time")
    public ResponseEntity<Result<TeacherTimeVO>> getTeacherTimes(@PathVariable("teacherId") @NotNull Long teacherId,
                                                                 @RequestParam(value = "planId") @NotNull Long planId) {
        Teacher teacher = teacherCRUD.getTeacherById(teacherId);

        if (teacher == null)
            throw ResourceNotFoundException.getInstance(TeacherMessage.getTeacherNotFound(teacherId.toString()));

        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        return ResponseEntity.ok(ResFact.<TeacherTimeVO>build()
                .setResult(teacherService.getTeacherTimeVO(plan, teacherTimeCRUD.getTeacherTimes(plan, teacher)))
                .get());
    }

    @GetMapping("/time/all")
    public ResponseEntity<Result<List<AllTeacherTimeGet>>> getAllTeacherTimes(@RequestParam(value = "planId") @NotNull Long planId) {
        List<AllTeacherTimeGet> allTeacherTimeGets = new ArrayList<>();

        List<Teacher> teachers = teacherCRUD.getAllTeachers();

        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        if (teachers != null) {
            for (Teacher teacher : teachers) {
                TeacherTimeVO teacherTimeVO = teacherService.getTeacherTimeVO(plan, teacherTimeCRUD.getTeacherTimes(plan, teacher));

                if (teacherTimeVO != null) {
                    for (TimePriorityVO time : teacherTimeVO.getTimes()) {
                        AllTeacherTimeGet allTeacherTimeGet = new AllTeacherTimeGet();
                        allTeacherTimeGet.setFirstName(teacher.getFirstName());
                        allTeacherTimeGet.setLastName(teacher.getLastName());
                        allTeacherTimeGet.setTeacherId(teacher.getId());
                        allTeacherTimeGet.setTime(time.getTime());
                        allTeacherTimeGet.setPriority(time.getPriority());
                        allTeacherTimeGets.add(allTeacherTimeGet);
                    }
                }
            }
        }

        return ResponseEntity.ok(ResFact.<List<AllTeacherTimeGet>>build()
                .setResult(allTeacherTimeGets)
                .get());
    }

    @GetMapping("/time/all/plan/{teacherId}")
    public ResponseEntity<Result<List<TeacherAllTimeGetVO>>> getTeacherTimeForAllPlans(@PathVariable("teacherId") @NotNull Long teacherId) {
        List<TeacherAllTimeGetVO> teacherAllTimeGetVOS = new ArrayList<>();

        Teacher teacher = teacherCRUD.getTeacherById(teacherId);
        if (teacher == null)
            throw ResourceNotFoundException.getInstance(TeacherMessage.getTeacherNotFound(teacherId.toString()));

        List<Plan> plans = planCRUD.getAllPlans();

        if (plans != null && !plans.isEmpty()) {
            for (Plan plan : plans) {
                TeacherTimeVO teacherTimeVO = teacherService.getTeacherTimeVO(plan, teacherTimeCRUD.getTeacherTimes(plan, teacher));

                if (teacherTimeVO != null) {
                    for (TimePriorityVO time : teacherTimeVO.getTimes()) {
                        TeacherAllTimeGetVO teacherAllTimeGetVO = new TeacherAllTimeGetVO();
                        teacherAllTimeGetVO.setTime(time.getTime());
                        teacherAllTimeGetVO.setPriority(time.getPriority());
                        teacherAllTimeGetVO.setPlanId(plan.getId());
                        teacherAllTimeGetVOS.add(teacherAllTimeGetVO);
                    }
                }
            }
        }

        return ResponseEntity.ok(ResFact.<List<TeacherAllTimeGetVO>>build()
                .setResult(teacherAllTimeGetVOS)
                .get());
    }

    @GetMapping("/count")
    public ResponseEntity<Result<Long>> getTeachersCount() {
        return ResponseEntity.ok(ResFact.<Long>build()
                .setResult(teacherCRUD.getTeachersCount())
                .get());
    }
}