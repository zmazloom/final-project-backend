package planning.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import planning.model.Teacher;

@Repository
public interface TeacherCRUD extends JpaRepository<Teacher, Long> {

    @Query("from Teacher t where t.removed = false order by t.id")
    List<Teacher> getAllTeachers();

    @Query("from Teacher t where t.id = :teacherId and t.removed = false")
    Teacher getTeacherById(long teacherId);

    @Query("from Teacher t where t.username = :username and t.removed = false")
    Teacher getTeacherByUsername(String username);

    @Query("from Teacher t where t.id <> :teacherId and t.username = :username and t.removed = false")
    Teacher checkDuplicateTeacherUsername(long teacherId, String username);

    @Query("select count(t) from Teacher t where t.removed = false")
    Long getTeachersCount();
}