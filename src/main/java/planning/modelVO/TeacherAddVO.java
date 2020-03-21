package planning.modelVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import planning.model.Teacher;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherAddVO {

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    private Teacher.TeacherPrefix prefix;

    @NotNull
    @NotEmpty
    @Size(min = 4)
    private String username;

    private String password;

}
