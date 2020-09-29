package planning.modelVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonGroupVO {

    @NotNull
    private Long teacherId;

    @NotNull
    private Long lessonId;

    @NotNull
    private Long planId;

    @NotNull
    private String number;

    private int zarfiat;

    private Integer jalaseNumber;

}
