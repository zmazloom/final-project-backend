package planning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import planning.model.Classroom;

import java.util.List;

@Repository
public interface ClassroomCRUD extends JpaRepository<Classroom, Long> {

    @Query("from Classroom c where c.name = :className")
    Classroom getClassroomByName(String className);

    @Query("from Classroom c where c.removed = false order by c.id")
    List<Classroom> getAllClassrooms();

    @Query("select count(c) from Classroom c where c.removed = false")
    Long getClassroomsCount();

    @Query("from Classroom c where c.id = :classId and c.removed = false")
    Classroom getClassroomById(long classId);

    @Query("from Classroom c where c.id <> :classId and c.name = :className and c.removed = false")
    Classroom checkDuplicateClassName(long classId, String className);

}
