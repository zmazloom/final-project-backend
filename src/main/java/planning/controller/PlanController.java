package planning.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import planning.exception.ResourceConflictException;
import planning.exception.ResourceNotFoundException;
import planning.message.PlanMessage;
import planning.model.Plan;
import planning.model.PlanDetail;
import planning.model.ResFact;
import planning.model.Result;
import planning.modelVO.PlanDetailVO;
import planning.modelVO.PlanVO;
import planning.repository.PlanCRUD;
import planning.repository.PlanDetailCRUD;
import planning.service.PlanService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/plan")
public class PlanController {

    private final PlanCRUD planCRUD;
    private final PlanDetailCRUD planDetailCRUD;
    private final PlanService planService;

    @PostMapping(value = "")
    public ResponseEntity<Result<PlanVO>> addPlan(@RequestBody @Validated @NotNull PlanVO planVO) {
        if (planCRUD.getPlanByName(planVO.getName()) != null)
            throw ResourceConflictException.getInstance(PlanMessage.getDuplicatePlan(planVO.getName()));

        Plan plan = planService.addPlan(planVO);

        return ResponseEntity.ok(ResFact.<PlanVO>build()
                .setResult(planService.getPlanVO(plan))
                .get());
    }

    @PutMapping(value = "/{planId}")
    public ResponseEntity<Result<PlanVO>> updatePlan(@PathVariable("planId") @NotNull Long planId,
                                                     @RequestBody @NotNull @NotEmpty String name) {
        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        if (planCRUD.checkDuplicatePlanName(planId, name) != null)
            throw ResourceConflictException.getInstance(PlanMessage.getDuplicatePlan(name));

        return ResponseEntity.ok(ResFact.<PlanVO>build()
                .setResult(planService.getPlanVO(planService.updatePlan(plan, name)))
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

    @PostMapping(value = "/{planId}/copy")
    public ResponseEntity<Result<PlanVO>> copyPlan(@PathVariable("planId") @NotNull Long planId,
                                                   @RequestBody @NotNull @NotEmpty String name) {
        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        if (planCRUD.checkDuplicatePlanName(planId, name) != null)
            throw ResourceConflictException.getInstance(PlanMessage.getDuplicatePlan(name));

        return ResponseEntity.ok(ResFact.<PlanVO>build()
                .setResult(planService.getPlanVO(planService.copyPlan(plan, name)))
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

    @PostMapping(value = "/{planId}/planning")
    public ResponseEntity<Result<PlanVO>> savePlanning(@PathVariable("planId") @NotNull Long planId,
                                                       @RequestBody @Validated @NotNull List<PlanDetailVO> planDetailVOList) {
        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        planService.savePlanning(plan, planDetailVOList);

        return ResponseEntity.ok(ResFact.<PlanVO>build()
                .setResult(planService.getPlanVO(plan))
                .get());
    }

    @PostMapping(value = "/{planId}/check")
    public ResponseEntity<Result<HashMap<String, List<String>>>> checkPlanning(@PathVariable("planId") @NotNull Long planId,
                                                                               @RequestBody @Validated @NotNull List<PlanDetailVO> planDetailVOList) {
        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        return ResponseEntity.ok(ResFact.<HashMap<String, List<String>>>build()
                .setResult(planService.checkPlanning(plan, planDetailVOList))
                .get());
    }

    @GetMapping(value = "")
    public ResponseEntity<Result<List<PlanVO>>> getAllPlans() {
        return ResponseEntity.ok(ResFact.<List<PlanVO>>build()
                .setResult(planService.getPlanVOs(planCRUD.getAllPlans()))
                .get());
    }

    @GetMapping(value = "/{planId}/detail")
    public ResponseEntity<Result<List<PlanDetail>>> getPlanDetails(@PathVariable("planId") @NotNull Long planId) {
        Plan plan = planCRUD.getPlanById(planId);

        if (plan == null)
            throw ResourceNotFoundException.getInstance(PlanMessage.getPlanNotFound(planId.toString()));

        return ResponseEntity.ok(ResFact.<List<PlanDetail>>build()
                .setResult(planDetailCRUD.getPlanDetails(plan))
                .get());
    }
}
