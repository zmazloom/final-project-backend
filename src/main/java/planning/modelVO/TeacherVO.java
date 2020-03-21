package planning.modelVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import planning.model.Teacher;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherVO {

    private Long id;
    private Date created;
    private boolean removed;
    private String firstName;
    private String lastName;
    private Teacher.TeacherPrefix prefix;
    private String username;
    private String password;
    private String avatar;

}
