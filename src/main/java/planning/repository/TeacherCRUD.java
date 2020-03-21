package planning.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import planning.model.Teacher;

@Repository
public interface TeacherCRUD extends JpaRepository<Teacher, Long> {

    @Query("select t from Teacher t where t.removed = false order by t.lastName")
    public List<Teacher> getAllTeachers();

    @Query("select t from Teacher t where t.id = :teacherId and t.removed = false")
    public Teacher getTeacherById(long teacherId);

}