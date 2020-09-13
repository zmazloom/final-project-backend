package planning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import planning.model.Plan;
import java.util.List;

@Repository
public interface PlanCRUD extends JpaRepository<Plan, Long> {

    @Query("from Plan p where p.name = :planName")
    Plan getPlanByName(String planName);

    @Query("from Plan p where p.id = :planId and p.removed = false")
    Plan getPlanById(long planId);

    @Query("from Plan p where p.removed = false order by p.id")
    List<Plan> getAllPlans();

    @Query("from Plan p where p.id <> :planId and p.name = :name")
    Plan checkDuplicatePlanName(long planId, String name);

    @Query("select count(p) from Plan p where p.removed = false")
    Long getPlansCount();
}
