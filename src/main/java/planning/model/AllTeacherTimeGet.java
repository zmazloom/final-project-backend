package planning.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllTeacherTimeGet {

    private Time time;
    private int priority;

    //techer
    private long teacherId;
    private String firstName;
    private String lastName;

}
