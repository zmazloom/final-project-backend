package planning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import planning.model.Lesson;
import java.util.List;

@Repository
public interface LessonCRUD extends JpaRepository<Lesson, Long> {

    @Query("from Lesson l where l.removed = false order by name")
    public List<Lesson> getAllLessons();

    @Query("from Lesson l where l.id = :lessonId and l.removed = false")
    public Lesson getLessonById(long lessonId);
}
