package planning.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String code;
    private String number;
    private Unit unit;
    private Grade grade;

    //classroom
    private long classroomId;
    private String classroomName;

}
