package planning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import planning.model.Plan;

@Repository
public interface PlanCRUD extends JpaRepository<Plan, Long> {


}
