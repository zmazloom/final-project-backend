package planning.service;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import planning.model.*;
import planning.modelVO.*;
import planning.repository.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PlanService {

    private final PlanCRUD planCRUD;
    private final PlanDetailCRUD planDetailCRUD;
    private final TeacherCRUD teacherCRUD;
    private final ClassroomCRUD classroomCRUD;
    private final LessonCRUD lessonCRUD;
    private final LessonGroupCRUD lessonGroupCRUD;
    private final TeacherService teacherService;
    private final LessonService lessonService;
    private final GroupService groupService;
    private final ClassroomService classroomService;
    private final TeacherTimeCRUD teacherTimeCRUD;

    private ModelMapper modelMapper = new ModelMapper();

    public PlanVO getPlanVO(Plan plan) {
        if (plan == null)
            return null;

        return modelMapper.map(plan, PlanVO.class);
    }

    public List<PlanVO> getPlanVOs(List<Plan> plans) {
        List<PlanVO> vos = new ArrayList<>();
        plans.stream().filter(Objects::nonNull).forEach(plan -> vos.add(getPlanVO(plan)));

        return vos;
    }

    public Plan addPlan(PlanVO planVO) {
        Plan plan = new Plan();
        plan.setName(planVO.getName());
        plan.setTimeType(planVO.getTimeType());
        plan.setNimsal(planVO.getNimsal());

        return planCRUD.saveAndFlush(plan);
    }

    public Plan updatePlan(Plan plan, String newName, int nimsal) {
        plan.setName(newName);
        plan.setNimsal(nimsal);

        planCRUD.saveAndFlush(plan);
        return plan;
    }

    public void deletePlan(Plan plan) {
        if (plan != null) {
            plan.setRemoved(true);
            planCRUD.saveAndFlush(plan);
        }
    }

    public Plan copyPlan(Plan plan, String name, int nimsal) {
        Plan copyPlan = new Plan();
        copyPlan.setName(name);
        copyPlan.setTimeType(plan.getTimeType());
        copyPlan.setNimsal(nimsal);
        Plan newPlan = planCRUD.saveAndFlush(copyPlan);

        copyGroups(plan, newPlan);
        copyTeacherTimes(plan, newPlan);

        return copyPlan;
    }

    public List<PlanDetailGet> getPlanDetails(Plan plan) {
        List<PlanDetail> planDetails = planDetailCRUD.getPlanDetails(plan);
        List<PlanDetailGet> planDetailGets = new ArrayList<>();
        if (planDetails != null && !planDetails.isEmpty()) {
            for (PlanDetail planDetail : planDetails) {
                PlanDetailGet planDetailGet = new PlanDetailGet();

                if (planDetail.getGroup() != null) {
                    planDetailGet.setGroupId(planDetail.getGroup().getId());
                    planDetailGet.setJalaseNumber(planDetail.getGroup().getJalaseNumber());
                    planDetailGet.setZarfiat(planDetail.getGroup().getZarfiat());
                    planDetailGet.setNumber(planDetail.getGroup().getNumber());

                    if (planDetail.getGroup().getTeacher() != null) {
                        planDetailGet.setFirstName(planDetail.getGroup().getTeacher().getFirstName());
                        planDetailGet.setLastName(planDetail.getGroup().getTeacher().getLastName());
                        planDetailGet.setUsername(planDetail.getGroup().getTeacher().getUsername());
                        planDetailGet.setPrefix(planDetail.getGroup().getTeacher().getPrefix());
                        planDetailGet.setTeacherId(planDetail.getGroup().getTeacher().getId());
                    }
                    if (planDetail.getGroup().getLesson() != null) {
                        planDetailGet.setName(planDetail.getGroup().getLesson().getName());
                        planDetailGet.setCode(planDetail.getGroup().getLesson().getCode());
                        planDetailGet.setTerm(planDetail.getGroup().getLesson().getTerm());
                        planDetailGet.setGrade(planDetail.getGroup().getLesson().getGrade());
                        planDetailGet.setUnit(planDetail.getGroup().getLesson().getUnit());
                        planDetailGet.setLessonId(planDetail.getGroup().getLesson().getId());
                    }
                }
                if (planDetail.getClassroom() != null) {
                    planDetailGet.setClassroomName(planDetail.getClassroom().getName());
                    planDetailGet.setClassroomId(planDetail.getClassroom().getId());
                }

                planDetailGet.setId(planDetail.getId());
                planDetailGet.setTime(planDetail.getTime());
                planDetailGet.setWeekType(planDetail.getWeekType());

                planDetailGets.add(planDetailGet);
            }
        }

        return planDetailGets;
    }

    public void savePlanning(Plan plan, List<PlanDetailVO> planDetailVOS) {
        List<PlanDetail> planDetails = planDetailCRUD.getPlanDetails(plan);

        if (planDetailVOS != null) {
            if (planDetails != null) {
                for (int i = 0; i < planDetails.size(); i++) {
                    boolean fined = false;
                    for (PlanDetailVO planDetailVO : planDetailVOS) {
                        if (planDetails.get(i).getClassroom().getId().equals(planDetailVO.getClassroomId()) &&
                                planDetails.get(i).getGroup().getId().equals(planDetailVO.getGroupId()) &&
                                planDetails.get(i).getTime().equals(planDetailVO.getTime()) &&
                                (planDetailVO.getWeekType() == null || planDetails.get(i).getWeekType().equals(planDetailVO.getWeekType()))) {
                            planDetailVOS.remove(planDetailVO);
                            fined = true;
                            break;
                        }
                    }
                    if (!fined) {
                        PlanDetail removedPlanDetail = planDetails.get(i);
                        planDetails.remove(removedPlanDetail);
                        planDetailCRUD.delete(removedPlanDetail);
                        i--;
                    }
                }
            }

            for (PlanDetailVO planDetailVO : planDetailVOS) {
                PlanDetail planDetail = new PlanDetail();
                planDetail.setClassroom(classroomCRUD.getClassroomById(planDetailVO.getClassroomId()));
                planDetail.setGroup(lessonGroupCRUD.getLessonGroupById(planDetailVO.getGroupId()));
                planDetail.setPlan(plan);
                planDetail.setTime(planDetailVO.getTime());
                planDetail.setWeekType(planDetailVO.getWeekType());

                planDetailCRUD.saveAndFlush(planDetail);
            }
        }

    }

    public List<Course> pasreToCourses(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try (InputStream is = multipartFile.getInputStream()) {
            Files.copy(is, file.toPath());
        }
        try {
            Document htmlFile = Jsoup.parse(file, "UTF-8");

            Element container = htmlFile.getElementsByClass("container-fluid").stream()
                    .filter(elem -> !elem.parent().tagName().equalsIgnoreCase("body")).findAny().orElse(null);

            Element table = container != null ? container.getElementsByTag("table").stream().filter(elem -> elem.attributes().get("border").equals("1")).findAny().orElse(null) : null;

            List<Element> rows = table != null ? table.getElementsByTag("tr") : null;

            List<Course> courses = new ArrayList<>();

            if (rows != null) {
                rows.forEach(row -> {
                    List<Element> cols = row.getElementsByTag("td");
                    if (cols == null || cols.isEmpty())
                        return;

                    Teacher professor;
                    professor = Teacher.builder().lastName(cols.get(8).childNode(0).toString().replace("&nbsp;", "")).build();

                    String name = cols.get(3).childNode(0).toString();
                    String degree = "";
                    List<Course.Time> times = new ArrayList<>();
                    Classroom classroom = new Classroom();

                    String title = cols.get(10).childNode(0).attributes().get("title");
                    if (title != null && !title.isEmpty()) {
                        String body = title.substring(title.indexOf(" body=[") + 7).replace("]", "");
                        String[] tags = body.split("<br>");
                        for (String tag : tags) {
                            Document html = Jsoup.parse(tag);
                            if (html.getElementsByTag("b").get(0).childNode(0).toString().contains("مقطع")) {
                                degree = html.getElementsByTag("body").get(0).childNode(1).toString();
                                if (degree != null && degree.length() > 2)
                                    degree = degree.substring(1);
                            } else if (html.getElementsByTag("b").get(0).childNode(0).toString().contains("جلسه")) {
                                String time = html.getElementsByTag("body").get(0).childNode(1).toString();
                                times.add(Course.Time.builder()
                                        .time(time.substring(1, time.indexOf("(")))
                                        .type(time.contains("زوج") ? PlanDetail.WeekType.ZOJ :
                                                (time.contains("فرد") ? PlanDetail.WeekType.FARD : PlanDetail.WeekType.HAR))
                                        .build());

                                if (time.contains("کلاس")) {
                                    try {
                                        classroom.setName(time.substring(time.indexOf("کلاس") + 4, time.indexOf(")")));
                                    } catch (Exception ignored) {
                                    }
                                }
                            }
                        }

                    }

                    courses.add(Course.builder()
                            .number(Integer.valueOf(cols.get(1).child(0).childNode(0).toString()))
                            .code(Integer.valueOf(cols.get(2).childNode(0).toString()))
                            .name(name)
                            .unit(Double.valueOf(cols.get(4).childNode(0).toString()))
                            .capacity(Integer.valueOf(cols.get(6).childNode(0).toString()))
                            .teacher(professor)
                            .degree(degree)
                            .times(times)
                            .classroom(classroom)
                            .build());
                });
            }

            return courses;
        } finally {
            file.delete();
        }
    }

    public void convertCoursesToThisProject(Plan plan, List<Course> courses) {
        if (courses != null) {
            List<Teacher> teachers = teacherCRUD.getAllTeachers();
            List<Lesson> lessons = lessonCRUD.getAllLessons();
            List<Classroom> classrooms = classroomCRUD.getAllClassrooms();

            createClassAndTeacherAndLessons(courses, teachers, lessons, classrooms);
            createGroups(plan, courses);
        }
    }

    private void createClassAndTeacherAndLessons(List<Course> courses, List<Teacher> teachers, List<Lesson> lessons, List<Classroom> classrooms) {
        for (Course course : courses) {
            if (course.getClass() != null && course.getClassroom().getName() != null && !course.getClassroom().getName().equals("")) {
                boolean fined = false;

                if (classrooms != null) {
                    for (Classroom classroom : classrooms) {
                        if (course.getClassroom().getName().equalsIgnoreCase(classroom.getName())) {
                            course.getClassroom().setId(classroom.getId());
                            fined = true;
                            break;
                        }
                    }
                }

                if (!fined) {
                    ClassroomVO newClassroom = new ClassroomVO();
                    newClassroom.setName(course.getClassroom().getName());
                    newClassroom.setZarfiat(course.getCapacity());
                    Classroom newClassroomSaved = classroomService.addClassroom(newClassroom);
                    if (classrooms != null)
                        classrooms.add(newClassroomSaved);
                    course.getClassroom().setId(newClassroomSaved.getId());
                }
            }
            if (course.getTeacher() != null && course.getTeacher().getLastName() != null && !course.getTeacher().getLastName().equals("")) {
                boolean fined = false;

                if (teachers != null) {
                    for (Teacher teacher : teachers) {
                        if (course.getTeacher().getLastName().equalsIgnoreCase(teacher.getLastName()) ||
                                course.getTeacher().getLastName().equalsIgnoreCase(teacher.getFirstName() + " " + teacher.getLastName())) {
                            course.getTeacher().setId(teacher.getId());
                            fined = true;
                            break;
                        }
                    }
                }

                if (!fined) {
                    TeacherAddVO newTeacher = new TeacherAddVO();
                    newTeacher.setFirstName("");
                    newTeacher.setLastName(course.getTeacher().getLastName());
                    newTeacher.setPrefix(TeacherPrefix.OSTAD);
                    newTeacher.setUsername(course.getTeacher().getLastName());
                    newTeacher.setPassword("123456789");
                    Teacher newTeacherSaved = teacherService.addTeacher(newTeacher, null);
                    if (teachers != null)
                        teachers.add(newTeacherSaved);
                    course.getTeacher().setId(newTeacherSaved.getId());
                }
            }
            if (course.getName() != null && !course.getName().equals("")) {
                boolean fined = false;

                if (lessons != null) {
                    for (Lesson lesson : lessons) {
                        if (course.getName().equalsIgnoreCase(lesson.getName()) &&
                                course.getCode().equals(lesson.getCode())) {
                            course.setLessonId(lesson.getId());
                            fined = true;
                            break;
                        }
                    }
                }

                if (!fined) {
                    LessonVO newLesson = new LessonVO();
                    newLesson.setName(course.getName());
                    newLesson.setCode(course.getCode());

                    if (course.getDegree() != null) {
                        if (course.getDegree().equalsIgnoreCase("کارشناسی ارشد"))
                            newLesson.setGrade(Grade.KARSHENASI);
                        else if (course.getDegree().equalsIgnoreCase("دکترا"))
                            newLesson.setGrade(Grade.KARSHENASI);
                        else
                            newLesson.setGrade(Grade.KARSHENASI);
                    } else
                        newLesson.setGrade(Grade.KARSHENASI);

                    if (course.getUnit() != null) {
                        if (course.getUnit() == 1D)
                            newLesson.setUnit(Unit.ONE);
                        else if (course.getUnit() == 2D)
                            newLesson.setUnit(Unit.TWO);
                        else if (course.getUnit() == 3D)
                            newLesson.setUnit(Unit.THREE);
                        else if (course.getUnit() == 4D)
                            newLesson.setUnit(Unit.FOUR);
                        else
                            newLesson.setUnit(Unit.ZERO);
                    } else
                        newLesson.setUnit(Unit.ZERO);

                    Lesson newLessonSaved = lessonService.addLesson(newLesson);
                    if (lessons != null)
                        lessons.add(newLessonSaved);
                    course.setLessonId(newLessonSaved.getId());
                }
            }
        }
    }

    private void createGroups(Plan plan, List<Course> courses) {
        for (Course course : courses) {

            if (course.getLessonId() != null && course.getLessonId() != 0) {

                LessonGroupVO lessonGroupVO = new LessonGroupVO();

                lessonGroupVO.setNumber(course.getNumber() != null ? String.valueOf(course.getNumber()) : "");
                lessonGroupVO.setZarfiat(course.getCapacity() != null ? course.getCapacity() : 0);

                int jalaseNumber = 0;
                if (course.getTimes() != null && !course.getTimes().isEmpty()) {
                    for (Course.Time time : course.getTimes()) {
                        if (time != null) {
                            if (time.getType() != null && !time.getType().equals(PlanDetail.WeekType.HAR))
                                jalaseNumber++;
                            else
                                jalaseNumber += 2;
                        }
                    }
                }

                lessonGroupVO.setJalaseNumber(jalaseNumber);

                lessonGroupVO.setTeacherId(course.getTeacher() != null ? course.getTeacher().getId() : null);
                lessonGroupVO.setLessonId(course.getLessonId());
                lessonGroupVO.setPlanId(plan.getId());

                LessonGroup lessonGroup = groupService.addLessonGroup(plan, lessonGroupVO);
                addLessonGroupToPlan(plan, lessonGroup, course);
            }
        }
    }

    private void addLessonGroupToPlan(Plan plan, LessonGroup lessonGroup, Course course) {
        if (course.getTimes() != null && !course.getTimes().isEmpty()) {

            for (Course.Time time : course.getTimes()) {
                Time groupTime = convertTextToTime(time.getTime());
                PlanDetail.WeekType weekType = time.getType();

                if (groupTime != null && weekType != null) {
                    if (weekType.equals(PlanDetail.WeekType.HAR)) {
                        PlanDetail planDetail1 = new PlanDetail();
                        planDetail1.setTime(groupTime);
                        planDetail1.setWeekType(PlanDetail.WeekType.ZOJ);
                        planDetail1.setClassroom(course.getClassroom() != null ? classroomCRUD.getClassroomById(course.getClassroom().getId()) : null);
                        planDetail1.setGroup(lessonGroup);
                        planDetail1.setPlan(plan);
                        planDetailCRUD.saveAndFlush(planDetail1);

                        PlanDetail planDetail2 = new PlanDetail();
                        planDetail2.setTime(groupTime);
                        planDetail2.setWeekType(PlanDetail.WeekType.FARD);
                        planDetail2.setClassroom(course.getClassroom() != null ? classroomCRUD.getClassroomById(course.getClassroom().getId()) : null);
                        planDetail2.setGroup(lessonGroup);
                        planDetail2.setPlan(plan);
                        planDetailCRUD.saveAndFlush(planDetail2);
                    } else {
                        PlanDetail planDetail = new PlanDetail();
                        planDetail.setTime(groupTime);
                        planDetail.setWeekType(weekType);
                        planDetail.setClassroom(course.getClassroom() != null ? classroomCRUD.getClassroomById(course.getClassroom().getId()) : null);
                        planDetail.setGroup(lessonGroup);
                        planDetail.setPlan(plan);
                        planDetailCRUD.saveAndFlush(planDetail);
                    }

                }
            }
        }
    }

    private Time convertTextToTime(String timeText) {
        if (timeText != null) {
            switch (timeText) {
                case "شنبه ساعت 8":
                    return Time.SHANBE8T;
                case "شنبه ساعت 10":
                    return Time.SHANBE10T;
                case "شنبه ساعت 12":
                    return Time.SHANBE12T;
                case "شنبه ساعت 14":
                    return Time.SHANBE14T;
                case "شنبه ساعت 16":
                    return Time.SHANBE16T;
                case "شنبه ساعت 18":
                    return Time.SHANBE18T;
                case "یکشنبه ساعت 8":
                    return Time.YEKSHANBE8T;
                case "یکشنبه ساعت 10":
                    return Time.YEKSHANBE10T;
                case "یکشنبه ساعت 12":
                    return Time.YEKSHANBE12T;
                case "یکشنبه ساعت 14":
                    return Time.YEKSHANBE14T;
                case "یکشنبه ساعت 16":
                    return Time.YEKSHANBE16T;
                case "یکشنبه ساعت 18":
                    return Time.YEKSHANBE18T;
                case "دوشنبه ساعت 8":
                    return Time.DOSHANBE8T;
                case "دوشنبه ساعت 10":
                    return Time.DOSHANBE10T;
                case "دوشنبه ساعت 12":
                    return Time.DOSHANBE12T;
                case "دوشنبه ساعت 14":
                    return Time.DOSHANBE14T;
                case "دوشنبه ساعت 16":
                    return Time.DOSHANBE16T;
                case "دوشنبه ساعت 18":
                    return Time.DOSHANBE18T;
                case "سه شنبه ساعت 8":
                    return Time.SESHANBE8T;
                case "سه شنبه ساعت 10":
                    return Time.SESHANBE10T;
                case "سه شنبه ساعت 12":
                    return Time.SESHANBE12T;
                case "سه شنبه ساعت 14":
                    return Time.SESHANBE14T;
                case "سه شنبه ساعت 16":
                    return Time.SESHANBE16T;
                case "سه شنبه ساعت 18":
                    return Time.SESHANBE18T;
                case "چهارشنبه ساعت 8":
                    return Time.CHARSHANBE8T;
                case "چهارشنبه ساعت 10":
                    return Time.CHARSHANBE10T;
                case "چهارشنبه ساعت 12":
                    return Time.CHARSHANBE12T;
                case "چهارشنبه ساعت 14":
                    return Time.CHARSHANBE14T;
                case "چهارشنبه ساعت 16":
                    return Time.CHARSHANBE16T;
                case "چهارشنبه ساعت 18":
                    return Time.CHARSHANBE18T;
                case "پنجشنبه ساعت 8":
                    return Time.PANJSHANBE8T;
                case "پنجشنبه ساعت 10":
                    return Time.PANJSHANBE10T;
                case "پنجشنبه ساعت 12":
                    return Time.PANJSHANBE12T;
                case "پنجشنبه ساعت 14":
                    return Time.PANJSHANBE14T;
                case "پنجشنبه ساعت 16":
                    return Time.PANJSHANBE16T;
                case "پنجشنبه ساعت 18":
                    return Time.PANJSHANBE18T;
                default:
                    return null;
            }
        }

        return null;
    }

    private void copyGroups(Plan plan, Plan newPlan) {
        if (plan != null && newPlan != null) {
            List<LessonGroup> lessonGroups = lessonGroupCRUD.getAllLessonGroups(plan);

            if (lessonGroups != null && !lessonGroups.isEmpty()) {
                for (LessonGroup lessonGroup : lessonGroups) {
                    LessonGroup newLessonGroup = new LessonGroup();
                    newLessonGroup.setPlan(newPlan);
                    newLessonGroup.setLesson(lessonGroup.getLesson());
                    newLessonGroup.setTeacher(lessonGroup.getTeacher());
                    newLessonGroup.setJalaseNumber(lessonGroup.getJalaseNumber());
                    newLessonGroup.setNumber(lessonGroup.getNumber());
                    newLessonGroup.setZarfiat(lessonGroup.getZarfiat());
                    LessonGroup lessonGroupSaved = lessonGroupCRUD.saveAndFlush(newLessonGroup);

                    List<PlanDetail> planDetails = planDetailCRUD.getPlanDetailByGroup(plan, lessonGroup);
                    if (planDetails != null && !planDetails.isEmpty()) {
                        for (PlanDetail planDetail : planDetails) {
                            PlanDetail newPlanDetail = new PlanDetail();
                            newPlanDetail.setGroup(lessonGroupSaved);
                            newPlanDetail.setClassroom(planDetail.getClassroom());
                            newPlanDetail.setPlan(newPlan);
                            newPlanDetail.setTime(planDetail.getTime());
                            newPlanDetail.setWeekType(planDetail.getWeekType());

                            planDetailCRUD.saveAndFlush(newPlanDetail);
                        }
                    }

                }
            }
        }
    }

    private void copyTeacherTimes(Plan plan, Plan newPlan) {
        if (plan != null && newPlan != null) {
            List<TeacherTime> teacherTimes = teacherTimeCRUD.getAllTeacherTimes(plan);

            if (teacherTimes != null && !teacherTimes.isEmpty()) {
                for (TeacherTime teacherTime : teacherTimes) {
                    TeacherTime newTeacherTime = new TeacherTime();
                    newTeacherTime.setPlan(newPlan);
                    newTeacherTime.setPriority(teacherTime.getPriority());
                    newTeacherTime.setTeacher(teacherTime.getTeacher());
                    newTeacherTime.setTime(teacherTime.getTime());
                    teacherTimeCRUD.saveAndFlush(newTeacherTime);
                }
            }
        }
    }

}
