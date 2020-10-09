package planning.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import planning.model.*;
import planning.modelVO.PlanDetailVO;
import planning.modelVO.PlanVO;
import planning.repository.*;

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
    private final TeacherTimeCRUD teacherTimeCRUD;
    private final LessonGroupCRUD lessonGroupCRUD;

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

    public Plan copyPlan(Plan plan, String name) {
        Plan copyPlan = new Plan();
        copyPlan.setName(name);
        copyPlan.setTimeType(plan.getTimeType());
        planCRUD.saveAndFlush(copyPlan);

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
}
