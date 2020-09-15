package planning.modelVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import planning.model.PlanDetail;
import planning.model.Time;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDetailVO {

    @NotNull
    private Long teacherId;

    @NotNull
    private Long classroomId;

    @NotNull
    private Long lessonId;

    @NotNull
    private Time time;

    private PlanDetail.WeekType weekType;

}
