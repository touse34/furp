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
    int markAsRead(@Param("noticeId") Integer noticeId, @Param("phdId") Integer phdId);
}
