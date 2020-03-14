package planning.modelVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherVO {

    private String firstName;
    private String lastName;
    private String prefix;
    private String username;
    private String password;
    private String avatar;
    private Date created;

}
