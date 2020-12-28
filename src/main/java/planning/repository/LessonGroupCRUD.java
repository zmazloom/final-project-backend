package planning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import planning.model.Lesson;
import planning.model.LessonGroup;
import planning.model.Plan;
import planning.model.Teacher;

import java.util.List;

@Repository
public interface LessonGroupCRUD extends JpaRepository<LessonGroup, Long> {

    @Query("from LessonGroup lg where lg.plan = :plan and lg.removed = false order by lg.lesson.name")
    List<LessonGroup> getAllLessonGroups(Plan plan);

    @Query("from LessonGroup lg where lg.id = :groupId and lg.removed = false")
    LessonGroup getLessonGroupById(long groupId);

    @Query("select distinct lg.lesson from LessonGroup lg where lg.plan = :plan and lg.removed = false")
    List<Lesson> getPlanLessons(Plan plan);

    @Query("select distinct lg.teacher from LessonGroup lg where lg.plan = :plan and lg.removed = false")
    List<Teacher> getPlanTeachers(Plan plan);

}
