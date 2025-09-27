package com.furp.service.impl;

import com.furp.DTO.*;
import com.furp.VO.UserAddResponseVO;
import com.furp.VO.UserVO;
import com.furp.entity.Phd;
import com.furp.entity.Teacher;
import com.furp.entity.User;
import com.furp.exception.AccountNotFoundException;
import com.furp.exception.PasswordErrorException;
import com.furp.mapper.*;
import com.furp.response.PageResult;
import com.furp.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PhdMapper phdMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private SupervisorMapper supervisorMapper;
    @Autowired
    private SkillMapper skillMapper;
    @Autowired
    private PhdSkillMapper phdSkillMapper;
    @Autowired
    private TeacherSkillMapper teacherSkillMapper;
    @Autowired
    private AnnualReviewMapper annualReviewMapper;
    @Autowired
    private AvailableTimeMapper availableTimeMapper;
    @Autowired
    private ReviewAssessorMapper reviewAssessorMapper;
    @Autowired
    private SchedulesMapper schedulesMapper;

    public User login(LoginDTO loginDTO){
        String username = loginDTO.getUsername();
        String rawPassword = loginDTO.getPassword();

        User user = userMapper.getUserByName(username);

        if(user == null){
            //账号不存在
            throw new AccountNotFoundException("用户名不存在");
        }
        if (!BCrypt.checkpw(rawPassword, user.getPassword())) {
            // 密码错误
            throw new PasswordErrorException("密码错误");
        }

        return user;

    }

    @Override
    public User getById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
        // 1. 启动分页
        PageHelper.startPage(userPageQueryDTO.getPage(), userPageQueryDTO.getSize());

        // 2. 直接调用 UserMapper 的 pageQuery 方法
        // XML 中的动态 SQL 会自动处理 type -> role_id 的逻辑
        Page<UserVO> page = userMapper.pageQuery(userPageQueryDTO);

        // 3. 对查询结果进行二次加工 (这是修改的重点)
        if (page != null && !page.getResult().isEmpty()) {
            for (UserVO userVO : page.getResult()) {
                List<String> researchAreas;

                // 如果是博士生 (roleId=2)，则查询其关联数据
                if (userVO.getRoleId() != null && userVO.getRoleId() == 2 && userVO.getPhdId() != null) {

                    // --- 修改点 1：处理 supervisors 列表 ---
                    // a. 从 Mapper 获取原始的 Integer 类型的 ID 列表
                    List<Integer> supervisorIdsFromDb = userMapper.getSupervisorIdsByPhdId(userVO.getPhdId());
                    if (supervisorIdsFromDb != null && !supervisorIdsFromDb.isEmpty()) {
                        // b. 在 Service 层进行转换，将 List<Integer> 转换为 List<String>，并添加 "T" 前缀和补零
                        List<String> formattedSupervisorIds = supervisorIdsFromDb.stream()
                                .map(id -> "T" + String.format("%03d", id)) // 例如: 1 -> "T001"
                                .collect(Collectors.toList());
                        // c. 将转换后的 List<String> 设置到 VO 对象中
                        userVO.setSupervisors(formattedSupervisorIds);

                    }

                    // --- 新增点：处理 mainSupervisor ---
                    Integer mainSupervisorId = userMapper.getMainSupervisorIdByPhdId(userVO.getPhdId());
                    if (mainSupervisorId != null) {
                        // 同样进行格式化处理
                        userVO.setMainSupervisor("T" + String.format("%03d", mainSupervisorId));
                    }


                    // 查询博士生的研究领域
                    researchAreas = userMapper.getResearchAreaNamesByPhdId(userVO.getPhdId());
                    userVO.setResearchAreas(researchAreas);
                    // 【新增】如果是教师 (roleId=1)
                } else if (userVO.getRoleId() != null && userVO.getRoleId() == 1 && userVO.getTeacherId() != null) {
                    // 查询教师的研究领域
                    researchAreas = userMapper.getResearchAreaNamesByTeacherId(userVO.getTeacherId());
                    userVO.setResearchAreas(researchAreas);
                }
            }
        }

        // 4. 封装 PageResult 对象返回
        return new PageResult<>(
                page.getResult(),
                page.getTotal(),
                page.getPageNum(),
                page.getPageSize()
        );
    }

    @Override
    public void disableUserById(Integer userId) {
        // Step 1: Use the mapper to find the user by their ID.
        User user = userMapper.selectById(userId);

        // Step 2: Check if the result is null.
        if (user == null) {
            // If the user is null, it means no user with that ID was found in the database.
            // Throw an exception to stop the process immediately and signal an error.
            throw new RuntimeException("User not found with ID: " + userId);
        }
        // Now it's safe to proceed with the deletion.
        userMapper.softDeleteById(userId);

    }

    @Override
    public void enableUser(Integer userId) {
        // Step 1: Use the mapper to find the user by their ID.
        User user = userMapper.selectById(userId);

        // Step 2: Check if the result is null.
        if (user == null) {
            // If the user is null, it means no user with that ID was found in the database.
            // Throw an exception to stop the process immediately and signal an error.
            throw new RuntimeException("User not found with ID: " + userId);
        }

        // Now it's safe to proceed with the deletion.
        userMapper.enableById(userId);
    }

    @Override
    @Transactional
    public void deleteUserById(Integer userId) {
        // 1. 首先，检查用户是否存在，并获取其角色ID
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        Integer roleId = user.getRoleId();

        // 2. 根据角色，执行不同的级联删除逻辑
        if (roleId == 2) { // 如果是博士生 (phd)
            // 2.1 找到 phd 表的记录，以获取其主键 phd.id
            Phd phd = phdMapper.findByUserId(userId);
            if (phd != null) {
                Integer phdId = phd.getId();

                // 2.1 Find all annual_review IDs associated with this PhD
                List<Integer> reviewIds = annualReviewMapper.findIdsByPhdId(phdId);

                if (reviewIds != null && !reviewIds.isEmpty()) {
                    // 2.2 Delete the "grandchildren" first: records in schedules and review_assessor
                    reviewAssessorMapper.deleteByAnnualReviewIds(reviewIds);
                    schedulesMapper.deleteByAnnualReviewIds(reviewIds);
                }
                // 2.2 删除最底层的关联：supervisor 和 phd_skill
                supervisorMapper.deleteByPhdId(phdId);
                phdSkillMapper.deleteByPhdId(phdId);
                annualReviewMapper.deleteByPhdId(phdId);

                // 2.3 删除 phd 表自身的记录
                phdMapper.deleteById(phdId);
            }
        } else if (roleId == 1) { // 如果是教师 (teacher)
            // 2.1 找到 teacher 表的记录
            Teacher teacher = teacherMapper.findByUserId(userId);
            if (teacher != null) {
                Integer teacherId = teacher.getId();

                // 2.2 删除最底层的关联：supervisor 和 teacher_skill
                supervisorMapper.deleteByTeacherId(teacherId);
                teacherSkillMapper.deleteByTeacherId(teacherId);
                availableTimeMapper.deleteByTeacherId(teacherId);
                reviewAssessorMapper.deleteByTeacherId(teacherId);
                schedulesMapper.deleteByTeacherId(teacherId);
                // 2.3 删除 teacher 表自身的记录
                teacherMapper.deleteById(teacherId);
            }
        }

        // 3. 最后，删除 user 表中的主记录
        userMapper.deleteById(userId);

        log.info("用户及其所有关联数据已成功硬删除, userId: {}", userId);
    }

    @Override
    @Transactional // Ensures all database operations below either succeed together or fail together.
    public UserAddResponseVO addUser(UserAddDTO userAddDTO) {

        UserAddResponseVO responseVO = new UserAddResponseVO();

        // Step 1: Create and insert the common user record
        User user = new User();
        user.setName(userAddDTO.getName());
        user.setEmail(userAddDTO.getEmail());


        // For security, never store plain text passwords. Here we use MD5 as a basic example.
        // In a real project, BCrypt is recommended.
        //user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        // 使用 BCrypt 生成一个带盐的、高强度的哈希值
        String hashedPassword = BCrypt.hashpw("123456", BCrypt.gensalt(12));
        user.setPassword(hashedPassword);
        user.setStatus("active"); // Set a default status
        user.setCreateTime(LocalDateTime.now()); // Set creation time

        // Step 2: Handle type-specific logic
        if ("phd".equals(userAddDTO.getType())) {
            // --- PhD Logic ---
            user.setRoleId(2); // Set role_id for PhD
            userMapper.insert(user); // After this, user.getId() will be populated with the new ID

            Phd phd = new Phd();
            phd.setUserId(user.getId());
            //phd.setStudentId(userAddDTO.getId()); // The "id" from the DTO
            phd.setEnrollmentDate(userAddDTO.getEnrollmentDate());
            phd.setStudentId(userAddDTO.getStudentId());
            phd.setName(userAddDTO.getName());
            phdMapper.insert(phd); // After this, phd.getId() will be populated

            // Handle supervisor relationships
            List<String> supervisorIdStrings = userAddDTO.getSupervisors();
            if (supervisorIdStrings != null && !supervisorIdStrings.isEmpty()) {
                for (String idStr : supervisorIdStrings) {
                    Integer teacherId = Integer.parseInt(idStr.replace("T", ""));
                    boolean isLead = idStr.equals(userAddDTO.getMainSupervisor());
                    supervisorMapper.insert(phd.getId(), teacherId, isLead);
                }
            }

            // Handle research area (skill) relationships
            List<String> researchAreaNames = userAddDTO.getResearchAreas();
            if (researchAreaNames != null && !researchAreaNames.isEmpty()) {
                for (String areaName : researchAreaNames) {
                    Integer skillId = skillMapper.getIdByName(areaName);
                    if (skillId != null) {
                        phdSkillMapper.insert(phd.getId(), skillId);
                    }
                }
            }

            // Format the response
            responseVO.setId("PhD" + String.format("%03d", phd.getId()));

        } else if ("teacher".equals(userAddDTO.getType())) {
            // --- Teacher Logic ---
            user.setRoleId(1); // Set role_id for Teacher
            userMapper.insert(user); // After this, user.getId() will be populated

            Teacher teacher = new Teacher();
            teacher.setUserId(user.getId());
            teacher.setName(userAddDTO.getName());
            // You might want to save the business ID ("T2021003") to a specific column
            // teacher.setEmployeeId(userAddDTO.getId());
            teacherMapper.insert(teacher); // After this, teacher.getId() will be populated

            // Handle research area (skill) relationships
            List<String> researchAreaNames = userAddDTO.getResearchAreas();
            if (researchAreaNames != null && !researchAreaNames.isEmpty()) {
                for (String areaName : researchAreaNames) {
                    Integer skillId = skillMapper.getIdByName(areaName);
                    if (skillId != null) {
                        teacherSkillMapper.insertskill(teacher.getId(), skillId);
                    }
                }
            }

            // Format the response
            responseVO.setId("T" + String.format("%03d", teacher.getId()));

        } else {
            // Handle unknown type
            throw new IllegalArgumentException("Unsupported user type: " + userAddDTO.getType());
        }

        // Step 3: Set common response fields and return
        responseVO.setCreateTime(user.getCreateTime());
        return responseVO;

    }

    @Override
    public DashboardStatsDTO getStats() {
        Long totalPhds = userMapper.countTotalPhds();
        Long totalTeachers = userMapper.countTotalTeachers();
        Long confirmedTeachers = userMapper.countConfirmedTeachers();
        Long pendingTeachers = userMapper.countPendingTeachers();
        Long totalSchedules = userMapper.countTotalSchedules();
        Long totalTimeSlots = userMapper.countTotalTimeSlots();
        Long pendingResearchAreaApprovals = userMapper.countPendingResearchAreaApprovals();

        // 更新 DTO 的构造函数调用
        DashboardStatsDTO statsDTO = new DashboardStatsDTO(
                totalPhds,
                totalTeachers,
                confirmedTeachers,
                pendingTeachers,
                totalSchedules,
                totalTimeSlots,
                pendingResearchAreaApprovals
        );

        return statsDTO;


    }

    @Override
    public List<UserInfo> findByRole(Integer roleId) {
        return userMapper.findByRole(roleId);
    }

    @Override
    public List<UserInfo> findAll() {
        return userMapper.findAll();
    }
}
