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
@Table(name = "plan_detail")
public class PlanDetail {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @CreationTimestamp
    private Date created;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plan plan;

    @ManyToOne(fetch = FetchType.EAGER)
    private LessonGroup group;

    @Column
    @Enumerated
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    private Classroom classroom;

    @Enumerated
    private WeekType weekType;

    public enum WeekType {
        ZOJ,
        FARD,
        HAR
    }

}
