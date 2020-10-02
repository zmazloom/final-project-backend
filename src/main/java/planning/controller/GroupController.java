package planning.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import planning.exception.AccessDeniedException;
import planning.exception.ResourceNotFoundException;
import planning.message.CommonMessage;
import planning.message.PlanMessage;
import planning.model.*;
import planning.modelVO.LessonGroupVO;
import planning.repository.LessonGroupCRUD;
import planning.repository.PlanCRUD;
import planning.service.GroupService;
import planning.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/group")
public class GroupController {

    private final LessonGroupCRUD lessonGroupCRUD;
    private final PlanCRUD planCRUD;
    private final GroupService groupService;
    private final LoginService loginService;

    @PostMapping(value = "/plan/{planId}")
    public ResponseEntity<Result<LessonGroupVO>> savePlanGroup(HttpServletRequest request,
                                                               @PathVariable("planId") @NotNull Long planId,
                                                               @RequestBody @Validated @NotNull LessonGroupVO lessonGroupVO) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            throw AccessDeniedException.getInstance(CommonMessage.getRequestDenied());

        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        return ResponseEntity.ok(ResFact.<LessonGroupVO>build()
                .setResult(groupService.getLessonGroupVO(groupService.addLessonGroup(plan, lessonGroupVO)))
                .get());
    }

    @PutMapping(value = "/{groupId}")
    public ResponseEntity<Result<LessonGroupVO>> updatePlanGroup(HttpServletRequest request,
                                                                 @PathVariable("groupId") @NotNull Long groupId,
                                                                 @RequestBody @Validated @NotNull LessonGroupVO lessonGroupVO) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            throw AccessDeniedException.getInstance(CommonMessage.getRequestDenied());

        LessonGroup lessonGroup = lessonGroupCRUD.getLessonGroupById(groupId);

        if (lessonGroup == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getGroupNotFound(groupId.toString()));

        return ResponseEntity.ok(ResFact.<LessonGroupVO>build()
                .setResult(groupService.getLessonGroupVO(groupService.updateLessonGroup(lessonGroup, lessonGroupVO)))
                .get());
    }

    @GetMapping(value = "/plan/{planId}")
    public ResponseEntity<Result<List<LessonGroupVO>>> getAllLessonGroups(@PathVariable("planId") @NotNull Long planId) {
        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        return ResponseEntity.ok(ResFact.<List<LessonGroupVO>>build()
                .setResult(groupService.getLessonGroupVOs(lessonGroupCRUD.getAllLessonGroups(plan)))
                .get());
    }

    @GetMapping(value = "/{groupId}")
    public ResponseEntity<Result<LessonGroupVO>> getLessonGroupById(@PathVariable("groupId") @NotNull Long groupId) {
        LessonGroup lessonGroup = lessonGroupCRUD.getLessonGroupById(groupId);

        if (lessonGroup == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getGroupNotFound(groupId.toString()));

        return ResponseEntity.ok(ResFact.<LessonGroupVO>build()
                .setResult(groupService.getLessonGroupVO(lessonGroup))
                .get());
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Result<Boolean>> deleteLessonGroup(@PathVariable("groupId") @NotNull Long groupId) {
        LessonGroup lessonGroup = lessonGroupCRUD.getLessonGroupById(groupId);

        if (lessonGroup == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getGroupNotFound(groupId.toString()));

        groupService.deleteLessonGroup(lessonGroup);

        return ResponseEntity.ok(ResFact.<Boolean>build()
                .setResult(true)
                .setMessage(PlanMessage.getGroupDeleted(groupId.toString()))
                .get());
    }
}
