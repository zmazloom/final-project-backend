package planning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import planning.model.LessonGroup;
import planning.model.Plan;
import planning.model.PlanDetail;
import java.util.List;

@Repository
public interface PlanDetailCRUD extends JpaRepository<PlanDetail, Long> {

    @Query("from PlanDetail pd where pd.plan = :plan")
    List<PlanDetail> getPlanDetails(Plan plan);

    @Query("from PlanDetail pd where pd.plan = :plan and pd.group = :lessonGroup")
    List<PlanDetail> getPlanDetailByGroup(Plan plan, LessonGroup lessonGroup);
}
