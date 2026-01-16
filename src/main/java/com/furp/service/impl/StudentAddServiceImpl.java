package com.furp.service.impl;

import com.alibaba.excel.EasyExcel;
import com.furp.DTO.StudentImportDTO;
import com.furp.entity.Phd;
import com.furp.entity.Teacher;
import com.furp.entity.User;
import com.furp.mapper.PhdMapper;
import com.furp.mapper.SupervisorMapper;
import com.furp.mapper.TeacherMapper;
import com.furp.mapper.UserMapper;
import com.furp.service.StudentAddService;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
@Slf4j
@Service
public class StudentAddServiceImpl implements StudentAddService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PhdMapper phdMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private SupervisorMapper supervisorMapper;

    private static final String DEFAULT_PASSWORD = "123456";
    /**
     * 批量导入学生 (Excel)
     *
     * @param file 前端上传的Excel文件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchImportStudents(MultipartFile file) {
        try{
            List<StudentImportDTO> dtoList = EasyExcel.read(file.getInputStream())
                    .head(StudentImportDTO.class)
                    .sheet()
                    .doReadSync();

            if (dtoList.isEmpty()){
                throw new RuntimeException("Excel文件为空或格式不正确");
            }

            for (StudentImportDTO dto : dtoList){
                if (checkStudentExist(dto.getStudentId())){
                    log.warn("学号{}已经存在，跳过导入",dto.getStudentId());
                    continue;
                }
                saveStudentData(dto);
            }
        } catch (IOException e){
            log.error("Excel导入失败",e);
            throw new RuntimeException("Excel读取失败："+e.getMessage());
        }
    }

    /**
     * 手动录入单个学生
     *
     * @param studentDTO 前端表单提交的学生信息
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void addOneStudent(StudentImportDTO studentDTO) {
        if (checkStudentExist(studentDTO.getStudentId())){
            throw new RuntimeException("学号"+studentDTO.getStudentId()+"已存在");
        }
        saveStudentData(studentDTO);
    }

    private void saveStudentData(StudentImportDTO dto){
        //1.insert user
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRoleId(2);
        user.setStatus("active");
        user.setCreateTime(LocalDateTime.now());

        String hashedPassword = BCrypt.hashpw(DEFAULT_PASSWORD,BCrypt.gensalt(12));
        user.setPassword(hashedPassword);

        userMapper.insert(user);

        // 🛑【调试点 1】看这里打印的是不是 null
        System.out.println(">>> 调试信息: User插入后，ID = " + user.getId());

        if (user.getId() == null) {
            throw new RuntimeException("严重错误：User插入成功但没有回填ID，请检查Entity注解！");
        }

        //insert PHD table
        Phd phd = new Phd();
        phd.setUserId(user.getId());
        phd.setStudentId(dto.getStudentId());
        phd.setName(dto.getName());

        // ✅ 处理 "2023/9/9" 格式的日期
        if (StringUtils.hasText(dto.getEnrollmentDate())) {
            try {
                // 定义格式器：yyyy/M/d 可以匹配 2023/9/9，也可以匹配 2023/12/12
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");

                // 解析为 LocalDate
                LocalDate date = LocalDate.parse(dto.getEnrollmentDate(), formatter);

                // 转为 LocalDateTime (补上 00:00:00) 存入数据库
                phd.setEnrollmentDate(date);

            } catch (Exception e) {
                log.error("日期解析失败: " + dto.getEnrollmentDate(), e);
                // 解析失败时的兜底：使用当前时间，或者抛出异常提示用户
                phd.setEnrollmentDate(LocalDate.now());
            }
        } else {
            // 没填日期，默认给当前时间
            phd.setEnrollmentDate(LocalDate.now());
        }

        phdMapper.insert(phd);

        // 🛑【调试点 2】看代码有没有运行到这里
        System.out.println(">>> 调试信息: Phd 插入完成，Phd ID = " + phd.getId());

        // 3.1 处理主导师 (Lead Supervisor) -> is_lead = true (1)
        linkSupervisorByName(phd.getId(), dto.getLeadSupervisor(), true);

        // 3.2 处理其他导师 -> is_lead = false (0)
        linkSupervisorByName(phd.getId(), dto.getSecondSupervisor(), false);
        linkSupervisorByName(phd.getId(), dto.getThirdSupervisor(), false);
        linkSupervisorByName(phd.getId(), dto.getFourthSupervisor(), false);


    }



    /**
     * 辅助方法：根据名字查找导师并插入 supervisor 表
     * @param phdId 刚刚生成的博士生ID
     * @param teacherName Excel里的导师名字
     * @param isLead 是否是主导师
     */
    private void linkSupervisorByName(Integer phdId, String teacherName, boolean isLead) {
        // 1. 如果名字为空，直接返回，不处理
        if (!StringUtils.hasText(teacherName)) {
            return;
        }

        // 2. 去数据库查找这个名字对应的 Teacher
        // ⚠️ 注意：这里假设 TeacherMapper 有 findByName 方法 (下面会教你写)
        Teacher teacher = teacherMapper.findByName(teacherName.trim());

        if (teacher != null) {
            // 3. 插入关联表 (对应你截图的表结构)
            // MyBatis 会自动把 boolean true 转为 1, false 转为 0
            supervisorMapper.insert(phdId, teacher.getId(), isLead);
        } else {
            // 如果找不到老师（比如Excel名字写错了），记录日志
            log.warn("导入警告：系统里找不到名字为 '{}' 的导师，无法建立关联", teacherName);
        }
    }

    /**
     * 辅助方法：检查学号是否存在
     * 使用 MyBatis-Plus 的 QueryWrapper 自动生成查询
     */
    private boolean checkStudentExist(String studentId) {
        // 如果学号为空，直接视为不存在
        if (!StringUtils.hasText(studentId)) {
            return false;
        }

        // 创建查询条件：SELECT count(*) FROM phd WHERE student_id = ?
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Phd> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();

        // 注意：这里的 "student_id" 是数据库表里的字段名
        // 如果你数据库里叫 "student_id"，就填 "student_id"
        queryWrapper.eq("student_id", studentId);

        // selectCount 是 MyBatis-Plus 自带的方法
        return phdMapper.selectCount(queryWrapper) > 0;
    }
}
