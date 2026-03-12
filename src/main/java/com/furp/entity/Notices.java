package com.furp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("notices")
public class Notices {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;

    private String content;

    private LocalDateTime createTime;

    private Integer creatorId;

    private String type;

    private String status; // draft, sent, scheduled

    private String recipientType; // all, teacher, student

    private LocalDateTime sendTime; // actual send time

    private LocalDateTime scheduleTime; // scheduled send time
}
