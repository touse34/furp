package com.furp.service;

import com.furp.VO.NoticesVO;
import com.furp.response.PageResult;

import java.util.List;

public interface NoticesService {
    /**
     * 获取所有通知
     * @return 通知列表
     */
    public PageResult<NoticesVO> getNoticeList(int pageNum, int pageSize, Integer phdId);

    boolean markNoticeAsRead(Integer noticeId, Integer phdId);

}


