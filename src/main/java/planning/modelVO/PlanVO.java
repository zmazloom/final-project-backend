package planning.modelVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import planning.model.TimeType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanVO {

    private long id;
    private Date created;
    private boolean removed;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private TimeType timeType;

    private int nimsal;
}
