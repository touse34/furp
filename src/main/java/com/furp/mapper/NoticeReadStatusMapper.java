package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.entity.NoticeReadStatus;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface NoticeReadStatusMapper extends BaseMapper<NoticeReadStatus>{

    /**
     * 插入一条已读记录。
     * 使用 INSERT IGNORE 来优雅地处理唯一键冲突：
     * 如果记录已存在，则什么都不做；如果不存在，则插入。
     * 这样可以防止同一个用户重复标记同一条通知为已读而导致程序报错。
     *
     * @param noticeId 通知ID
     * @param phdId    博士生ID
     * @return 影响的行数 (插入成功为1, 已存在为0)
     */
    @Insert("INSERT IGNORE INTO notice_read_status (notice_id, phd_id) VALUES (#{noticeId}, #{phdId})")
    int markAsReadPhd(@Param("noticeId") Integer noticeId, @Param("phdId") Integer phdId);

    @Insert("INSERT IGNORE INTO notice_read_status (notice_id, teacher_id) VALUES (#{noticeId}, #{teacherId})")
    int markAsReadTeacher(@Param("noticeId") Integer noticeId, @Param("teacherId") Integer teacherId);

    @Insert("INSERT INTO notice_read_status (notice_id, phd_id) " +
            "select n.id, #{phdId} from notices n " +
            "left join notice_read_status nrs on n.id = nrs.notice_id and nrs.phd_id = #{phdId} " +
            "where nrs.id is null")
    int markAllAsReadPhd(@Param("phdId") Integer phdId);

    @Insert("INSERT INTO notice_read_status (notice_id, teacher_Id) " +
            "select n.id, #{phdId} from notices n " +
            "left join notice_read_status nrs on n.id = nrs.notice_id and nrs.phd_id = #{teacherId)} " +
            "where nrs.id is null")
    int markAllAsReadTeacher(@Param("teacherId") Integer teacherId);



}
