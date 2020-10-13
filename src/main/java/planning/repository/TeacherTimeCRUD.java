package planning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import planning.model.Plan;
import planning.model.Teacher;
import planning.model.TeacherTime;
import java.util.List;

@Repository
public interface TeacherTimeCRUD extends JpaRepository<TeacherTime, Long>  {

    @Query("from TeacherTime tt where tt.plan = :plan and tt.teacher = :teacher")
    List<TeacherTime> getTeacherTimes(Plan plan, Teacher teacher);

    @Query("from TeacherTime tt where tt.plan = :plan")
    List<TeacherTime> getAllTeacherTimes(Plan plan);
}
