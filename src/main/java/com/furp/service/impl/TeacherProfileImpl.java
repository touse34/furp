package com.furp.service.impl;

import com.furp.DTO.TeacherProfileDTO;
import com.furp.mapper.TeacherProfileMapper;
import com.furp.service.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeacherProfileImpl implements TeacherProfileService {
    @Autowired
    private TeacherProfileMapper teacherProfileMapper;

    @Override
    public void update(TeacherProfileDTO teacherProfileDTO) {
        // 设置更新时间
        teacherProfileDTO.setUpdateTime(LocalDateTime.now());

        // 参数验证
        if (teacherProfileDTO.getUserId() == null) {
            throw new IllegalArgumentException("教师userId不能为空");
        }

        System.out.println("准备更新教师信息：" + teacherProfileDTO);

        // 执行更新
        teacherProfileMapper.update(teacherProfileDTO);
    }

    @Override
    public TeacherProfileDTO getById(Integer userId) {
        System.out.println("=== 开始查询教师信息，teacherId: " + userId + " ===");

        try {
            // 调用新的查询方法，支持多个研究方向
            List<TeacherProfileDTO> results = teacherProfileMapper.getByIdWithResearchAreas(userId);

            System.out.println("=== 调试信息 ===");
            System.out.println("查询结果数量: " + (results == null ? "null" : results.size()));

            if (results == null || results.isEmpty()) {
                System.out.println("查询结果为空，返回null");
                return null;
            }

            // 打印每条记录的详细信息
            for (int i = 0; i < results.size(); i++) {
                TeacherProfileDTO result = results.get(i);
                System.out.println("记录 " + i + ":");
                System.out.println("  userId: " + result.getUserId());
                System.out.println("  name: " + result.getName());
                System.out.println("  researchAreaId: " + result.getResearchAreaId());
                System.out.println("  researchAreaName: " + result.getResearchAreaName());
                System.out.println("  createdAt: " + result.getCreatedAt());
                System.out.println("  status: " + result.getStatus());
            }

            // 创建结果对象
            TeacherProfileDTO profile = new TeacherProfileDTO();
            TeacherProfileDTO firstResult = results.get(0);

            // 设置基本信息
            profile.setUserId(firstResult.getUserId());
            profile.setName(firstResult.getName());
            profile.setLastLoginAt(firstResult.getLastLoginAt());
            profile.setUpdateTime(firstResult.getUpdateTime());

            System.out.println("开始聚合研究方向...");

            // 聚合所有研究方向
            for (TeacherProfileDTO result : results) {
                System.out.println("处理研究方向: id=" + result.getResearchAreaId() + ", name=" + result.getResearchAreaName());
                if (result.getResearchAreaId() != null && result.getResearchAreaName() != null) {
                    profile.addResearchArea(
                            result.getResearchAreaId(),
                            result.getResearchAreaName(),
                            result.getStatus(),
                            result.getCreatedAt()
                    );
                    System.out.println("成功添加研究方向");
                } else {
                    System.out.println("研究方向数据为空，跳过");
                }
            }

            System.out.println("最终研究方向数量: " + profile.getResearchAreas().size());
            System.out.println("=== 查询完成 ===");
            return profile;

        } catch (Exception e) {
            System.out.println("查询过程中发生异常: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}