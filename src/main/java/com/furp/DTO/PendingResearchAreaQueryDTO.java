package com.furp.DTO;

import lombok.Data;
import java.io.Serializable;

@Data
public class PendingResearchAreaQueryDTO implements Serializable {
    private String keyword;
}
