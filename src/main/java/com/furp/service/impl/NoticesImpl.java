package com.furp.service.impl;

import com.furp.DTO.ReviewInfoVo;
import com.furp.VO.NoticesVO;
import com.furp.mapper.NoticeReadStatusMapper;
import com.furp.mapper.NoticesMapper;
import com.furp.response.PageResult;
import com.furp.service.NoticesService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class NoticesImpl implements NoticesService {

    @Autowired
    private NoticesMapper noticesMapper;

    @Autowired
    private NoticeReadStatusMapper noticeReadStatusMapper;

    @Override
    public PageResult<NoticesVO> getNoticeList(int pageNum, int pageSize, Integer Id, String role) {
        PageHelper.startPage(pageNum, pageSize);
        List<NoticesVO> notices;
        if (role.equals("phd")) {
            notices = noticesMapper.findAllWithReadStatusByPhdId(Id);
        }else if(role.equals("teacher")){
            notices = noticesMapper.findAllWithReadStatusByTeacherId(Id);
        }else{
            notices = Collections.emptyList();
        }
        Page<NoticesVO> page = (Page<NoticesVO>) notices;

        PageResult<NoticesVO> pageResult = new PageResult<>(
                page.getResult(),
                page.getTotal(),
                page.getPageNum(),
                page.getPageSize());


        return pageResult;


    }

    @Override
    public boolean markNoticeAsRead(Integer noticeId, Integer phdId) {
        // 调用Mapper方法插入记录
        int rowsAffected = noticeReadStatusMapper.markAsRead(noticeId, phdId);
        // 如果影响的行数大于0，说明是新插入的，操作成功
        // 即使原来已存在（返回0），从业务角度看也算是“已读”状态，所以可以直接返回true
        // 或者您可以根据 rowsAffected 的值返回更精确的状态
        return true;
    }
}
