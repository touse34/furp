package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("supervisor")
public class Supervisor {
    private int phdId;
    private int teacherId;
    private int isLead;
}
