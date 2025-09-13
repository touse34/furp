package com.furp.VO;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class UserAddResponseVO implements Serializable {
    /*
            * 新创建用户的业务 ID (例如: "PhD003")
     */
    private String id;

    /**
     * 用户的创建时间
     */
    private LocalDateTime createTime;

}
