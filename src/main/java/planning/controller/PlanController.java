package planning.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import planning.exception.*;
import planning.message.CommonMessage;
import planning.message.PlanMessage;
import planning.model.*;
import planning.modelVO.PlanDetailVO;
import planning.modelVO.PlanVO;
import planning.repository.PlanCRUD;
import planning.service.LoginService;
import planning.service.PlanService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/plan")
public class PlanController {

    private final PlanCRUD planCRUD;
    private final PlanService planService;
    private final LoginService loginService;

    @PostMapping(value = "")
    public ResponseEntity<Result<PlanVO>> addPlan(@RequestBody @Validated @NotNull PlanVO planVO) {
        Plan plan = planService.addPlan(planVO);

        return ResponseEntity.ok(ResFact.<PlanVO>build()
                .setResult(planService.getPlanVO(plan))
                .get());
    }

    @PutMapping(value = "/{planId}")
    public ResponseEntity<Result<PlanVO>> updatePlan(@PathVariable("planId") @NotNull Long planId,
                                                     @RequestBody @NotNull @NotEmpty String name,
                                                     @RequestBody int nimsal) {
        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        if (planCRUD.checkDuplicatePlanName(planId, name) != null)
            throw ResourceConflictException.getInstance(PlanMessage.getDuplicatePlan(name));

        return ResponseEntity.ok(ResFact.<PlanVO>build()
                .setResult(planService.getPlanVO(planService.updatePlan(plan, name, nimsal)))
                .get());
    }

    @GetMapping(value = "/{planId}")
    public ResponseEntity<Result<PlanVO>> getPlanById(@PathVariable("planId") @NotNull Long planId) {
        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        return ResponseEntity.ok(ResFact.<PlanVO>build()
                .setResult(planService.getPlanVO(plan))
                .get());
    }

    @DeleteMapping(value = "/{planId}")
    public ResponseEntity<Result<Boolean>> deletePlanById(@PathVariable("planId") @NotNull Long planId) {
        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        planService.deletePlan(plan);

        return ResponseEntity.ok(ResFact.<Boolean>build()
                .setResult(true)
                .setMessage(PlanMessage.getPlanDeleted(planId.toString()))
                .get());
    }

    @GetMapping(value = "")
    public ResponseEntity<Result<List<PlanVO>>> getAllPlans() {
        return ResponseEntity.ok(ResFact.<List<PlanVO>>build()
                .setResult(planService.getPlanVOs(planCRUD.getAllPlans()))
                .get());
    }

    @GetMapping("/count")
    public ResponseEntity<Result<Long>> getPlansCount() {
        return ResponseEntity.ok(ResFact.<Long>build()
                .setResult(planCRUD.getPlansCount())
                .get());
    }

    @PostMapping(value = "/{planId}/planning")
    public ResponseEntity<Result<PlanVO>> savePlanning(HttpServletRequest request,
                                                       @PathVariable("planId") @NotNull Long planId,
                                                       @RequestBody @Validated @NotNull List<PlanDetailVO> planDetailVOList) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            throw AccessDeniedException.getInstance(CommonMessage.getRequestDenied());

        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        planService.savePlanning(plan, planDetailVOList);

        return ResponseEntity.ok(ResFact.<PlanVO>build()
                .setResult(planService.getPlanVO(plan))
                .get());
    }

    @GetMapping(value = "/{planId}/detail")
    public ResponseEntity<Result<List<PlanDetailGet>>> getPlanDetails(@PathVariable("planId") @NotNull Long planId) {
        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        return ResponseEntity.ok(ResFact.<List<PlanDetailGet>>build()
                .setResult(planService.getPlanDetails(plan))
                .get());
    }

    @PostMapping(value = "/{planId}/copy")
    public ResponseEntity<Result<PlanVO>> copyPlan(@PathVariable("planId") @NotNull Long planId,
                                                   @RequestBody @NotNull @NotEmpty String name,
                                                   @RequestParam("nimsal") int nimsal) {
        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        if (planCRUD.checkDuplicatePlanName(planId, name) != null)
            throw ResourceConflictException.getInstance(PlanMessage.getDuplicatePlan(name));

        return ResponseEntity.ok(ResFact.<PlanVO>build()
                .setResult(planService.getPlanVO(planService.copyPlan(plan, name, nimsal)))
                .get());
    }

    @PostMapping(value = "/pouya")
    public ResponseEntity<Result<String>> addPouyaPlanning(HttpServletRequest request,
                                                           @RequestParam("name") String name,
                                                           @RequestParam("nimsal") int nimsal,
                                                           @RequestParam(value = "file") MultipartFile file) {
        if (!loginService.checkServiceAccess(request, Role.ROLE_ADMIN))
            throw AccessDeniedException.getInstance(CommonMessage.getRequestDenied());

        if (file == null)
            throw InvalidRequestException.getInstance(CommonMessage.getParamRequired("فایل"));
        if (name == null || name.equals(""))
            throw InvalidRequestException.getInstance(CommonMessage.getParamRequired("نام"));

        try {
            List<Course> courses = planService.pasreToCourses(file);
            Plan plan = planService.addPlan(PlanVO.builder().name(name).nimsal(nimsal).timeType(TimeType.TWO_HOURS).build());
            planService.convertCoursesToThisProject(plan, courses);

            return ResponseEntity.ok(ResFact.<String>build()
                    .setResult("برنامه با موفقیت اضافه شد.")
                    .get());
        } catch (Exception ex) {
            throw InternalServerException.getInstance(ex.getMessage());
        }
    }
}
