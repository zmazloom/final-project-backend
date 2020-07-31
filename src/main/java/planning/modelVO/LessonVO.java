package planning.modelVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import planning.model.Grade;
import planning.model.Unit;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonVO {

    private long id;
    private String created;
    private boolean removed;

    @NotNull
    @NotEmpty
    private String name;

    private String code;

    private String number;

    @NotNull
    private Unit unit;

    @NotNull
    private Grade grade;

}
