package com.furp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// PotentialAssignment class
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class PotentialAssignment {
        private int phdId;
        private int teacher1Id;
        private int teacher2Id;
        private TimeSlot timeSlot;
        private int skillScore;
    }