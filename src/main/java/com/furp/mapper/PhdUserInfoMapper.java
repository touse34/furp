package com.furp.mapper;


import com.furp.DTO.PhdUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PhdUserInfoMapper {


    /*
    * 获取所有 PhD 的信息，包括 PhD 的信息，以及 PhD 的所有导师信息
     */
    @Select("SELECT\n" +
            "    u.id      AS userId,\n" +
            "    u.name    AS name,\n" +
            "    u.role_id AS roleId,\n" +
            "    p.id      AS phdId,\n" +
            "    -- 如果想把主导师排在前面，可加 ORDER BY\n" +
            "    GROUP_CONCAT(s.teacher_id ORDER BY s.is_lead DESC) AS supervisorIds\n" +
            "FROM `user` u\n" +
            "JOIN  phd         p ON p.user_id = u.id\n" +
            "LEFT JOIN supervisor s ON s.phd_id = p.id\n" +
            "WHERE u.role_id = 2\n" +
            "GROUP BY u.id, u.name, u.role_id, p.id;")
    List<Map<String, Object>> getAllPhdWithSupervisors();
}
