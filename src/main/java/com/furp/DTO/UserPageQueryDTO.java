package com.furp.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPageQueryDTO implements Serializable {
    /**
     * 用户类型 (phd/teacher)，这个是必须的
     */
    private String type;

    /**
     * 页码，默认为1
     */
    private int page = 1;

    /**
     * 每页记录数，默认为20
     */
    private int size = 20;

    /**
     * 搜索关键词，可选
     */
    private String keyword;

    /**
     * 状态筛选，可选
     */
    private String status;

}
