package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.DTO.UserInfo;
import com.furp.DTO.UserPageQueryDTO;
import com.furp.VO.UserVO;
import com.furp.entity.Teacher;
import com.furp.entity.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户表的 Mapper 接口
 * 继承 BaseMapper<User> 后，就自动拥有了对 User 表的 CRUD 能力。
 * * @Mapper 注解是可选的，因为主启动类中已经使用了 @MapperScan。
 * 但保留它也无害。
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 如果有复杂的、自定义的 SQL 查询，可以在这里定义方法，并在 XML 文件中实现
    // 但对于基础的 CRUD, 这里已经完全足够了，无需任何代码。

    @Select("SELECT * from user where name = #{username}")
    User getUserByName(String username);


    /*
    查询所有的userInfo
     */
    @Select("SELECT id, role_id, `name` FROM `user`")
    List<UserInfo> findAll();



    @Select("SELECT id, role_id, `name` FROM `user` where role_id= #{roleId}")
    List<UserInfo> findByRole(Integer roleId);


    Long countTotalPhds();

    Long countTotalTeachers();

    Long countConfirmedTeachers();

    Long countPendingTeachers();

    Long countTotalSchedules();

    Long countTotalTimeSlots();

    Long countPendingResearchAreaApprovals();

    Page<UserVO> pageQuery(UserPageQueryDTO userPageQueryDTO);


    /**
     * 根据 phd 表的主键 ID 查询其所有导师的 ID 列表 (user.id)
     * @param phdId phd 表的主键 ID
     * @return 导师的用户ID列表
     */
    @Select("SELECT teacher_id FROM supervisor WHERE phd_id = #{phdId}")
    List<Integer> getSupervisorIdsByPhdId(Integer phdId);
    /*
    获取一个 PHD 的主要导师的 ID
     */
    @Select("SELECT teacher_id FROM supervisor WHERE phd_id = #{phdId} and is_lead=1")
    Integer getMainSupervisorIdByPhdId(Integer phdId);

    @Select("SELECT s.skill_name " +
            "FROM skill s " +
            "JOIN phd_skill ps ON s.id = ps.skill_id " +
            "WHERE ps.phd_id = #{phdId}")
    List<String> getResearchAreaNamesByPhdId(Integer phdId);
    @Select("SELECT s.skill_name " +
            "FROM skill s " +
            "JOIN teacher_skill ts ON s.id = ts.skill_id " +
            "WHERE ts.teacher_id = #{phdId}")
    List<String> getResearchAreaNamesByTeacherId(Integer teacherId);
}

