package com.hackathon.healsync.util;

import java.time.LocalDateTime;

public enum DoctorShift {
    DAY,
    NIGHT;

    public static DoctorShift fromTimeRange(LocalDateTime start, LocalDateTime end) {
        int startHour = start.getHour();
        int endHour = end.getHour();
        // Day shift: 8am to 2pm (08:00 to 14:00)
        if (startHour >= 8 && endHour <= 14) {
            return DAY;
        }
        // Night shift: 2pm to 10pm (14:00 to 22:00)
        if (startHour >= 14 && endHour <= 22) {
            return NIGHT;
        }
        // Default: treat as DAY if not matching above
        return DAY;
    }
}
