package com.furp.service.impl;


import com.furp.DTO.ResearchAreaDetail;
import com.furp.DTO.TeacherResearchAreasResponseDTO;
import com.furp.mapper.TeacherResearchAreasResponseMapper;
import com.furp.service.TeacherResearchAreasResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeacherResearchAreasResponseImpl implements TeacherResearchAreasResponseService {

    @Autowired
    private TeacherResearchAreasResponseMapper teacherResearchAreasResponseMapper;

    /**
     * 添加某个老师新的研究方向
     *
     * @param teacherId
     * @param researchAreaDetail 新的研究方向信息
     * @return 添加后的研究方向信息
     */
    @Override
    public ResearchAreaDetail addResearchArea(Integer teacherId, ResearchAreaDetail researchAreaDetail) {
        System.out.println("=== 开始添加新研究方向，teacherId: " + teacherId + ", 研究方向: " + researchAreaDetail.getName() + " ===");
        //1.补全CreatedAt
        researchAreaDetail.setCreatedAt(LocalDateTime.now());
        //2.其他字段
        String skillName = researchAreaDetail.getName();
        int rows = teacherResearchAreasResponseMapper.addResearchArea(teacherId, skillName);
        if (rows == 0) {
            throw new IllegalArgumentException("技能不存在: " + skillName);
        }

        // 2) 查 skill，拿到 id/status/createdAt/approvedAt
        ResearchAreaDetail skill = teacherResearchAreasResponseMapper.findSkillByName(skillName);
        if (skill == null) {
            throw new IllegalStateException("插入后未能查询到技能: " + skillName);
        }
        // 3) 直接把 skill 返回（其中 id/status 来自 skill 表）
        return skill;

    }

    /**
     * 删除某个老师某个研究方向
     *
     * @param teacherId
     * @param areaId
     * @return 删除后的研究方向信息
     */
    @Override
    public void deleteResearchArea(Integer teacherId, Long areaId) {
        teacherResearchAreasResponseMapper.deleteResearchArea(teacherId, areaId);


    }

    @Override
    public TeacherResearchAreasResponseDTO getTeacherResearchAreas(Integer teacherId) {
        System.out.println("=== 开始查询教师信息，teacherId: " + teacherId + " ===");

        try {
            // 1. 查询所有研究方向明细
            List<TeacherResearchAreasResponseDTO> results = teacherResearchAreasResponseMapper.getByIdResearchAreas(teacherId);

            System.out.println("=== 调试信息 ===");
            System.out.println("查询结果数量: " + (results == null ? "null" : results.size()));
            if (results == null || results.isEmpty()) {
                System.out.println("查询结果为空，返回null");
                return null;
            }

            // 2. 打印每条记录内容
            for (int i = 0; i < results.size(); i++) {
                TeacherResearchAreasResponseDTO result = results.get(i);
                System.out.println("记录 " + i + ":");
                System.out.println("  id: " + result.getId());
                System.out.println("  name: " + result.getName());
                System.out.println("  status: " + result.getStatus());
                System.out.println("  createdAt: " + result.getCreatedAt());
                System.out.println("  approvedAt: " + result.getApprovedAt());
            }

            // 3. 创建返回对象（一个老师）
            TeacherResearchAreasResponseDTO profile = new TeacherResearchAreasResponseDTO();

            // 4. 聚合所有研究方向信息
            int approvedCount = 0;
            int pendingCount = 0;

            for (TeacherResearchAreasResponseDTO result : results) {
                if (result.getId() != null && result.getName() != null) {
                    profile.addResearchAreaDetail(
                            result.getId(),
                            result.getName(),
                            result.getStatus(),
                            result.getCreatedAt(),
                            result.getApprovedAt()
                    );

                    // 计数
                    if ("approved".equalsIgnoreCase(result.getStatus())) {
                        approvedCount++;
                    } else if ("pending".equalsIgnoreCase(result.getStatus())) {
                        pendingCount++;
                    }

                    System.out.println("成功添加研究方向");
                } else {
                    System.out.println("研究方向数据为空，跳过");
                }
            }

            profile.setApprovedCount(approvedCount);
            profile.setPendingCount(pendingCount);
            profile.setTotalCount(results.size());

            System.out.println("最终研究方向数量: " + profile.getResearchAreaDetail().size());
            System.out.println("=== 查询完成 ===");
            return profile;

        } catch (Exception e) {
            System.out.println("查询过程中发生异常: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

}
