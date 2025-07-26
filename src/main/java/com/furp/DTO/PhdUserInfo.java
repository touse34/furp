package com.furp.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhdUserInfo {
    private Integer userId;         // phd 的 userId
    private Integer phdId;
    private String name;
    private Integer roleId;
    private String email;
    private List<Integer> supervisorIds = new ArrayList<>(); // 一个学生可能有多个导师

    // 新增：导师的姓名列表
    private List<String> supervisorNames = new ArrayList<>(); // 每个导师的姓名
}
