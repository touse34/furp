package com.furp.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.furp.DTO.ReviewInfoVo;
import com.furp.DTO.StatusUpdateDTO;
import com.furp.DTO.TeacherImportDTO;
import com.furp.entity.Supervisor;
import com.furp.entity.Teacher;
import com.furp.entity.User;
import com.furp.exception.AccessDeniedException;
import com.furp.exception.ResourceNotFoundException;
import com.furp.mapper.*;

import com.furp.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@Service
public class TeacherImpl implements TeacherService {
    @Autowired
    private UserMapper userMapper; // 需要插入 User 表
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private SupervisorMapper supervisorMapper;
    @Autowired
    private AnnualReviewMapper annualReviewMapper;
    @Autowired
    private ReviewAssessorMapper reviewAssessorMapper;

    private static final String DEFAULT_PASSWORD = "123456";

    public List<Teacher> findAllTeacher(){
        return teacherMapper.selectAll();
    }

    public List<Teacher> findEligibleAssessors(Integer phdId){

        List<Teacher> allTeachers = teacherMapper.selectAll();

        List<Supervisor> supervisors = supervisorMapper.findSupervisorsByPhdId(phdId);

        Set<Integer> supervisorIDs = supervisors.stream()
                .map(teacher -> teacher.getId())
                .collect(Collectors.toSet());

        if(supervisorIDs.isEmpty()){
            return allTeachers;
        }

        List<Teacher> eligibleAssessors = allTeachers.stream()
                .filter(teacher -> !supervisorIDs.contains(teacher.getId()))
                .collect(Collectors.toList());

        return eligibleAssessors;



    }

    @Override
    public List<ReviewInfoVo> findReviewScheduleByTeacherId(Integer teacherId) {
        return annualReviewMapper.findScheduledReviewByTeacherId(teacherId);
    }

    @Override
    public void updateTaskStatus(Integer taskId, Integer currentTeacherId, StatusUpdateDTO dto) {
        int count = reviewAssessorMapper.isExist( currentTeacherId, taskId);
        if(count==0){
            throw new AccessDeniedException("你无权更新该任务的状态");
        }

        int affectCount = annualReviewMapper.updateReviewStatusById(taskId, dto.getStatus());
        if (affectCount == 0) {
            throw new ResourceNotFoundException("任务状态更新失败，可能是任务ID不存在");
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String extractAndImportTeachers(MultipartFile file) {
        try {
            // 1. 读取 Excel 数据
            List<TeacherImportDTO> dtoList = EasyExcel.read(file.getInputStream())
                    .head(TeacherImportDTO.class)
                    .sheet()
                    .doReadSync();

            // 2. 【关键】使用 Set 进行内存去重
            // Set 的特性是：不允许重复元素。如果add重复的，它会自动忽略。
            Set<String> uniqueTeacherNames = new HashSet<>();

            for (TeacherImportDTO dto : dtoList) {
                addIfValid(uniqueTeacherNames, dto.getAssessor1());
                addIfValid(uniqueTeacherNames, dto.getAssessor2());
                addIfValid(uniqueTeacherNames, dto.getLeadSupervisor());
                addIfValid(uniqueTeacherNames, dto.getSecondSupervisor());
                addIfValid(uniqueTeacherNames, dto.getThirdSupervisor());
                addIfValid(uniqueTeacherNames, dto.getFourthSupervisor());
            }

            int successCount = 0;
            int existCount = 0;

            // 3. 遍历去重后的名单，插入数据库
            for (String name : uniqueTeacherNames) {

                // 3.1 二次检查：数据库里是不是已经有了？(防止多次上传同一个文件)
                if (checkTeacherExist(name)) {
                    existCount++;
                    continue; // 数据库已有，跳过
                }

                // 3.2 插入 User 表 (创建账号)
                User user = new User();
                user.setName(name);
                user.setEmail(null); // Excel里没邮箱，暂时留空
                user.setRoleId(1);   // 假设 1 代表 Teacher 角色
                user.setStatus("active");
                user.setCreateTime(LocalDateTime.now());
                // 加密密码
                user.setPassword(BCrypt.hashpw(DEFAULT_PASSWORD, BCrypt.gensalt()));

                userMapper.insert(user); // 插入后 user.getId() 会有值

                // 3.3 插入 Teacher 表 (关联 User)
                Teacher teacher = new Teacher();
                teacher.setName(name);
                teacher.setUserId(user.getId()); // 关联刚才生成的账号ID

                teacherMapper.insert(teacher);

                successCount++;
            }

            return "提取完成：发现 " + uniqueTeacherNames.size() + " 个唯一导师名。新增 " + successCount + " 人，数据库已有 " + existCount + " 人。";

        } catch (IOException e) {
            log.error("提取失败", e);
            throw new RuntimeException("读取失败: " + e.getMessage());
        }
    }

    /**
     * 辅助方法：如果不为空，就加入 Set
     */
    private void addIfValid(Set<String> set, String name) {
        if (StringUtils.hasText(name)) {
            set.add(name.trim()); // 这里的 trim() 很重要，防止 "Name " 和 "Name" 被当成两个人
        }
    }

    /**
     * 辅助方法：查重
     */
    private boolean checkTeacherExist(String name) {
        QueryWrapper<Teacher> query = new QueryWrapper<>();
        query.eq("name", name);
        return teacherMapper.selectCount(query) > 0;
    }



}
