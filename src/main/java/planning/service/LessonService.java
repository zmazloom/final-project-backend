package planning.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import planning.exception.InvalidRequestException;
import planning.message.CommonMessage;
import planning.model.Lesson;
import planning.modelVO.LessonVO;
import planning.repository.LessonCRUD;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class LessonService {

    private final LessonCRUD lessonCRUD;

    private ModelMapper modelMapper = new ModelMapper();

    public LessonVO getLessonVO(Lesson lesson) {
        if (lesson == null)
            return null;

        return modelMapper.map(lesson, LessonVO.class);
    }

    public List<LessonVO> getLessonVOs(List<Lesson> lessons) {
        List<LessonVO> vos = new ArrayList<>();
        lessons.stream().filter(Objects::nonNull).forEach(lesson -> vos.add(getLessonVO(lesson)));

        return vos;
    }

    public Lesson addLesson(LessonVO lessonVO) {
        Lesson lesson = new Lesson();
        lesson.setName(lessonVO.getName());
        lesson.setUnit(lessonVO.getUnit());
        lesson.setCode(lessonVO.getCode());
        lesson.setNumber(lessonVO.getNumber());
        lesson.setGrade(lessonVO.getGrade());

        return lessonCRUD.saveAndFlush(lesson);
    }

    public void deleteLesson(Lesson lesson) {
        if(lesson != null) {
            lesson.setRemoved(true);

            lessonCRUD.saveAndFlush(lesson);
        }
    }

    public Lesson updateLesson(Lesson lesson, LessonVO lessonVO) {
        if(lessonVO != null) {
            if(lessonVO.getName() != null && lessonVO.getName().equals(""))
                throw InvalidRequestException.getInstance(CommonMessage.getParamRequired("name"));

            if(lessonVO.getName() != null)
                lesson.setName(lessonVO.getName());
            if(lessonVO.getCode() != null)
                lesson.setCode(lessonVO.getCode());
            if(lessonVO.getNumber() != null)
                lesson.setNumber(lessonVO.getNumber());
            if(lessonVO.getGrade() != null)
                lesson.setGrade(lessonVO.getGrade());
            if(lessonVO.getUnit() != null)
                lesson.setUnit(lessonVO.getUnit());

            return lessonCRUD.saveAndFlush(lesson);
        }

        return null;
    }
}
