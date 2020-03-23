package planning.modelVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import planning.model.Time;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherTimeVO {

    @NotNull
    private Long planId;

    @NotNull
    private List<Time> times;

}
