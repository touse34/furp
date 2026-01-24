package com.furp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.furp.VO.SkillSelectionVO;
import com.furp.entity.Phd;
import com.furp.entity.PhdSkill;
import com.furp.entity.Skill;
import com.furp.mapper.PhdMapper;
import com.furp.mapper.PhdSkillMapper;
import com.furp.mapper.SkillMapper;
import com.furp.service.PhdSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhdSkillImpl implements PhdSkillService {
    @Autowired
    private PhdSkillMapper phdSkillMapper;
    @Autowired
    private PhdMapper phdMapper;
    @Autowired
    private SkillMapper skillMapper;

    @Override
    @Transactional
    public int updatestudentSkill(Integer userId, List<Integer> newSkillId) {
        Phd phd = phdMapper.selectPhdByUserId(userId);
        if (phd == null) {
            throw new RuntimeException("学生未找到或未关联PhD信息");
        }
        Integer phdId = phd.getId();
        // 2. 删除该学生所有旧的研究方向
        phdSkillMapper.deletePhdSkillsByPhdId(phdId);
        // 3. 插入新的研究方向
        int count = phdSkillMapper.insertPhdSkill(phdId, newSkillId);

        if (count <= 0) {
            throw new RuntimeException("更新学生研究方向失败");
        }
        return count;
    }

    @Override
    public String getSkillNameById(Integer skillId) {
        return phdSkillMapper.getSkillNameById(skillId);
    }

    @Override
    public List<SkillSelectionVO> getSkillSelectionForPhd(Integer phdId) {
        // 1. 创建条件构造器
        LambdaQueryWrapper<Skill> queryWrapper = new LambdaQueryWrapper<>();
        // 2. 添加过滤条件：只查 status = 'approved'
        queryWrapper.eq(Skill::getStatus, "approved");
        // 3. (可选) 按名称排序，让前端显示更整齐
        queryWrapper.orderByAsc(Skill::getSkillName);

        // 4. 【关键修改】把 queryWrapper 传进去，不要传 null
        List<Skill> approvedSkills = skillMapper.selectList(queryWrapper);

        // ---------------- 下面的逻辑保持不变 ----------------

        // 查询该 PhD 已选的技能 ID
        Set<Integer> selectedSkillIds = new HashSet<>(skillMapper.selectSkillIdByPhdId(phdId));

        // 组装 VO
        return approvedSkills.stream()
                .map(skill -> new SkillSelectionVO(
                        skill.getId(),
                        skill.getSkillName(),
                        selectedSkillIds.contains(skill.getId())
                ))
                .collect(Collectors.toList());
    }

    public Set<Integer> findPhdSkillsById(Integer phdId){
        return phdSkillMapper.selectSkillByPhdId(phdId).stream().map(phdSkill -> phdSkill.getSkillId()).collect(Collectors.toSet());
    }


}
