package oopproject.academic;

import java.time.LocalTime;
import oopproject.enums.DaysOfWeek;

public class TimeSlot {
    private DaysOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeSlot(DaysOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DaysOfWeek getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
