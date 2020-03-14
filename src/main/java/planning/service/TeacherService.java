package planning.service;

import planning.model.Teacher;
import planning.modelVO.TeacherVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeacherService {

    public static TeacherVO getTeacherVO(Teacher teacher) {
        if (teacher == null)
            return null;

        return TeacherVO.builder()
                .created(teacher.getCreated())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .prefix(teacher.getPrefix())
                .username(teacher.getUsername())
                .password(teacher.getPassword())
                .avatar(teacher.getAvatar())
                .build();
    }

    public static List<TeacherVO> getTeacherVOs(List<Teacher> teachers) {
        List<TeacherVO> vos = new ArrayList<>();
        teachers.stream().filter(Objects::nonNull).forEach(teacher -> vos.add(getTeacherVO(teacher)));

        return vos;
    }
}
