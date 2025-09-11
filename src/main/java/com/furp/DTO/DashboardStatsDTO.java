package com.furp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {

    private Long totalPhds;
    private Long totalTeachers;
    private Long confirmedTeachers;
    private Long pendingTeachers;
    private Long totalSchedules;
    private Long totalTimeSlots;
    private Long pendingResearchAreaApprovals;
}
