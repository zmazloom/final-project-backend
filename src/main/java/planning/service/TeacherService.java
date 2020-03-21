package planning.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import planning.model.Teacher;
import planning.modelVO.TeacherVO;
import planning.repository.TeacherCRUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TeacherService {

    private final TeacherCRUD teacherCRUD;

    private ModelMapper modelMapper = new ModelMapper();

    public TeacherVO getTeacherVO(Teacher teacher) {
        if (teacher == null)
            return null;

        return modelMapper.map(teacher, TeacherVO.class);
    }

    public List<TeacherVO> getTeacherVOs(List<Teacher> teachers) {
        List<TeacherVO> vos = new ArrayList<>();
        teachers.stream().filter(Objects::nonNull).forEach(teacher -> vos.add(getTeacherVO(teacher)));

        return vos;
    }

    public void deleteTeacher(Teacher teacher) {
        if(teacher != null) {
            teacher.setRemoved(true);
            teacherCRUD.saveAndFlush(teacher);
        }
    }
}
