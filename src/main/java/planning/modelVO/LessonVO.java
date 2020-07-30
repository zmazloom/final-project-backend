package planning.modelVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import planning.model.Lesson;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
    private Lesson.Unit unit;

    @NotNull
    private Lesson.Grade grade;

}
