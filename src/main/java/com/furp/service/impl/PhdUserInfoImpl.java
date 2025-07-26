package com.furp.service.impl;

import com.furp.DTO.PhdUserInfo;
import com.furp.mapper.PhdUserInfoMapper;
import com.furp.service.PhdUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PhdUserInfoImpl implements PhdUserInfoService{
    @Autowired
    private PhdUserInfoMapper phdUserInfoMapper;


    /*@Override
    public List<PhdUserInfo> getAllPhdWithSupervisors() {

        List<Map<String,Object>> raw = phdUserInfoMapper.getAllPhdWithSupervisors();
        Map<Integer, PhdUserInfo> merged = new LinkedHashMap<>();

        for (Map<String,Object> row : raw) {

            *//* —— 安全转成 int —— *//*
            Integer userId = ((Number) row.get("userId")).intValue();
            Integer phdId  = ((Number) row.get("phdId")).intValue();

            merged.computeIfAbsent(userId, k -> {
                PhdUserInfo dto = new PhdUserInfo();
                dto.setUserId(userId);
                dto.setPhdId(phdId);
                dto.setName((String) row.get("name"));
                dto.setRoleId(((Number) row.get("roleId")).intValue());
                dto.setEmail((String) row.get("email"));
                return dto;
            });

            *//* ➊ 接住返回值 *//*
            PhdUserInfo phd = merged.computeIfAbsent(userId, k -> {
                PhdUserInfo dto = new PhdUserInfo();
                dto.setUserId(userId);
                dto.setPhdId(phdId);
                dto.setName((String) row.get("name"));
                dto.setRoleId(((Number) row.get("roleId")).intValue());
                dto.setEmail((String) row.get("email"));
                dto.setSupervisorIds(new ArrayList<>());
                return dto;
            });

            Object concat = row.get("supervisorIds");          // 列别名要对！
            if (concat != null) {
                String str = concat.toString();                // "20,21,26"
                if (!str.isBlank()) {
                    for (String tid : str.split(",")) {
                        phd.getSupervisorIds().add(Integer.valueOf(tid.trim()));
                    }
                }
            }



        }
        return new ArrayList<>(merged.values());
    }
*/

    @Override
    public List<PhdUserInfo> getAllPhdWithSupervisors() {

        List<Map<String,Object>> raw = phdUserInfoMapper.getAllPhdWithSupervisors();
        Map<Integer, PhdUserInfo> merged = new LinkedHashMap<>();

        for (Map<String,Object> row : raw) {

            Integer userId = toInt(row.get("userId"));
            Integer phdId  = toInt(row.get("phdId"));

            // 只创建一次
            PhdUserInfo dto = merged.computeIfAbsent(userId, k -> {
                PhdUserInfo x = new PhdUserInfo();
                x.setUserId(userId);
                x.setPhdId(phdId);
                x.setName((String) row.get("name"));
                x.setRoleId(toInt(row.get("roleId")));
                x.setEmail((String) row.get("email"));
                x.setSupervisorIds(new ArrayList<>());
                x.setSupervisorNames(new ArrayList<>());
                return x;
            });

            // 解析两个 GROUP_CONCAT 字段
            addIntCsv(dto.getSupervisorIds(),   row.get("supervisorIds"));
            addStrCsv(dto.getSupervisorNames(), row.get("supervisorNames"));
        }
        return new ArrayList<>(merged.values());
    }

    /* ---------- 小工具方法 ---------- */
    private Integer toInt(Object o) {
        return o == null ? null : ((Number) o).intValue();
    }

    private void addIntCsv(List<Integer> target, Object csvObj) {
        if (csvObj == null) return;
        for (String s : csvObj.toString().split(",")) {
            if (!s.isBlank()) target.add(Integer.valueOf(s.trim()));
        }
    }

    private void addStrCsv(List<String> target, Object csvObj) {
        if (csvObj == null) return;
        for (String s : csvObj.toString().split(",")) {
            if (!s.isBlank()) target.add(s.trim());
        }
    }


}
