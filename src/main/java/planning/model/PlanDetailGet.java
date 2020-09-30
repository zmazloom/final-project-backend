package planning.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDetailGet {

    private long id;
    private PlanDetail.WeekType weekType;
    private Time time;

    //techer
    private long teacherId;
    private String firstName;
    private String lastName;
    private TeacherPrefix prefix;
    private String username;

    //lesson
    private long lessonId;
    private String name;
    private int code;
    private Unit unit;
    private Grade grade;
    private List<Integer> term;

    //classroom
    private long classroomId;
    private String classroomName;

}
