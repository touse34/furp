package com.furp.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo {
    private Integer userId;

    private String name;

    private Integer teacherId;

    private Integer phdId;

    private String role;

    private String token;




}
