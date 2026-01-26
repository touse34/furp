package com.furp.service;

import com.furp.VO.NoticeCheckVO;
import com.furp.VO.NoticesVO;
import com.furp.response.PageResult;

public interface NoticesService {
    /**
     * 获取所有通知
     * @return 通知列表
     */
    PageResult<NoticesVO> getNoticeList(int pageNum, int pageSize, Integer Id, String role);

    boolean markNoticeAsRead(Integer noticeId, Integer phdId, String role);

    Void markAllAsRead(Integer id, String role);

    /**
     * 获取老师的未读通知
     * @return 数量
     */
    NoticeCheckVO countUnread(Integer id);

}


