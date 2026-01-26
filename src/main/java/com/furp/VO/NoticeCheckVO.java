package com.furp.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeCheckVO {
    private boolean hasNewNotification;
    private int unReadCount;
}
