package planning.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import planning.exception.InvalidRequestException;
import planning.message.PlanMessage;
import planning.message.TeacherMessage;
import planning.model.*;
import planning.modelVO.PlanDetailVO;
import planning.modelVO.PlanVO;
import planning.repository.*;

import java.util.ArrayList;
import java.util.HashMap;
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

        return planCRUD.saveAndFlush(plan);
    }

    public Plan updatePlan(Plan plan, String newName) {
        plan.setName(newName);
        planCRUD.saveAndFlush(plan);
        return plan;
    }

    public void deletePlan(Plan plan) {
        if (plan != null) {
            plan.setRemoved(true);
            planCRUD.saveAndFlush(plan);
        }
    }

    public Plan copyPlan(Plan plan, String name){
        Plan copyPlan = new Plan();
        copyPlan.setName(name);
        copyPlan.setTimeType(plan.getTimeType());
        planCRUD.saveAndFlush(copyPlan);

        return copyPlan;
    }

    public List<PlanDetailGet> getPlanDetails(Plan plan) {
        List<PlanDetail> planDetails = planDetailCRUD.getPlanDetails(plan);
        List<PlanDetailGet> planDetailGets = new ArrayList<>();
        if(planDetails != null && !planDetails.isEmpty()) {
            for(PlanDetail planDetail : planDetails) {
                PlanDetailGet planDetailGet = new PlanDetailGet();

                if(planDetail.getTeacher() != null) {
                    planDetailGet.setFirstName(planDetail.getTeacher().getFirstName());
                    planDetailGet.setLastName(planDetail.getTeacher().getLastName());
                    planDetailGet.setUsername(planDetail.getTeacher().getUsername());
                    planDetailGet.setPrefix(planDetail.getTeacher().getPrefix());
                    planDetailGet.setTeacherId(planDetail.getTeacher().getId());
                }
                if(planDetail.getLesson() != null) {
                    planDetailGet.setName(planDetail.getLesson().getName());
                    planDetailGet.setCode(planDetail.getLesson().getCode());
                    planDetailGet.setNumber(planDetail.getLesson().getNumber());
                    planDetailGet.setGrade(planDetail.getLesson().getGrade());
                    planDetailGet.setUnit(planDetail.getLesson().getUnit());
                    planDetailGet.setLessonId(planDetail.getLesson().getId());
                }
                if(planDetail.getClassroom() != null) {
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
                                planDetails.get(i).getLesson().getId().equals(planDetailVO.getLessonId()) &&
                                planDetails.get(i).getTeacher().getId().equals(planDetailVO.getTeacherId()) &&
                                planDetails.get(i).getTime().equals(planDetailVO.getTime()) &&
                                planDetails.get(i).getWeekType().equals(planDetailVO.getWeekType())) {
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
                planDetail.setLesson(lessonCRUD.getLessonById(planDetailVO.getLessonId()));
                planDetail.setTeacher(teacherCRUD.getTeacherById(planDetailVO.getTeacherId()));
                planDetail.setPlan(plan);
                planDetail.setTime(planDetailVO.getTime());
                planDetail.setWeekType(planDetailVO.getWeekType());

                planDetailCRUD.saveAndFlush(planDetail);
            }
        }

    }

    public HashMap<String, List<String>> checkPlanning(Plan plan, List<PlanDetailVO> planDetailVOS) {
        HashMap<String, List<String>> planWarnings = new HashMap<>();
        planWarnings.put("teacher_time", new ArrayList<>());
        planWarnings.put("class_time", new ArrayList<>());
        planWarnings.put("lesson_teacher", new ArrayList<>());
        planWarnings.put("lesson_unit", new ArrayList<>());

        if (planDetailVOS != null && !planDetailVOS.isEmpty()) {
            checkPlanTime(plan.getTimeType(), planDetailVOS);
            checkLessonUnit(planWarnings, planDetailVOS);
            checkTeacherTime(planWarnings, planDetailVOS);
            checkClassTime(planWarnings, planDetailVOS);
            checkLessonTeacher(planWarnings, planDetailVOS);
        }

        return planWarnings;
    }

    private void checkPlanTime(TimeType planTypeTime, List<PlanDetailVO> planDetailVOS) {
        if (planTypeTime.equals(TimeType.TWO_HOURS)) {

            List<Time> twoHourTimes = TeacherService.getTwoHourTimes();

            for (PlanDetailVO planDetailVO : planDetailVOS) {
                if (!twoHourTimes.contains(planDetailVO.getTime())) {
                    throw InvalidRequestException.getInstance(TeacherMessage.getInvalidTwoTime());
                }
            }
        } else {
            List<Time> oneThirtyHourTimes = TeacherService.getOneThirtyHourTimes();

            for (PlanDetailVO planDetailVO : planDetailVOS) {
                if (!oneThirtyHourTimes.contains(planDetailVO.getTime())) {
                    throw InvalidRequestException.getInstance(TeacherMessage.getInvalidOneThirtyTime());
                }
            }
        }
    }

    private void checkTeacherTime(HashMap<String, List<String>> planWarnings, List<PlanDetailVO> planDetailVOS) {
        for (int i = 0; i < planDetailVOS.size(); i++) {
            long teacherId = planDetailVOS.get(i).getTeacherId();

            for (int j = i + 1; j < planDetailVOS.size(); j++) {
                if (teacherId == planDetailVOS.get(j).getTeacherId()) {
                    if (planDetailVOS.get(i).getTime().equals(planDetailVOS.get(j).getTime()) &&
                            (
//                                    planDetailVOS.get(i).getWeekType().equals(PlanDetail.WeekType.HAR) ||
                                    planDetailVOS.get(i).getWeekType().equals(planDetailVOS.get(j).getWeekType()))) {
                        Teacher teacher = teacherCRUD.getTeacherById(teacherId);
                        planWarnings.get("teacher_time").add(PlanMessage.getInvalidTeacherTime(teacher.getFirstName() + " " + teacher.getLastName(), TimeService.getTimePersian(planDetailVOS.get(i).getTime())));
                    }
                }
            }
        }
    }

    private void checkLessonUnit(HashMap<String, List<String>> planWarnings, List<PlanDetailVO> planDetailVOS) {
        List<Long> lessonIds = new ArrayList<>();

        for (int i = 0; i < planDetailVOS.size(); i++) {
            if (!lessonIds.contains(planDetailVOS.get(i).getLessonId())) {
                lessonIds.add(planDetailVOS.get(i).getLessonId());

                Lesson lesson = lessonCRUD.getLessonById(planDetailVOS.get(i).getLessonId());
                int lessonNum = 0;

                for (int j = i; j < planDetailVOS.size(); j++) {
                    if (lesson.getId().equals(planDetailVOS.get(j).getLessonId())) {
//                        if (planDetailVOS.get(j).getWeekType().equals(PlanDetail.WeekType.HAR))
//                            lessonNum += 2;
//                        else
                            lessonNum++;
                    }
                }

                if (!lessonEqualUnit(lesson, lessonNum)) {
                    planWarnings.get("lesson_unit").add(PlanMessage.getInvalidLessonUnit(lesson.getName()));
                }
            }
        }
    }

    private boolean lessonEqualUnit(Lesson lesson, int lessonNum) {
        switch (lesson.getUnit()) {
            case ZERO:
                if (lessonNum != 0)
                    return false;
                break;
            case ONE:
            case TWO:
                if (lessonNum > 2)
                    return false;
                break;
            case THREE:
                if (lessonNum > 3)
                    return false;
                break;
            case FOUR:
                if (lessonNum > 4)
                    return false;
                break;
        }

        return true;
    }

    private void checkClassTime(HashMap<String, List<String>> planWarnings, List<PlanDetailVO> planDetailVOS) {
        for (int i = 0; i < planDetailVOS.size(); i++) {
            long classroomId = planDetailVOS.get(i).getClassroomId();

            for (int j = i + 1; j < planDetailVOS.size(); j++) {
                if (classroomId == planDetailVOS.get(j).getClassroomId()) {
                    if (planDetailVOS.get(i).getTime().equals(planDetailVOS.get(j).getTime()) &&
                            (
//                                    planDetailVOS.get(i).getWeekType().equals(PlanDetail.WeekType.HAR) ||
                                    planDetailVOS.get(i).getWeekType().equals(planDetailVOS.get(j).getWeekType()))) {
                        Classroom classroom = classroomCRUD.getClassroomById(classroomId);
                        planWarnings.get("class_time").add(PlanMessage.getInvalidClassTime(classroom.getName(), TimeService.getTimePersian(planDetailVOS.get(i).getTime())));
                    }
                }
            }
        }
    }

    private void checkLessonTeacher(HashMap<String, List<String>> planWarnings, List<PlanDetailVO> planDetailVOS) {
        for (int i = 0; i < planDetailVOS.size(); i++) {
            long lessonId = planDetailVOS.get(i).getLessonId();
            long teacherId = planDetailVOS.get(i).getTeacherId();

            for (int j = i + 1; j < planDetailVOS.size(); j++) {
                if ((lessonId == planDetailVOS.get(j).getLessonId() && teacherId != planDetailVOS.get(j).getTeacherId()) ||
                        (lessonId != planDetailVOS.get(j).getLessonId() && teacherId == planDetailVOS.get(j).getTeacherId())) {

                    Lesson lesson = lessonCRUD.getLessonById(lessonId);
                    planWarnings.get("lesson_teacher").add(PlanMessage.getInvalidLessonTeacher(lesson.getName()));
                }
            }
        }
    }
}
