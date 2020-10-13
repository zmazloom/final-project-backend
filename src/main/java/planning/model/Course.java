package planning.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private String name;
    private Teacher teacher;
    private String degree;
    private Integer number;
    private Integer code;
    private Double unit;
    private Long lessonId;
    private Integer capacity;
    private List<Time> times;
    private Classroom classroom;


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Time {

        private String time;
        @Builder.Default
        private PlanDetail.WeekType type = PlanDetail.WeekType.HAR;
    }
}
