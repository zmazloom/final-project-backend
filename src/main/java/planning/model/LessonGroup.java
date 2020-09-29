package planning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lesson_group")
public class LessonGroup {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @CreationTimestamp
    private Date created;

    @Column
    private boolean removed;

    @Column
    private String number;

    @Column
    private int zarfiat;

    @Column
    private int jalaseNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plan plan;

}
