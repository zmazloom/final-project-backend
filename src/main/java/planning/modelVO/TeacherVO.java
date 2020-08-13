package planning.modelVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import planning.model.TeacherPrefix;

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
    private TeacherPrefix prefix;
    private String username;
    private String password;
    private String avatar;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", created='" + created + '\'' +
                ", removed='" + removed + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", prefix='" + prefix + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

}
