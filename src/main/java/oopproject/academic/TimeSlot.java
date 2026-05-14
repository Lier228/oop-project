package oopproject.academic;

import java.io.Serializable;
import java.time.LocalTime;
import oopproject.enums.DaysOfWeek;

public class TimeSlot implements Serializable {
    private static final long serialVersionUID = 1L;

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
