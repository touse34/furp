package com.furp.entity;

/*import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("meeting_room")
public class Room {
    private int id;
    private int userId;
    private int capacity;

}*/

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("meeting_room")  // 表名
public class Room {
    @TableId
    private Integer id;         // 会议室的ID

    private String location;    // 会议室的位置
    private Integer capacity;   // 会议室的容量
}