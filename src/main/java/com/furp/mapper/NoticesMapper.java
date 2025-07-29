package com.furp.mapper;

import com.furp.VO.NoticesVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NoticesMapper {
    @Select("SELECT " +
            "    n.id, " +
            "    n.title, " +
            "    n.content, " +
            "    n.create_time AS time, " +
            "    n.type, " +
            "    (nrs.id IS NOT NULL) AS `read` " + // <-- 核心逻辑在这里
            "FROM " +
            "    notices n " +
            "LEFT JOIN " +
            "    notice_read_status nrs ON n.id = nrs.notice_id AND nrs.phd_id = #{phdId} " +
            "ORDER BY " +
            "    n.create_time DESC")
    List<NoticesVO> findAllWithReadStatusByPhdId(@Param("phdId") Integer phdId);
}
