package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.DTO.ResearchAreaDetail;
import com.furp.DTO.ResearchAreaPageQueryDTO;
import com.furp.VO.ResearchAreasVO;
import com.furp.DTO.PendingResearchAreaQueryDTO;
import com.furp.DTO.ResearchAreaPageQueryDTO;
import com.furp.VO.PendingResearchAreaVO;
import com.furp.VO.ResearchAreasVO;
import com.furp.entity.Skill;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SkillMapper extends BaseMapper<Skill> {

    @Select("select s.id from skill s " +
            "join phd_skill p on p.skill_id = s.id " +
            "where phd_id = #{phdId}")
    List<Integer> selectSkillIdByPhdId(Integer phdId);

    @Select("SELECT id FROM skill WHERE skill_name = #{name}")
    Integer getIdByName(String name);


    List<PendingResearchAreaVO> findPending(PendingResearchAreaQueryDTO queryDTO);
    Page<ResearchAreasVO> pageQuery(ResearchAreaPageQueryDTO queryDTO);

    @Select("SELECT * FROM skill WHERE skill_name = #{name}")
    ResearchAreaDetail findByName(String name);

    /**
     * 【新增】自定义的 insert 方法，用于接收 DTO 对象
     * @param detailDTO 从 Service 层传递过来的 DTO 对象
     * @return 影响的行数
     */
    int insertResearchAreaDetail(ResearchAreaDetail detailDTO);
    @Select("SELECT * FROM skill WHERE skill_name = #{name}")
    Skill findIfExistByName(String name);
}
