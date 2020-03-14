package planning.modelVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherAddVO {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String prefix;

    @NotNull
    @NotEmpty
    private String username;

    private File avatar;

}
