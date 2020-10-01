package planning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teacher_time")
public class TeacherTime {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @CreationTimestamp
    private Date created;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    @Enumerated
    private Time time;

    @Column
    private int priority;
}
