package planning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import planning.model.LessonGroup;
import planning.model.Plan;

import java.util.List;

@Repository
public interface LessonGroupCRUD extends JpaRepository<LessonGroup, Long> {

    @Query("from LessonGroup lg where lg.plan = :plan")
    List<LessonGroup> getAllLessonGroups(Plan plan);

    @Query("from LessonGroup lg where lg.id = :groupId and lg.removed = false")
    LessonGroup getLessonGroupById(long groupId);

}
