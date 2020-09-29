package planning.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import planning.model.LessonGroup;
import planning.model.Plan;
import planning.modelVO.LessonGroupVO;
import planning.repository.LessonCRUD;
import planning.repository.LessonGroupCRUD;
import planning.repository.TeacherCRUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GroupService {

    private final TeacherCRUD teacherCRUD;
    private final LessonCRUD lessonCRUD;
    private final LessonGroupCRUD lessonGroupCRUD;

    public LessonGroupVO getLessonGroupVO(LessonGroup lessonGroup) {
        if (lessonGroup == null)
            return null;

        return LessonGroupVO.builder()
                .teacherId(lessonGroup.getTeacher().getId())
                .lessonId(lessonGroup.getLesson().getId())
                .planId(lessonGroup.getPlan().getId())
                .number(lessonGroup.getNumber())
                .jalaseNumber(lessonGroup.getJalaseNumber())
                .zarfiat(lessonGroup.getZarfiat())
                .build();
    }

    public List<LessonGroupVO> getLessonGroupVOs(List<LessonGroup> lessonGroups) {
        List<LessonGroupVO> vos = new ArrayList<>();
        lessonGroups.stream().filter(Objects::nonNull).forEach(lessonGroup -> vos.add(getLessonGroupVO(lessonGroup)));

        return vos;
    }

    public LessonGroup addLessonGroup(Plan plan, LessonGroupVO lessonGroupVO) {
        LessonGroup lessonGroup = new LessonGroup();
        lessonGroup.setNumber(lessonGroupVO.getNumber());
        lessonGroup.setZarfiat(lessonGroupVO.getZarfiat());
        lessonGroup.setJalaseNumber(lessonGroupVO.getJalaseNumber());
        lessonGroup.setTeacher(teacherCRUD.getTeacherById(lessonGroupVO.getTeacherId()));
        lessonGroup.setLesson(lessonCRUD.getLessonById(lessonGroupVO.getLessonId()));
        lessonGroup.setPlan(plan);

        return lessonGroupCRUD.saveAndFlush(lessonGroup);
    }

    public void deleteLessonGroup(LessonGroup lessonGroup) {
        if (lessonGroup != null) {
            lessonGroup.setRemoved(true);

            lessonGroupCRUD.saveAndFlush(lessonGroup);
        }
    }

    public LessonGroup updateLessonGroup(LessonGroup lessonGroup, LessonGroupVO lessonGroupVO) {
        if (lessonGroupVO != null) {
            lessonGroup.setNumber(lessonGroupVO.getNumber());
            lessonGroup.setZarfiat(lessonGroupVO.getZarfiat());
            lessonGroup.setJalaseNumber(lessonGroupVO.getJalaseNumber());
            lessonGroup.setTeacher(teacherCRUD.getTeacherById(lessonGroupVO.getTeacherId()));
            lessonGroup.setLesson(lessonCRUD.getLessonById(lessonGroupVO.getLessonId()));
            return lessonGroupCRUD.saveAndFlush(lessonGroup);
        }

        return null;
    }

}
