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
    private int userId;

    private String name;

    private int teacherId;

    private int phdId;

    private String role;

    private String token;




}
