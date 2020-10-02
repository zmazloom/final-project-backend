package planning.modelVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import planning.model.Time;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherAllTimeGetVO {

    private Time time;
    private int priority;
    private long planId;

}
