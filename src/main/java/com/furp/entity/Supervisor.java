package com.furp.entity;

/*import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("supervisor")
public class Supervisor {
    private int phdId;
    private int teacherId;
    private int isLead;
}*/


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("supervisor")  // 指定表名为supervisor
public class Supervisor {
    private Integer phdId;     // 关联的PhD的ID
    private Integer teacherId; // 关联的教师的ID
    private Integer isLead;    // 是否是主导师，1表示是主导师，0表示不是主导师
}
