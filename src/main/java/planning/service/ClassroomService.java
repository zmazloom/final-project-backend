package planning.service;

import planning.model.Classroom;
import planning.modelVO.ClassroomVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassroomService {

    public static ClassroomVO getClassroomVO(Classroom classroom) {
        if (classroom == null)
            return null;

        return ClassroomVO.builder()
                .id(classroom.getId())
                .created(classroom.getCreated())
                .removed(classroom.isRemoved())
                .name(classroom.getName())
                .build();
    }

    public static List<ClassroomVO> getClassroomVOs(List<Classroom> classrooms) {
        List<ClassroomVO> vos = new ArrayList<>();
        classrooms.stream().filter(Objects::nonNull).forEach(classroom -> vos.add(getClassroomVO(classroom)));

        return vos;
    }
}
