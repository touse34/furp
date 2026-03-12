package com.furp.service.impl;

import com.furp.DTO.NoticeCreateDTO;
import com.furp.VO.NoticeAdminVO;
import com.furp.entity.Notices;
import com.furp.mapper.NoticesMapper;
import com.furp.response.PageResult;
import com.furp.service.AdminNoticeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminNoticeServiceImpl implements AdminNoticeService {

    @Autowired
    private NoticesMapper noticesMapper;

    @Override
    public PageResult<NoticeAdminVO> listNotices(String status, String keyword, int page, int size) {
        PageHelper.startPage(page, size);
        List<NoticeAdminVO> list = noticesMapper.findForAdmin(status, keyword);
        Page<NoticeAdminVO> p = (Page<NoticeAdminVO>) list;
        return new PageResult<>(p.getResult(), p.getTotal(), p.getPageNum(), p.getPageSize());
    }

    @Override
    public NoticeAdminVO createNotice(NoticeCreateDTO dto, Integer creatorId) {
        Notices notice = new Notices();
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setRecipientType(dto.getRecipientType() != null ? dto.getRecipientType() : "all");
        notice.setCreatorId(creatorId);
        notice.setCreateTime(LocalDateTime.now());
        notice.setType("normal");

        if ("immediate".equalsIgnoreCase(dto.getSendType())) {
            notice.setStatus("sent");
            notice.setSendTime(LocalDateTime.now());
        } else if ("scheduled".equalsIgnoreCase(dto.getSendType()) && dto.getScheduleTime() != null) {
            notice.setStatus("scheduled");
            notice.setScheduleTime(LocalDateTime.parse(dto.getScheduleTime()));
        } else {
            notice.setStatus("draft");
        }

        noticesMapper.insert(notice);

        NoticeAdminVO vo = new NoticeAdminVO();
        vo.setId(notice.getId());
        vo.setTitle(notice.getTitle());
        vo.setContent(notice.getContent());
        vo.setRecipientType(notice.getRecipientType());
        vo.setStatus(notice.getStatus());
        vo.setCreateTime(notice.getCreateTime());
        vo.setSendTime(notice.getSendTime());
        vo.setScheduleTime(notice.getScheduleTime());
        return vo;
    }

    @Override
    public void updateNotice(Integer id, NoticeCreateDTO dto) {
        Notices notice = noticesMapper.selectById(id);
        if (notice == null) {
            throw new RuntimeException("Notice not found with id: " + id);
        }
        if (dto.getTitle() != null) notice.setTitle(dto.getTitle());
        if (dto.getContent() != null) notice.setContent(dto.getContent());
        if (dto.getRecipientType() != null) notice.setRecipientType(dto.getRecipientType());

        if ("immediate".equalsIgnoreCase(dto.getSendType())) {
            notice.setStatus("sent");
            notice.setSendTime(LocalDateTime.now());
        } else if ("scheduled".equalsIgnoreCase(dto.getSendType()) && dto.getScheduleTime() != null) {
            notice.setStatus("scheduled");
            notice.setScheduleTime(LocalDateTime.parse(dto.getScheduleTime()));
        } else if ("draft".equalsIgnoreCase(dto.getSendType())) {
            notice.setStatus("draft");
        }

        noticesMapper.updateById(notice);
    }

    @Override
    public void deleteNotice(Integer id) {
        Notices notice = noticesMapper.selectById(id);
        if (notice == null) {
            throw new RuntimeException("Notice not found with id: " + id);
        }
        noticesMapper.deleteById(id);
    }

    @Override
    public NoticeAdminVO sendNotice(Integer id) {
        Notices notice = noticesMapper.selectById(id);
        if (notice == null) {
            throw new RuntimeException("Notice not found with id: " + id);
        }
        notice.setStatus("sent");
        notice.setSendTime(LocalDateTime.now());
        noticesMapper.updateById(notice);

        NoticeAdminVO vo = new NoticeAdminVO();
        vo.setId(notice.getId());
        vo.setStatus("sent");
        vo.setSendTime(notice.getSendTime());
        return vo;
    }
}
