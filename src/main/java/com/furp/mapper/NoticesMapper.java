package com.furp.mapper;

import com.furp.VO.NoticesVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NoticesMapper {
    @Select("SELECT DISTINCT" +
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

    @Select("select distinct n.id, n.title, n.content, n.create_time as time, n.type, (nrs.id is not null) as `read` " +
            "from notices n " +
            "left join notice_read_status nrs on n.id = nrs.notice_id and nrs.teacher_id = #{teacherId} " +
            "order by n.create_time desc")
    List<NoticesVO> findAllWithReadStatusByTeacherId(@Param("teacherId") Integer teacherId);

    @Select("select count(*) from notices left join notice_read_status nrs on notices.id = nrs.notice_id and nrs.teacher_id = #{teacherId} where teacher_id IS NULL")
    int countUnreadNotices(@Param("teacherId") Integer teacherId);
}
