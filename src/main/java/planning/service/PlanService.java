package planning.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import planning.model.Classroom;
import planning.modelVO.ClassroomVO;
import planning.repository.ClassroomCRUD;
import planning.repository.PlanCRUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PlanService {

    private final PlanCRUD planCRUD;

//    public PlanVO getPlanVO(Classroom classroom) {
//        if (classroom == null)
//            return null;
//
//        return ClassroomVO.builder()
//                .id(classroom.getId())
//                .created(classroom.getCreated())
//                .removed(classroom.isRemoved())
//                .name(classroom.getName())
//                .build();
//    }

//    public List<ClassroomVO> getClassroomVOs(List<Classroom> classrooms) {
//        List<ClassroomVO> vos = new ArrayList<>();
//        classrooms.stream().filter(Objects::nonNull).forEach(classroom -> vos.add(getClassroomVO(classroom)));
//
//        return vos;
//    }

//    public Classroom addPlan(ClassroomVO classroomVO) {
//        return planCRUD.saveAndFlush(classroom);
//    }

}
