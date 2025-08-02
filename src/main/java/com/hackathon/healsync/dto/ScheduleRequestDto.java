package com.hackathon.healsync.dto;

import com.hackathon.healsync.entity.DoctorSchedule.ShiftType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequestDto {
    private List<ScheduleShift> shifts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleShift {
        private String dayOfWeek; // MONDAY, TUESDAY, etc.
        private ShiftType shiftType; // DAY, NIGHT, ON_CALL
        private LocalTime startTime;
        private LocalTime endTime;

        // Explicit getters and setters for IDE compatibility
        public String getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        
        public ShiftType getShiftType() { return shiftType; }
        public void setShiftType(ShiftType shiftType) { this.shiftType = shiftType; }
        
        public LocalTime getStartTime() { return startTime; }
        public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
        
        public LocalTime getEndTime() { return endTime; }
        public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    }

    // Explicit getters and setters for IDE compatibility
    public List<ScheduleShift> getShifts() { return shifts; }
    public void setShifts(List<ScheduleShift> shifts) { this.shifts = shifts; }
}
