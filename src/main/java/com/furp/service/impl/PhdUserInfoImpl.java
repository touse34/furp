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


    @Override
    public List<PhdUserInfo> getAllPhdWithSupervisors() {

        List<Map<String,Object>> raw = phdUserInfoMapper.getAllPhdWithSupervisors();
        Map<Integer, PhdUserInfo> merged = new LinkedHashMap<>();

        for (Map<String,Object> row : raw) {

            /* —— 安全转成 int —— */
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

            /* ➊ 接住返回值 */
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


}
