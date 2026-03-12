package com.furp.service;

import com.furp.DTO.NoticeCreateDTO;
import com.furp.VO.NoticeAdminVO;
import com.furp.response.PageResult;

public interface AdminNoticeService {
    PageResult<NoticeAdminVO> listNotices(String status, String keyword, int page, int size);
    NoticeAdminVO createNotice(NoticeCreateDTO dto, Integer creatorId);
    void updateNotice(Integer id, NoticeCreateDTO dto);
    void deleteNotice(Integer id);
    NoticeAdminVO sendNotice(Integer id);
}
