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
public class TimePriorityVO {

    private Time time;
    private int priority;   //1: bala, 2:motevaset, 3:payin

}
