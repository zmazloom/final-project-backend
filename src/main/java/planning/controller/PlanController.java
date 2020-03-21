package planning.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import planning.model.Plan;
import planning.repository.PlanCRUD;
import planning.service.PlanService;
import javax.validation.constraints.NotNull;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(value = "/plan")
public class PlanController {

    private final PlanCRUD planCRUD;
    private final PlanService planService;

//    @PostMapping(value = "")
//    public boolean addPlan(@RequestBody @NotNull Plan plan) {
//        Plan newPlan = planCRUD.saveAndFlush(plan);
//
//        return true;
//    }
}
