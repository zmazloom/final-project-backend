package planning.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import planning.model.Classroom;
import planning.modelVO.ClassroomVO;
import planning.repository.ClassroomCRUD;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ClassroomService {

    private final ClassroomCRUD classroomCRUD;

    private ModelMapper modelMapper = new ModelMapper();

    public ClassroomVO getClassroomVO(Classroom classroom) {
        if (classroom == null)
            return null;

        return modelMapper.map(classroom, ClassroomVO.class);
    }

    public List<ClassroomVO> getClassroomVOs(List<Classroom> classrooms) {
        List<ClassroomVO> vos = new ArrayList<>();
        classrooms.stream().filter(Objects::nonNull).forEach(classroom -> vos.add(getClassroomVO(classroom)));

        return vos;
    }

    public Classroom addClassroom(ClassroomVO classroomVO) {
        Classroom classroom = new Classroom();
        classroom.setName(classroomVO.getName());

        return classroomCRUD.saveAndFlush(classroom);
    }

    public void deleteClassroom(Classroom classroom) {
        if(classroom != null) {
            classroom.setRemoved(true);

            classroomCRUD.saveAndFlush(classroom);
        }
    }

    public Classroom updateClassroom(Classroom classroom, ClassroomVO classroomVO) {
        if(classroomVO != null) {
            if(classroomVO.getName() != null)
                classroom.setName(classroomVO.getName());
            return classroomCRUD.saveAndFlush(classroom);
        }

        return null;
    }
}
