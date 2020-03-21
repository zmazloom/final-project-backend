package planning.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import planning.exception.InternalServerException;
import planning.exception.InvalidRequestException;
import planning.message.CommonMessage;
import planning.message.TeacherMessage;
import planning.model.Teacher;
import planning.modelVO.TeacherAddVO;
import planning.modelVO.TeacherVO;
import planning.repository.TeacherCRUD;
import planning.utils.PasswordUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
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
        if (teacher != null) {
            teacher.setRemoved(true);
            teacherCRUD.saveAndFlush(teacher);
        }
    }

    public Teacher addTeacher(TeacherAddVO teacherAddVO, MultipartFile avatar) {
        validateTeacherInsert(teacherAddVO);

        Teacher teacher = new Teacher();

        if (avatar != null && !avatar.isEmpty()) {
            teacher.setAvatar(saveAvatarFile(avatar));
        }

        if (teacherAddVO.getPassword() == null || teacherAddVO.getPassword().equals(""))
            teacher.setPassword(PasswordUtils.generatePassword());
        else
            teacher.setPassword(teacherAddVO.getPassword());

        teacher.setFirstName(teacherAddVO.getFirstName());
        teacher.setLastName(teacherAddVO.getLastName());
        teacher.setUsername(teacherAddVO.getUsername());
        teacher.setPrefix(teacherAddVO.getPrefix());

        return teacherCRUD.saveAndFlush(teacher);
    }

    public Teacher updateTeacher(Teacher teacher, TeacherAddVO teacherAddVO, MultipartFile avatar) {
        validateTeacherUpdate(teacherAddVO);

        if (avatar != null && !avatar.isEmpty()) {
            if(teacher.getAvatar() != null && !teacher.getAvatar().equals(""))
                deleteOldAvatar(teacher.getAvatar());

            teacher.setAvatar(saveAvatarFile(avatar));
        }
        if (teacherAddVO.getPassword() != null)
            teacher.setPassword(teacherAddVO.getPassword());
        if (teacherAddVO.getFirstName() != null)
            teacher.setFirstName(teacherAddVO.getFirstName());
        if (teacherAddVO.getLastName() != null)
            teacher.setLastName(teacherAddVO.getLastName());
        if (teacherAddVO.getUsername() != null)
            teacher.setUsername(teacherAddVO.getUsername());
        if (teacherAddVO.getPrefix() != null)
            teacher.setPrefix(teacherAddVO.getPrefix());

        return teacherCRUD.saveAndFlush(teacher);
    }

    private String saveAvatarFile(MultipartFile avatar) {
        try {
            Calendar calendar = Calendar.getInstance();

            byte[] bytes = avatar.getBytes();
            Path path = Paths.get("./avatar/" + calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.HOUR) + calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND) + "-" + avatar.getOriginalFilename());
            Files.write(path, bytes);
            return path.toString();
        } catch (IOException ex) {
            throw InternalServerException.getInstance(TeacherMessage.getErrorSaveAvatar());
        }
    }

    private void deleteOldAvatar(String filePath) {
        File file = new File(filePath);
        if(file.exists())
            file.delete();
    }

    private void validateTeacherInsert(TeacherAddVO teacherAddVO) {
        if(teacherAddVO.getUsername() == null || teacherAddVO.getUsername().equals("") || teacherAddVO.getUsername().length() < 4)
            throw InvalidRequestException.getInstance(TeacherMessage.getInvalidUsername());

        if(teacherAddVO.getFirstName() == null || teacherAddVO.getFirstName().equals(""))
            throw InvalidRequestException.getInstance(CommonMessage.getParamRequired("firstName"));

        if(teacherAddVO.getLastName() == null || teacherAddVO.getLastName().equals(""))
            throw InvalidRequestException.getInstance(CommonMessage.getParamRequired("lastName"));
    }

    private void validateTeacherUpdate(TeacherAddVO teacherAddVO) {
        if(teacherAddVO.getUsername() != null && (teacherAddVO.getUsername().equals("") || teacherAddVO.getUsername().length() < 4))
            throw InvalidRequestException.getInstance(TeacherMessage.getInvalidUsername());

        if(teacherAddVO.getPassword() != null && teacherAddVO.getPassword().equals(""))
            throw InvalidRequestException.getInstance(CommonMessage.getParamRequired("password"));

        if(teacherAddVO.getFirstName() != null && teacherAddVO.getFirstName().equals(""))
            throw InvalidRequestException.getInstance(CommonMessage.getParamRequired("firstName"));

        if(teacherAddVO.getLastName() != null && teacherAddVO.getLastName().equals(""))
            throw InvalidRequestException.getInstance(CommonMessage.getParamRequired("lastName"));
    }

}
