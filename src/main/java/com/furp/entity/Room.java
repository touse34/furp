package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("meeting_room")
public class Room {
    private int id;
    private int userId;
    private int capacity;

}
