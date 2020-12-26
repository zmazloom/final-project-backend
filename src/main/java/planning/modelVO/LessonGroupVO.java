package planning.modelVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import planning.model.Grade;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonGroupVO {

    private Long groupId;

    @NotNull
    private Long teacherId;
    private String firstName;
    private String lastName;
    private String teacherName;

    @NotNull
    private Long lessonId;
    private String name;
    private Integer code;
    private String lessonName;
    private Grade grade;

    private Long planId;

    @NotNull
    private String number;

    private int zarfiat;

    private Integer jalaseNumber;

}
