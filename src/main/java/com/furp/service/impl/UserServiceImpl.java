package com.furp.service.impl;

import com.furp.DTO.DashboardStatsDTO;
import com.furp.DTO.LoginDTO;
import com.furp.DTO.UserInfo;
import com.furp.DTO.UserPageQueryDTO;
import com.furp.VO.UserVO;
import com.furp.entity.User;
import com.furp.exception.AccountNotFoundException;
import com.furp.exception.PasswordErrorException;
import com.furp.mapper.UserMapper;
import com.furp.response.PageResult;
import com.furp.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


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
